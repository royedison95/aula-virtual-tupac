package com.institucion.aula_virtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.institucion.aula_virtual.model.Opcion;

public interface OpcionRepository extends JpaRepository<Opcion, Long> {
}