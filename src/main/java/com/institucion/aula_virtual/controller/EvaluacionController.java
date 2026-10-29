package com.institucion.aula_virtual.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.institucion.aula_virtual.model.Curso;
import com.institucion.aula_virtual.model.Evaluacion;
import com.institucion.aula_virtual.model.Opcion;
import com.institucion.aula_virtual.model.Pregunta;
import com.institucion.aula_virtual.service.CursoService;
import com.institucion.aula_virtual.service.EvaluacionService;
import com.institucion.aula_virtual.service.PreguntaService;

@Controller
@RequestMapping("/profesor/evaluaciones")
@PreAuthorize("hasRole('PROFESOR') or hasRole('ADMIN')")
public class EvaluacionController {

    private final EvaluacionService evaluacionService;
    private final CursoService cursoService;
    private final PreguntaService preguntaService;

    public EvaluacionController(EvaluacionService evaluacionService, CursoService cursoService, PreguntaService preguntaService) {
        this.evaluacionService = evaluacionService;
        this.cursoService = cursoService;
        this.preguntaService = preguntaService;
    }

    // 1. Listar evaluaciones de un curso
    @GetMapping("/curso/{idCurso}")
    public String listarEvaluaciones(@PathVariable Long idCurso, Model model) {
        Curso curso = cursoService.buscarPorId(idCurso).orElse(null);
        if (curso == null) return "redirect:/profesor/dashboard";

        model.addAttribute("curso", curso);
        model.addAttribute("evaluaciones", evaluacionService.listarPorCurso(curso));
        model.addAttribute("nuevaEvaluacion", new Evaluacion());
        return "profesor/evaluaciones"; 
    }

    // 2. Guardar nueva evaluación
    @PostMapping("/guardar/{idCurso}")
    public String guardarEvaluacion(@PathVariable Long idCurso, @ModelAttribute Evaluacion evaluacion, RedirectAttributes redirectAttributes) {
        Curso curso = cursoService.buscarPorId(idCurso).orElse(null);
        if (curso != null) {
            evaluacion.setCurso(curso);
            evaluacion.setEstado(true); // Activa por defecto
            evaluacionService.guardar(evaluacion);
            redirectAttributes.addFlashAttribute("mensaje", "Evaluación creada correctamente.");
        }
        return "redirect:/profesor/evaluaciones/curso/" + idCurso;
    }

    // 3. Eliminar evaluación
    @PostMapping("/eliminar/{idCurso}/{idEvaluacion}")
    public String eliminarEvaluacion(@PathVariable Long idCurso, @PathVariable Long idEvaluacion, RedirectAttributes redirectAttributes) {
        evaluacionService.eliminar(idEvaluacion);
        redirectAttributes.addFlashAttribute("mensaje", "Evaluación eliminada.");
        return "redirect:/profesor/evaluaciones/curso/" + idCurso;
    }


     @GetMapping("/{idEv}/preguntas")
    public String verPreguntas(@PathVariable Long idEv, Model model) {
        Evaluacion evaluacion = evaluacionService.buscarPorId(idEv).orElse(null);
        if (evaluacion == null) return "redirect:/profesor/dashboard";

        model.addAttribute("evaluacion", evaluacion);
        model.addAttribute("preguntas", preguntaService.listarPorEvaluacion(evaluacion));
        model.addAttribute("curso", evaluacion.getCurso());
        return "profesor/preguntas";
    }

    @PostMapping("/{idEv}/preguntas/guardar")
    public String guardarPregunta(@PathVariable Long idEv, 
                                  @RequestParam("enunciado") String enunciado,
                                  @RequestParam("respuestaCorrecta") String correcta,
                                  @RequestParam("incorrecta1") String inc1,
                                  @RequestParam("incorrecta2") String inc2,
                                  @RequestParam("incorrecta3") String inc3,
                                  RedirectAttributes redirectAttributes) {
        
        Evaluacion evaluacion = evaluacionService.buscarPorId(idEv).orElse(null);
        
        if (evaluacion != null) {
            // 1. Creamos la pregunta
            Pregunta pregunta = new Pregunta();
            pregunta.setEnunciado(enunciado);
            pregunta.setTipo("OPCION_MULTIPLE");
            pregunta.setPuntaje(2); // Puedes ajustar el puntaje
            pregunta.setEvaluacion(evaluacion);
            preguntaService.guardarPregunta(pregunta);

            // 2. Creamos y guardamos la opción correcta
            Opcion opCorrecta = new Opcion();
            opCorrecta.setTexto(correcta);
            opCorrecta.setEsCorrecta(true);
            opCorrecta.setPregunta(pregunta);
            preguntaService.guardarOpcion(opCorrecta);

            // 3. Creamos y guardamos las opciones incorrectas
            Opcion op1 = new Opcion(); op1.setTexto(inc1); op1.setEsCorrecta(false); op1.setPregunta(pregunta);
            Opcion op2 = new Opcion(); op2.setTexto(inc2); op2.setEsCorrecta(false); op2.setPregunta(pregunta);
            Opcion op3 = new Opcion(); op3.setTexto(inc3); op3.setEsCorrecta(false); op3.setPregunta(pregunta);
            
            preguntaService.guardarOpcion(op1);
            preguntaService.guardarOpcion(op2);
            preguntaService.guardarOpcion(op3);

            redirectAttributes.addFlashAttribute("mensaje", "Pregunta agregada correctamente.");
        }
        
        return "redirect:/profesor/evaluaciones/" + idEv + "/preguntas";
    }
}