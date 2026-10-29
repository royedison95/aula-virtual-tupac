package com.institucion.aula_virtual.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.institucion.aula_virtual.model.Curso;
import com.institucion.aula_virtual.model.Evaluacion;
import com.institucion.aula_virtual.repository.EvaluacionRepository;

@Service
public class EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;

    public EvaluacionService(EvaluacionRepository evaluacionRepository) {
        this.evaluacionRepository = evaluacionRepository;
    }

    public List<Evaluacion> listarPorCurso(Curso curso) {
        return evaluacionRepository.findByCurso(curso);
    }

    public Optional<Evaluacion> buscarPorId(Long id) {
        return evaluacionRepository.findById(id);
    }

    public Evaluacion guardar(Evaluacion evaluacion) {
        return evaluacionRepository.save(evaluacion);
    }

    public void eliminar(Long id) {
        evaluacionRepository.deleteById(id);
    }
}