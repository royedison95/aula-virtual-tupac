package com.institucion.aula_virtual.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.institucion.aula_virtual.model.Matricula;
import com.institucion.aula_virtual.service.CursoService;
import com.institucion.aula_virtual.service.MatriculaService;
import com.institucion.aula_virtual.service.UsuarioService;

@Controller
@RequestMapping("/admin/matriculas")
@PreAuthorize("hasRole('ADMIN')")
public class MatriculaController {

    private final MatriculaService matriculaService;
    private final UsuarioService usuarioService;
    private final CursoService cursoService;

    public MatriculaController(MatriculaService matriculaService, UsuarioService usuarioService, CursoService cursoService) {
        this.matriculaService = matriculaService;
        this.usuarioService = usuarioService;
        this.cursoService = cursoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("matriculas", matriculaService.listarTodas());
        model.addAttribute("estudiantes", usuarioService.listarPorRol("ESTUDIANTE"));
        // Nota: Idealmente aquí deberías traer solo cursos activos
        model.addAttribute("cursos", cursoService.listarTodos()); 
        return "admin/matriculas/lista";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Matricula matricula, RedirectAttributes redirectAttributes) {
        matriculaService.guardar(matricula);
        redirectAttributes.addFlashAttribute("mensaje", "Estudiante matriculado correctamente.");
        return "redirect:/admin/matriculas";
    }
}