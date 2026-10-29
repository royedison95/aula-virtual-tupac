package com.institucion.aula_virtual.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.institucion.aula_virtual.model.Usuario;

@Controller
public class DashboardController {

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

    // ===== DASHBOARD PROFESOR =====
    @GetMapping("/profesor/dashboard")
    public String dashboardProfesor() {
        return "profesor/dashboard";
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
}