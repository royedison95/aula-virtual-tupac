package com.institucion.aula_virtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.institucion.aula_virtual.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
}