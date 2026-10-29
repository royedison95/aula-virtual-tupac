package com.institucion.aula_virtual.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.institucion.aula_virtual.model.Curso;
import com.institucion.aula_virtual.model.Progreso;
import com.institucion.aula_virtual.model.Usuario;
import com.institucion.aula_virtual.repository.ProgresoRepository;

@Service
public class ProgresoService {

    private final ProgresoRepository progresoRepository;

    public ProgresoService(ProgresoRepository progresoRepository) {
        this.progresoRepository = progresoRepository;
    }

    public Optional<Progreso> obtenerProgreso(Usuario estudiante, Curso curso) {
        return progresoRepository.findByEstudianteAndCurso(estudiante, curso);
    }

    public Progreso guardar(Progreso progreso) {
        progreso.setFechaUltimaActividad(LocalDateTime.now());
        return progresoRepository.save(progreso);
    }
}