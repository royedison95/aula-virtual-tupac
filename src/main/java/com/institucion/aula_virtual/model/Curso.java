package com.institucion.aula_virtual.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String imagenUrl;
    private String areaCurricular;

    private String nivel;
    private String gradoSeccion;
    private String bimestre;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    private String estado;
    private Integer duracionHoras;

    @Column(name = "id_profesor")
    private Long idProfesor;

    @Column(name = "id_grado")
    private Long idGrado;

    // ==================== CONSTRUCTORES ====================
    public Curso() {
    }

    public Curso(Long id, String titulo, String descripcion, String imagenUrl,
                 String areaCurricular, String nivel, String gradoSeccion,
                 String bimestre, LocalDateTime fechaCreacion, String estado,
                 Integer duracionHoras, Long idProfesor, Long idGrado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.areaCurricular = areaCurricular;
        this.nivel = nivel;
        this.gradoSeccion = gradoSeccion;
        this.bimestre = bimestre;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.duracionHoras = duracionHoras;
        this.idProfesor = idProfesor;
        this.idGrado = idGrado;
    }

    // ==================== GETTERS Y SETTERS ====================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getAreaCurricular() { return areaCurricular; }
    public void setAreaCurricular(String areaCurricular) { this.areaCurricular = areaCurricular; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getGradoSeccion() { return gradoSeccion; }
    public void setGradoSeccion(String gradoSeccion) { this.gradoSeccion = gradoSeccion; }

    public String getBimestre() { return bimestre; }
    public void setBimestre(String bimestre) { this.bimestre = bimestre; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getDuracionHoras() { return duracionHoras; }
    public void setDuracionHoras(Integer duracionHoras) { this.duracionHoras = duracionHoras; }

    public Long getIdProfesor() { return idProfesor; }
    public void setIdProfesor(Long idProfesor) { this.idProfesor = idProfesor; }

    public Long getIdGrado() { return idGrado; }
    public void setIdGrado(Long idGrado) { this.idGrado = idGrado; }
}