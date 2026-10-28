package com.institucion.aula_virtual.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.institucion.aula_virtual.model.Grado;
import com.institucion.aula_virtual.repository.GradoRepository;

@Service
public class GradoService {

    private final GradoRepository gradoRepository;

    // ==================== CONSTRUCTOR MANUAL ====================
    public GradoService(GradoRepository gradoRepository) {
        this.gradoRepository = gradoRepository;
    }

    public List<Grado> listarTodos() {
        return gradoRepository.findAll();
    }
}