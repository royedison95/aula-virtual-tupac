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
import com.institucion.aula_virtual.repository.MatriculaRepository;


@Controller
public class DashboardController {

  
    private final MatriculaRepository matriculaRepository; // <-- VA AQUÍ (junto a las variables)

    // EL CONSTRUCTOR VA AQUÍ
    public DashboardController( MatriculaRepository matriculaRepository) {
        
        this.matriculaRepository = matriculaRepository;
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

    // ===== DASHBOARD ADMIN  =====
    @GetMapping("/admin/dashboard") //mapping para el dashboard del administrador
    public String dashboardAdmin() {//retorna la vista del dashboard del administrador
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