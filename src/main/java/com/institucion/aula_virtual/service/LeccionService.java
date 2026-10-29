package com.institucion.aula_virtual.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.institucion.aula_virtual.model.Curso;
import com.institucion.aula_virtual.model.Leccion;
import com.institucion.aula_virtual.repository.LeccionRepository;

@Service
public class LeccionService {

    private final LeccionRepository leccionRepository;

    public LeccionService(LeccionRepository leccionRepository) {
        this.leccionRepository = leccionRepository;
    }

    public List<Leccion> listarPorCurso(Curso curso) {
        return leccionRepository.findByCursoOrderByOrdenAsc(curso);
    }

    public Leccion guardar(Leccion leccion) {
        return leccionRepository.save(leccion);
    }

    public void eliminar(Long id) {
        leccionRepository.deleteById(id);
    }

        public Optional<Leccion> buscarPorId(Long id) {
        return leccionRepository.findById(id);
    }
}