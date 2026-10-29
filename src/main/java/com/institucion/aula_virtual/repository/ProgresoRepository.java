package com.institucion.aula_virtual.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.institucion.aula_virtual.model.Curso;
import com.institucion.aula_virtual.model.Progreso;
import com.institucion.aula_virtual.model.Usuario;

public interface ProgresoRepository extends JpaRepository<Progreso, Long> {
    // Para buscar si el alumno ya tiene progreso en ese curso
    Optional<Progreso> findByEstudianteAndCurso(Usuario estudiante, Curso curso);
}