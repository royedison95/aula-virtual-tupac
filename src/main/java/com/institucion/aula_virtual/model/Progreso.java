package com.institucion.aula_virtual.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "progreso")
public class Progreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "porcentaje_avance")
    private Double porcentajeAvance;

    @Column(name = "lecciones_completadas")
    private Integer leccionesCompletadas;

    @Column(name = "total_lecciones")
    private Integer totalLecciones;

    @Column(name = "fecha_ultima_actividad")
    private LocalDateTime fechaUltimaActividad;

    private Boolean completado;

    @ManyToOne
    @JoinColumn(name = "id_estudiante")
    private Usuario estudiante;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;

    // ==================== CONSTRUCTORES ====================
    public Progreso() {
        this.fechaUltimaActividad = LocalDateTime.now();
        this.completado = false;
        this.porcentajeAvance = 0.0;
    }

    // ==================== GETTERS Y SETTERS ====================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getPorcentajeAvance() { return porcentajeAvance; }
    public void setPorcentajeAvance(Double porcentajeAvance) { this.porcentajeAvance = porcentajeAvance; }

    public Integer getLeccionesCompletadas() { return leccionesCompletadas; }
    public void setLeccionesCompletadas(Integer leccionesCompletadas) { this.leccionesCompletadas = leccionesCompletadas; }

    public Integer getTotalLecciones() { return totalLecciones; }
    public void setTotalLecciones(Integer totalLecciones) { this.totalLecciones = totalLecciones; }

    public LocalDateTime getFechaUltimaActividad() { return fechaUltimaActividad; }
    public void setFechaUltimaActividad(LocalDateTime fechaUltimaActividad) { this.fechaUltimaActividad = fechaUltimaActividad; }

    public Boolean getCompletado() { return completado; }
    public void setCompletado(Boolean completado) { this.completado = completado; }

    public Usuario getEstudiante() { return estudiante; }
    public void setEstudiante(Usuario estudiante) { this.estudiante = estudiante; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
}