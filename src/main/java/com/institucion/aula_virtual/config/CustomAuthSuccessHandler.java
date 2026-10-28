package com.institucion.aula_virtual.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.institucion.aula_virtual.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // Obtener el rol seleccionado en el login
        String rolSeleccionado = request.getParameter("rol");

        // Obtener el rol real del usuario autenticado
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String rolReal = usuario.getRol();

        // Comparar
        if (rolSeleccionado != null && !rolSeleccionado.equals(rolReal)) {
            //  Rol no coincide entonces Redirigir con error
            response.sendRedirect("/login?error=rol");
            return;
        }
            switch (rolReal) {
                case "ADMIN" -> response.sendRedirect("/admin/dashboard");
                case "PROFESOR" -> response.sendRedirect("/profesor/dashboard");
                case "ESTUDIANTE" -> response.sendRedirect("/estudiante/dashboard");
                case "APODERADO" -> response.sendRedirect("/apoderado/dashboard");
                default -> response.sendRedirect("/dashboard");
                }
    }
}