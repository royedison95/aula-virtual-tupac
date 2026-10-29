package com.institucion.aula_virtual.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "matricula")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_matricula")
    private LocalDateTime fechaMatricula;

    private String estado;
    
    @Column(name = "anio_escolar")
    private Integer anioEscolar;

    // Relación con el Estudiante
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estudiante")
    private Usuario estudiante;

    // Relación con el Curso
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_curso")
    private Curso curso;

    // ==================== CONSTRUCTORES ====================
    public Matricula() {
        this.fechaMatricula = LocalDateTime.now();
        this.estado = "ACTIVA";
    }

    // ==================== GETTERS Y SETTERS ====================
    public Long getId() { 
        return id;
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public LocalDateTime getFechaMatricula() { 
        return fechaMatricula; 
    }
    public void setFechaMatricula(LocalDateTime fechaMatricula) { 
        this.fechaMatricula = fechaMatricula; 
    }

    public String getEstado() { 
        return estado; 
    }
    public void setEstado(String estado) { 
        this.estado = estado; 
    }

    public Integer getAnioEscolar() {
        return anioEscolar; 
    }
    public void setAnioEscolar(Integer anioEscolar) { 
        this.anioEscolar = anioEscolar; 
    }

    public Usuario getEstudiante() { 
        return estudiante; 
    }
    public void setEstudiante(Usuario estudiante) { 
        this.estudiante = estudiante; 
    }

    public Curso getCurso() { 
        return curso; 
    }
    public void setCurso(Curso curso) { 
        this.curso = curso; 
    }
}