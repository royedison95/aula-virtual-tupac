package com.institucion.aula_virtual.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.institucion.aula_virtual.model.Usuario;
import com.institucion.aula_virtual.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // ==================== CONSTRUCTOR MANUAL ====================
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    public List<Usuario> listarPorEstado(Boolean estado) {
        return usuarioRepository.findByEstado(estado);
    }

    public List<Usuario> listarPorRolYEstado(String rol, Boolean estado) {
        return usuarioRepository.findByRolAndEstado(rol, estado);
    }

    public Page<Usuario> buscar(String buscar, String rol, Boolean estado, Pageable pageable) {
        String buscarFiltro = (buscar == null || buscar.isEmpty()) ? null : buscar;
        String rolFiltro = (rol == null || rol.isEmpty() || rol.equals("TODOS")) ? null : rol;
        return usuarioRepository.buscar(buscarFiltro, rolFiltro, estado, pageable);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }


    //  Buscar por DNI (AVANCE 3)
    public Optional<Usuario> buscarPorDni(String dni) {
    return usuarioRepository.findByDni(dni);
    }
}