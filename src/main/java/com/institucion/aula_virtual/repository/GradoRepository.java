package com.institucion.aula_virtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.institucion.aula_virtual.model.Grado;

public interface GradoRepository extends JpaRepository<Grado, Long> {
    
}