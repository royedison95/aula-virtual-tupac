package com.institucion.aula_virtual.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.institucion.aula_virtual.model.Rol;
import com.institucion.aula_virtual.repository.RolRepository;

@Service
public class RolService {

    private final RolRepository rolRepository;

    // Constructor manual (sin Lombok)
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }
}