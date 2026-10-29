package com.institucion.aula_virtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.institucion.aula_virtual.model.PasswordResetToken;
import com.institucion.aula_virtual.model.Usuario;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUsuario(Usuario usuario); 
}