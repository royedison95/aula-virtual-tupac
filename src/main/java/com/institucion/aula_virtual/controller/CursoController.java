package com.institucion.aula_virtual.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.institucion.aula_virtual.model.Curso;
import com.institucion.aula_virtual.repository.MatriculaRepository;
import com.institucion.aula_virtual.service.CursoService;
import com.institucion.aula_virtual.service.GradoService;
import com.institucion.aula_virtual.service.UsuarioService;

@Controller
@RequestMapping("/admin/cursos")
@PreAuthorize("hasRole('ADMIN')")
public class CursoController {

    private final CursoService cursoService;
    private final UsuarioService usuarioService;
    private final GradoService gradoService;
    private final MatriculaRepository matriculaRepository;

    // ==================== CONSTRUCTOR MANUAL ====================
    public CursoController(CursoService cursoService,
                           UsuarioService usuarioService,
                           GradoService gradoService,
                           MatriculaRepository matriculaRepository) {
        this.cursoService = cursoService;
        this.usuarioService = usuarioService;
        this.gradoService = gradoService;
        this.matriculaRepository = matriculaRepository;
    }

    // ===== LISTAR CON LOS 4 FILTROS =====
    @GetMapping
    public String listar(
            @RequestParam(value = "nivel", required = false) String nivel,
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "area", required = false) String area,
            @RequestParam(value = "buscar", required = false) String buscar,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Curso> cursosPage = cursoService.filtrar(nivel, estado, area, buscar, pageable);

        model.addAttribute("cursos", cursosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cursosPage.getTotalPages());
        model.addAttribute("totalItems", cursosPage.getTotalElements());
        model.addAttribute("nivelFiltro", nivel);
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("areaFiltro", area);
        model.addAttribute("buscarFiltro", buscar);
        model.addAttribute("size", size);

        model.addAttribute("profesores", usuarioService.listarPorRol("PROFESOR"));
        model.addAttribute("grados", gradoService.listarTodos());
        model.addAttribute("areas", cursoService.listarAreasUnicas());

         // Contar alumnos por curso
        Map<Long, Long> conteoAlumnos = new HashMap<>();
        for (Curso c : cursosPage.getContent()) {
            conteoAlumnos.put(c.getId(), matriculaRepository.countByCurso(c));
        }
        model.addAttribute("conteoAlumnos", conteoAlumnos);
        
        return "admin/cursos/lista";
    }

    // ===== GUARDAR =====
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Curso curso, RedirectAttributes redirectAttributes) {
        if (curso.getId() == null) {
            curso.setEstado("ACTIVO");
        }
        cursoService.guardar(curso);
        redirectAttributes.addFlashAttribute("mensaje", "Curso guardado correctamente.");
        return "redirect:/admin/cursos";
    }

    // ===== ELIMINAR =====
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cursoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Curso eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar este curso porque tiene lecciones o matrículas asociadas.");
        }

        return "redirect:/admin/cursos";
    }
}