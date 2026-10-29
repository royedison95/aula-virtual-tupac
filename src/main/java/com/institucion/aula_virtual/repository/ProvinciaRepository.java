package com.institucion.aula_virtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.institucion.aula_virtual.model.Provincia;

public interface ProvinciaRepository extends JpaRepository<Provincia, String> {
    List<Provincia> findByDepartmentId(String departmentId);
}