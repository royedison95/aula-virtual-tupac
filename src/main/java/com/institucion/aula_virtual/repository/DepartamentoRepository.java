package com.institucion.aula_virtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.institucion.aula_virtual.model.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, String> {
}