package com.institucion.aula_virtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.institucion.aula_virtual.model.Evaluacion;
import com.institucion.aula_virtual.model.Pregunta;

public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    List<Pregunta> findByEvaluacionOrderByOrdenAsc(Evaluacion evaluacion);
}