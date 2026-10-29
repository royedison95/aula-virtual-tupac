package com.institucion.aula_virtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.institucion.aula_virtual.model.Evaluacion;
import com.institucion.aula_virtual.model.EvaluacionEstudiante;
import com.institucion.aula_virtual.model.Usuario;

public interface EvaluacionEstudianteRepository extends JpaRepository<EvaluacionEstudiante, Long> {
    List<EvaluacionEstudiante> findByEstudiante(Usuario estudiante);
    EvaluacionEstudiante findByEstudianteAndEvaluacion(Usuario estudiante, Evaluacion evaluacion);
}