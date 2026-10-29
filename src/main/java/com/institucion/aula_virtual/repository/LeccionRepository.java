package com.institucion.aula_virtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.institucion.aula_virtual.model.Curso;
import com.institucion.aula_virtual.model.Leccion;

public interface LeccionRepository extends JpaRepository<Leccion, Long> {
    
    // Para listar las lecciones de un curso ordenadas por número de orden
    List<Leccion> findByCursoOrderByOrdenAsc(Curso curso);
    
    // Para contar cuántas lecciones tiene un curso
    long countByCurso(Curso curso);
}