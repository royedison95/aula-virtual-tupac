package com.institucion.aula_virtual.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.institucion.aula_virtual.repository.CursoRepository;
import com.institucion.aula_virtual.repository.MatriculaRepository;
import com.institucion.aula_virtual.repository.ProgresoRepository;
import com.institucion.aula_virtual.repository.UsuarioRepository;

@Controller
@RequestMapping("/admin/reportes")
@PreAuthorize("hasRole('ADMIN')")
public class ReporteController {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;
    private final ProgresoRepository progresoRepository;

    public ReporteController(UsuarioRepository usuarioRepository,
                             CursoRepository cursoRepository,
                             MatriculaRepository matriculaRepository,
                             ProgresoRepository progresoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.matriculaRepository = matriculaRepository;
        this.progresoRepository = progresoRepository;
    }

    @GetMapping
    public String verReportes(Model model) {
        // 1. Reporte de Usuarios por Rol
        model.addAttribute("estudiantes", usuarioRepository.countByRol("ESTUDIANTE"));
        model.addAttribute("profesores", usuarioRepository.countByRol("PROFESOR"));
        model.addAttribute("admins", usuarioRepository.countByRol("ADMIN"));
        model.addAttribute("apoderados", usuarioRepository.countByRol("APODERADO"));

        // 2. Reporte de Cursos y Matrículas
        model.addAttribute("cursosActivos", cursoRepository.countByEstado("ACTIVO"));
        model.addAttribute("cursosBorrador", cursoRepository.countByEstado("BORRADOR"));
        model.addAttribute("totalMatriculas", matriculaRepository.count());
        
        // 3. Listado completo de matrículas (para el reporte detallado)
        model.addAttribute("listaMatriculas", matriculaRepository.findAll());
        
        // 4. Reporte de Progreso (Listado de todos los progresos)
        model.addAttribute("listaProgresos", progresoRepository.findAll());

        return "admin/reportes";
    }
}