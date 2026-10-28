package com.institucion.aula_virtual.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.institucion.aula_virtual.model.Curso;
import com.institucion.aula_virtual.repository.CursoRepository;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    // ==================== CONSTRUCTOR MANUAL ====================
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<String> listarAreasUnicas() {
        return cursoRepository.findAreasUnicas();
    }

    public Page<Curso> filtrar(String nivel, String estado, String area, String buscar, Pageable pageable) {
        String nivelFiltro = (nivel == null || nivel.isEmpty()) ? null : nivel;
        String estadoFiltro = (estado == null || estado.isEmpty()) ? null : estado;
        String areaFiltro = (area == null || area.isEmpty()) ? null : area;
        String buscarFiltro = (buscar == null || buscar.isEmpty()) ? null : buscar;
        return cursoRepository.filtrar(nivelFiltro, estadoFiltro, areaFiltro, buscarFiltro, pageable);
    }

    public Optional<Curso> buscarPorId(Long id) {
        return cursoRepository.findById(id);
    }

    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }
}