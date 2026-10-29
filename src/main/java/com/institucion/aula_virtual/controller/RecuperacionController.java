package com.institucion.aula_virtual.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.institucion.aula_virtual.model.PasswordResetToken;
import com.institucion.aula_virtual.model.Usuario;
import com.institucion.aula_virtual.repository.PasswordResetTokenRepository;
import com.institucion.aula_virtual.service.EmailService;
import com.institucion.aula_virtual.service.UsuarioService;

@Controller
public class RecuperacionController {

    private final UsuarioService usuarioService;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public RecuperacionController(UsuarioService usuarioService,
                                  PasswordResetTokenRepository tokenRepository,
                                  EmailService emailService,
                                  PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. Mostrar formulario de recuperación
    @GetMapping("/recuperar-password")
    public String mostrarFormulario() {
        return "recuperar-password"; 
    }

    // 2. Validar Usuario y DNI, y enviar el correo (CON BLINDAJE DE ERRORES)
    @PostMapping("/recuperar-password")
    public String procesarRecuperacion(@RequestParam("codigoUsuario") String codigoUsuario,
                                       @RequestParam("dni") String dni, 
                                       Model model) {
        
        // 1. Buscamos al usuario
        Usuario usuario = usuarioService.buscarPorCodigoUsuario(codigoUsuario).orElse(null);
        
        // 2. Validamos que el usuario exista
        if (usuario == null) {
            model.addAttribute("error", "El código de usuario no existe en nuestros registros.");
            return "recuperar-password";
        }

        // 3. Validamos que el usuario tenga un DNI registrado (evita error null)
        if (usuario.getDni() == null || usuario.getDni().isEmpty()) {
            model.addAttribute("error", "Este usuario no tiene un DNI registrado. Contacta al administrador.");
            return "recuperar-password";
        }

        // 4. Validamos que el DNI coincida
        if (!usuario.getDni().equals(dni)) {
            model.addAttribute("error", "El DNI ingresado no coincide con el usuario.");
            return "recuperar-password";
        }

         // --- SI TODO ESTÁ BIEN, INTENTAMOS ENVIAR EL CORREO ---
        try {
            // Borramos el token viejo si existe para evitar el error de duplicado
            PasswordResetToken tokenViejo = tokenRepository.findByUsuario(usuario);
            if (tokenViejo != null) {
                tokenRepository.delete(tokenViejo);
            }

            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUsuario(usuario);
            resetToken.setExpiracion(LocalDateTime.now().plusHours(24)); 
            tokenRepository.save(resetToken);

            String url = "http://localhost:8080/cambiar-password?token=" + token;
            emailService.enviarCorreoRecuperacion(usuario.getEmail(), url);
            
            model.addAttribute("mensaje", "Verificación exitosa. Hemos enviado un enlace a tu correo: " + usuario.getEmail());
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al enviar el correo. Detalle técnico: " + e.getMessage());
            System.out.println("Error de correo: " + e.getMessage()); 
        }
        
        return "recuperar-password"; 
    }

    // 3. Mostrar formulario para escribir la nueva contraseña (viene desde el correo)
    @GetMapping("/cambiar-password")
    public String mostrarCambioPassword(@RequestParam("token") String token, Model model) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        
        if (resetToken == null || resetToken.getExpiracion().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "El enlace de recuperación es inválido o ha expirado.");
            return "recuperar-password";
        }

        model.addAttribute("token", token);
        return "cambiar-password"; 
    }

    // 4. Guardar la nueva contraseña
    @PostMapping("/cambiar-password")
    public String guardarNuevaPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       Model model) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        
        if (resetToken == null || resetToken.getExpiracion().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "El enlace es inválido o ha expirado.");
            return "recuperar-password";
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setPassword(passwordEncoder.encode(password)); 
        usuarioService.guardar(usuario);

        tokenRepository.delete(resetToken); 

        return "redirect:/login?resetExitoso=true";
    }
}