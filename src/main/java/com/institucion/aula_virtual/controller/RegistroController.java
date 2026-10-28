package com.institucion.aula_virtual.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.institucion.aula_virtual.model.Usuario;
import com.institucion.aula_virtual.service.UsuarioService;

@Controller
public class RegistroController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public RegistroController(UsuarioService usuarioService,
                              PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Usuario usuario, Model model) {

        //  VALIDAR DNI DUPLICADO
        Optional<Usuario> usuarioPorDni = usuarioService.buscarPorDni(usuario.getDni());
        if (usuarioPorDni.isPresent()) {
            model.addAttribute("error", "El DNI " + usuario.getDni() + " ya está registrado.");
            return "registro";
        }

        //  VALIDAR EMAIL DUPLICADO
        Optional<Usuario> usuarioPorEmail = usuarioService.buscarPorEmail(usuario.getEmail());
        if (usuarioPorEmail.isPresent()) {
            model.addAttribute("error", "El correo " + usuario.getEmail() + " ya está registrado.");
            return "registro";
        }

        // VALIDAR ROL VÁLIDO
        String rol = usuario.getRol();
        if (rol == null || rol.isEmpty() ||
            (!rol.equals("ADMIN") && !rol.equals("PROFESOR") &&
             !rol.equals("ESTUDIANTE") && !rol.equals("APODERADO"))) {
            model.addAttribute("error", "Debes seleccionar un perfil válido.");
            return "registro";
        }

        //  ENCRIPTAR CONTRASEÑA
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEstado(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        usuarioService.guardar(usuario);
        return "redirect:/login?registroExitoso=true";
    }
}