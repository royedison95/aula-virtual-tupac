package com.institucion.aula_virtual.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.institucion.aula_virtual.model.Distrito;
import com.institucion.aula_virtual.model.Provincia;
import com.institucion.aula_virtual.repository.DistritoRepository;
import com.institucion.aula_virtual.repository.ProvinciaRepository;

@RestController
@RequestMapping("/api/ubicacion")
public class UbicacionController {

    private final ProvinciaRepository provinciaRepository;
    private final DistritoRepository distritoRepository;

    public UbicacionController(ProvinciaRepository provinciaRepository, DistritoRepository distritoRepository) {
        this.provinciaRepository = provinciaRepository;
        this.distritoRepository = distritoRepository;
    }

    @GetMapping("/provincias/{departamentoId}")
    public List<Provincia> getProvincias(@PathVariable String departamentoId) {
        return provinciaRepository.findByDepartmentId(departamentoId);
    }

    @GetMapping("/distritos/{provinciaId}")
    public List<Distrito> getDistritos(@PathVariable String provinciaId) {
        return distritoRepository.findByProvinceId(provinciaId);
    }
}