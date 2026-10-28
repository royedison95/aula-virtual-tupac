package com.institucion.aula_virtual.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;// Importa la interfaz JpaRepository de Spring Data JPA para proporcionar operaciones CRUD y de paginación
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.institucion.aula_virtual.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    //  Validar DNI duplicado (AVANCE 3)
    Optional<Usuario> findByDni(String dni);

    List<Usuario> findByRol(String rol);


    //  NUEVO: Buscar por estado (activos/inactivos)
    List<Usuario> findByEstado(Boolean estado);

    //  NUEVO: Buscar por rol Y estado
    List<Usuario> findByRolAndEstado(String rol, Boolean estado);



    @Query("SELECT u FROM Usuario u WHERE " +
       "(:buscar IS NULL OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :buscar, '%')) " +
       "OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :buscar, '%')) " +
       "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :buscar, '%'))) AND " +
       "(:rol IS NULL OR u.rol = :rol) AND " +
       "(:estado IS NULL OR u.estado = :estado)")
Page<Usuario> buscar(@Param("buscar") String buscar,
                     @Param("rol") String rol,
                     @Param("estado") Boolean estado,
                     Pageable pageable);
}
