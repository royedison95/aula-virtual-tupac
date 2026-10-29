package com.institucion.aula_virtual.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.institucion.aula_virtual.model.Curso;
import com.institucion.aula_virtual.model.Matricula;
import com.institucion.aula_virtual.model.Usuario;
import com.institucion.aula_virtual.repository.CursoRepository;
import com.institucion.aula_virtual.repository.MatriculaRepository;
import com.institucion.aula_virtual.repository.UsuarioRepository;


@Controller
public class DashboardController {

  
    private final MatriculaRepository matriculaRepository; // <-- VA AQUÍ (junto a las variables)
    private final UsuarioRepository usuarioRepository; // <-- NUEVO
    private final CursoRepository cursoRepository;     // <-- NUEVO

    // EL CONSTRUCTOR VA AQUÍ
    public DashboardController( MatriculaRepository matriculaRepository, UsuarioRepository usuarioRepository, CursoRepository cursoRepository) {
        this.matriculaRepository = matriculaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }



    // ===== REDIRIGIR SEGÚN ROL =====
    @GetMapping("/dashboard")
    public String redirigirDashboard(Authentication auth) {
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PROFESOR"))) {
            return "redirect:/profesor/dashboard";
        } else {
            return "redirect:/estudiante/dashboard";
        }
    }

    // ===== DASHBOARD ADMIN (DINÁMICO) =====
    @GetMapping("/admin/dashboard")
    public String dashboardAdmin(Model model) {
        // 1. Hacemos las consultas a la base de datos
        long totalEstudiantes = usuarioRepository.countByRol("ESTUDIANTE");
        long totalProfesores = usuarioRepository.countByRol("PROFESOR");
        long cursosActivos = cursoRepository.countByEstado("ACTIVO");
        long totalMatriculas = matriculaRepository.count();

        // 2. Enviamos los números a la vista HTML
        model.addAttribute("totalEstudiantes", totalEstudiantes);
        model.addAttribute("totalProfesores", totalProfesores);
        model.addAttribute("cursosActivos", cursosActivos);
        model.addAttribute("totalMatriculas", totalMatriculas);

        return "admin/dashboard";
    }


    // ===== DASHBOARD ESTUDIANTE =====
    @GetMapping("/estudiante/dashboard")
    public String dashboardEstudiante( Authentication auth, Model model) {
        // Obtenemos al usuario logueado
        Usuario usuario = (Usuario) auth.getPrincipal();
        
        // Lo enviamos al HTML
        model.addAttribute("usuarioEstudiante", usuario);
        return "estudiante/dashboard";
    }
    
    // ===== DASHBOARD APODERADO =====
     @GetMapping("/apoderado/dashboard")
    public String dashboardApoderado() {
        return "apoderado/dashboard";
    }


// NUEVO MÉTODO: Ver los cursos del estudiante (VA AL FINAL DEL ARCHIVO, ANTES DE LA ÚLTIMA LLAVE)
    @GetMapping("/estudiante/mis-cursos")
    public String misCursosEstudiante(Authentication auth, Model model) {
        Usuario usuario = (Usuario) auth.getPrincipal();
        
        // Buscamos todas las matrículas de este estudiante
        List<Matricula> matriculas = matriculaRepository.findByEstudiante(usuario);
        
        // Extraemos solo los cursos de esas matrículas
                List<Curso> cursosInscritos = matriculas.stream()
                .map(m -> m.getCurso())
                .filter(curso -> curso != null)
                .collect(Collectors.toList());

        model.addAttribute("usuarioEstudiante", usuario);
        model.addAttribute("misCursos", cursosInscritos);
        
        return "estudiante/mis-cursos"; 
    }


}