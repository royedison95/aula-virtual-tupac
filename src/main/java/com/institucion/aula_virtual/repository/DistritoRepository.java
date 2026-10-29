package com.institucion.aula_virtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.institucion.aula_virtual.model.Distrito;

public interface DistritoRepository extends JpaRepository<Distrito, String> {
    List<Distrito> findByProvinceId(String provinceId);
}