package com.institucion.aula_virtual.repository;
import java.util.List;

import org.springframework.data.domain.Page;// Importa la interfaz Page de Spring Data para manejar resultados paginados
import org.springframework.data.domain.Pageable;// Importa la interfaz Pageable de Spring Data para manejar la paginación de resultados
import org.springframework.data.jpa.repository.JpaRepository;// Importa la interfaz JpaRepository de Spring Data JPA para proporcionar operaciones CRUD y de paginación
import org.springframework.data.jpa.repository.Query;// Importa la anotación Query para definir consultas personalizadas 
import org.springframework.data.repository.query.Param;// Importa la anotación Param para vincular parámetros de consulta con variables de método

import com.institucion.aula_virtual.model.Curso;
import com.institucion.aula_virtual.model.Usuario;

public interface CursoRepository extends JpaRepository<Curso, Long> {

       //  NUEVO: Obtener áreas únicas de la BD
    @Query("SELECT DISTINCT c.areaCurricular FROM Curso c WHERE c.areaCurricular IS NOT NULL ORDER BY c.areaCurricular")
    List<String> findAreasUnicas();

    //  FILTRO CON LOS 4 PARÁMETROS: NIVEL, ESTADO, ÁREA, BUSCAR
    @Query("SELECT c FROM Curso c WHERE " +
           "(:nivel IS NULL OR c.nivel = :nivel) AND " +
           "(:estado IS NULL OR c.estado = :estado) AND " +
           "(:area IS NULL OR c.areaCurricular = :area) AND " +
           "(:buscar IS NULL OR LOWER(c.titulo) LIKE LOWER(CONCAT('%', :buscar, '%')))")
    Page<Curso> filtrar(@Param("nivel") String nivel,
                        @Param("estado") String estado,
                        @Param("area") String area,
                        @Param("buscar") String buscar,
                        Pageable pageable);

     List<Curso> findByProfesor(Usuario profesor); 
}