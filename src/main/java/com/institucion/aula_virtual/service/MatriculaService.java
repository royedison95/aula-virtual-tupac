package com.institucion.aula_virtual.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.institucion.aula_virtual.model.Matricula;
import com.institucion.aula_virtual.repository.MatriculaRepository;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;

    public MatriculaService(MatriculaRepository matriculaRepository) {
        this.matriculaRepository = matriculaRepository;
    }

    public List<Matricula> listarTodas() {
        return matriculaRepository.findAll();
    }

    public Matricula guardar(Matricula matricula) {
        return matriculaRepository.save(matricula);
    }
    
    public void eliminar(Long id) {
        matriculaRepository.deleteById(id);
    }
}