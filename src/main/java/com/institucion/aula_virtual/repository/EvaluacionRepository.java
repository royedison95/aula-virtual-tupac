package com.institucion.aula_virtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.institucion.aula_virtual.model.Curso;
import com.institucion.aula_virtual.model.Evaluacion;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findByCurso(Curso curso);
}