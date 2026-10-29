package com.institucion.aula_virtual.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.institucion.aula_virtual.model.Usuario;
import com.institucion.aula_virtual.repository.DepartamentoRepository;
import com.institucion.aula_virtual.service.RolService;
import com.institucion.aula_virtual.service.UsuarioService;

@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final RolService rolService; 
    private final DepartamentoRepository departamentoRepository; 

    // ==================== CONSTRUCTOR MANUAL ====================
    public UsuarioController(UsuarioService usuarioService,
                             PasswordEncoder passwordEncoder, RolService rolService, DepartamentoRepository departamentoRepository) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.rolService = rolService;
        this.departamentoRepository = departamentoRepository;
    }

    // ===== LISTAR USUARIOS (CON BUSCADOR Y FILTROS) =====
    @GetMapping
    public String listar(
            @RequestParam(value = "buscar", required = false) String buscar,
            @RequestParam(value = "rol", required = false) String rol,
            @RequestParam(value = "estado", required = false, defaultValue = "ACTIVOS") String estado,
            @RequestParam(value = "departamento", required = false) String departamento,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        boolean esActivo = "ACTIVOS".equals(estado);

        Pageable pageable = PageRequest.of(page, size);
        Page<Usuario> usuariosPage = usuarioService.buscar(buscar, rol, esActivo, departamento, pageable);

        model.addAttribute("usuarios", usuariosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usuariosPage.getTotalPages());
        model.addAttribute("totalItems", usuariosPage.getTotalElements());
        model.addAttribute("buscarFiltro", buscar);
        model.addAttribute("rolFiltro", rol == null ? "TODOS" : rol);
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("size", size);
        model.addAttribute("departamentosFiltro", departamentoRepository.findAll());
        model.addAttribute("departamentoFiltro", departamento);

        return "admin/usuarios/lista";
    }

    // ===== FORMULARIO CREAR =====
    @GetMapping("/nuevo")
    public String formularioCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolService.listarTodos()); 
        model.addAttribute("departamentos", departamentoRepository.findAll());
        return "admin/usuarios/formulario";
    }

    // ===== FORMULARIO EDITAR =====
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.listarTodos()); 
        model.addAttribute("departamentos", departamentoRepository.findAll());
        return "admin/usuarios/formulario";
    }

    // ===== GUARDAR =====
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario, Model model) {

        // 1. Validar email duplicado
        Optional<Usuario> usuarioExistente = usuarioService.buscarPorEmail(usuario.getEmail());
        if (usuarioExistente.isPresent()) {
            if (usuario.getId() == null || !usuarioExistente.get().getId().equals(usuario.getId())) {
                model.addAttribute("error", "El correo " + usuario.getEmail() + " ya está registrado.");
                model.addAttribute("usuario", usuario);
                model.addAttribute("roles", rolService.listarTodos()); 
                model.addAttribute("departamentos", departamentoRepository.findAll());
                return "admin/usuarios/formulario";
            }
        }

        // 2. NUEVO: Validar DNI duplicado
        if (usuario.getDni() != null && !usuario.getDni().isEmpty()) {
            Optional<Usuario> usuarioPorDni = usuarioService.buscarPorDni(usuario.getDni());
            if (usuarioPorDni.isPresent()) {
                // Si el DNI existe, pero pertenece a OTRO usuario (no al que estamos editando)
                if (usuario.getId() == null || !usuarioPorDni.get().getId().equals(usuario.getId())) {
                    model.addAttribute("error", "El DNI " + usuario.getDni() + " ya está registrado en otro usuario.");
                    model.addAttribute("usuario", usuario);
                    model.addAttribute("roles", rolService.listarTodos()); 
                    model.addAttribute("departamentos", departamentoRepository.findAll());
                    return "admin/usuarios/formulario";
                }
            }
        }

        // 3. NUEVO USUARIO: ENCRIPTAR CONTRASEÑA
        if (usuario.getId() == null) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            usuario.setEstado(true);
            usuario.setFechaRegistro(LocalDateTime.now());
        } else {
            // EDITAR: Si no se cambió la contraseña, mantener la existente
            Usuario usuarioActual = usuarioService.buscarPorId(usuario.getId()).orElse(null);
            if (usuarioActual != null) {
                if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                    usuario.setPassword(usuarioActual.getPassword());
                } else {
                    // Si escribió una nueva contraseña, encriptarla
                    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
                }
            }
        }

        usuarioService.guardar(usuario);
        return "redirect:/admin/usuarios";
    }

    // ===== DESACTIVAR =====
    @PostMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        if (usuario != null) {
            usuario.setEstado(false);
            usuarioService.guardar(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario desactivado correctamente.");
        }
        return "redirect:/admin/usuarios";
    }

    // ===== REACTIVAR =====
    @PostMapping("/reactivar/{id}")
    public String reactivar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        if (usuario != null) {
            usuario.setEstado(true);
            usuarioService.guardar(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario reactivado correctamente.");
        }
        return "redirect:/admin/usuarios";
    }
}