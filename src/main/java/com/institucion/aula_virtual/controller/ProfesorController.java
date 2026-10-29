package com.institucion.aula_virtual.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.institucion.aula_virtual.model.Curso;
import com.institucion.aula_virtual.model.Leccion;
import com.institucion.aula_virtual.model.Usuario;
import com.institucion.aula_virtual.repository.CursoRepository;
import com.institucion.aula_virtual.service.LeccionService;

@Controller
@RequestMapping("/profesor")
@PreAuthorize("hasRole('PROFESOR')")
public class ProfesorController {

    private final CursoRepository cursoRepository;
    private final LeccionService leccionService;

    public ProfesorController(CursoRepository cursoRepository, LeccionService leccionService) {
        this.cursoRepository = cursoRepository;
        this.leccionService = leccionService;
    }

    // 1. Dashboard del Profesor (Ver sus cursos asignados)
    @GetMapping("/dashboard")
    public String dashboardProfesor(Authentication auth, Model model) {
        Usuario profesor = (Usuario) auth.getPrincipal();
        
        // Buscamos los cursos que le pertenecen a este profesor
        List<Curso> misCursos = cursoRepository.findByProfesor(profesor);
        
        model.addAttribute("usuarioProfesor", profesor);
        model.addAttribute("misCursos", misCursos);
        return "profesor/dashboard";
    }

    // 2. Ver las lecciones de un curso específico
    @GetMapping("/curso/{idCurso}/lecciones")
    public String gestionarLecciones(@PathVariable Long idCurso, Model model) {
        Curso curso = cursoRepository.findById(idCurso).orElse(null);
        model.addAttribute("curso", curso);
        model.addAttribute("lecciones", leccionService.listarPorCurso(curso));
        model.addAttribute("nuevaLeccion", new Leccion());
        return "profesor/lecciones"; // Vista para subir material
    }

    // 3. Guardar la lección creada por el profesor
    @PostMapping("/curso/{idCurso}/lecciones/guardar")
    public String guardarLeccion(@PathVariable Long idCurso, @ModelAttribute Leccion leccion, RedirectAttributes redirectAttributes) {
        Curso curso = cursoRepository.findById(idCurso).orElse(null);
        if (curso != null) {
            leccion.setCurso(curso);
            leccionService.guardar(leccion);
            redirectAttributes.addFlashAttribute("mensaje", "Lección creada correctamente.");
        }
        return "redirect:/profesor/curso/" + idCurso + "/lecciones";
    }
}