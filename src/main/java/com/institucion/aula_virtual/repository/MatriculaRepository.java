package com.institucion.aula_virtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.institucion.aula_virtual.model.Curso;
import com.institucion.aula_virtual.model.Matricula;
import com.institucion.aula_virtual.model.Usuario;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    
    // Para contar cuántos alumnos hay en un curso
    long countByCurso(Curso curso);

    // Para listar las matrículas de un curso
    List<Matricula> findByCurso(Curso curso);

    // NUEVO: Para listar las matrículas de un estudiante
    List<Matricula> findByEstudiante(Usuario estudiante); 
}