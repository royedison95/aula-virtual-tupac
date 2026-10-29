package com.institucion.aula_virtual.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.institucion.aula_virtual.model.Evaluacion;
import com.institucion.aula_virtual.model.Opcion;
import com.institucion.aula_virtual.model.Pregunta;
import com.institucion.aula_virtual.repository.OpcionRepository;
import com.institucion.aula_virtual.repository.PreguntaRepository;

@Service
public class PreguntaService {

    private final PreguntaRepository preguntaRepository;
    private final OpcionRepository opcionRepository;

    public PreguntaService(PreguntaRepository preguntaRepository, OpcionRepository opcionRepository) {
        this.preguntaRepository = preguntaRepository;
        this.opcionRepository = opcionRepository;
    }

    public List<Pregunta> listarPorEvaluacion(Evaluacion evaluacion) {
        return preguntaRepository.findByEvaluacionOrderByOrdenAsc(evaluacion);
    }

    public Pregunta guardarPregunta(Pregunta pregunta) {
        return preguntaRepository.save(pregunta);
    }

    public void guardarOpcion(Opcion opcion) {
        opcionRepository.save(opcion);
    }
}