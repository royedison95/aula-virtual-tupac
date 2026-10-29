package com.institucion.aula_virtual.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.institucion.aula_virtual.model.Evaluacion;
import com.institucion.aula_virtual.model.EvaluacionEstudiante;
import com.institucion.aula_virtual.model.Matricula;
import com.institucion.aula_virtual.model.Opcion;
import com.institucion.aula_virtual.model.Pregunta;
import com.institucion.aula_virtual.model.Usuario;
import com.institucion.aula_virtual.repository.EvaluacionEstudianteRepository;
import com.institucion.aula_virtual.repository.EvaluacionRepository;
import com.institucion.aula_virtual.repository.MatriculaRepository;
import com.institucion.aula_virtual.repository.OpcionRepository;
import com.institucion.aula_virtual.repository.PreguntaRepository;

@Controller
@RequestMapping("/estudiante/evaluaciones")
public class EvaluacionEstudianteController {

    private final EvaluacionRepository evaluacionRepository;
    private final PreguntaRepository preguntaRepository;
    private final OpcionRepository opcionRepository;
    private final EvaluacionEstudianteRepository evalEstudianteRepository;
    private final MatriculaRepository matriculaRepository;

    public EvaluacionEstudianteController(EvaluacionRepository evaluacionRepository,
                                          PreguntaRepository preguntaRepository,
                                          OpcionRepository opcionRepository,
                                          EvaluacionEstudianteRepository evalEstudianteRepository,
                                          MatriculaRepository matriculaRepository) {
        this.evaluacionRepository = evaluacionRepository;
        this.preguntaRepository = preguntaRepository;
        this.opcionRepository = opcionRepository;
        this.evalEstudianteRepository = evalEstudianteRepository;
        this.matriculaRepository = matriculaRepository;
    }

    // 1. Ver evaluaciones disponibles de sus cursos
    @GetMapping
    public String listarEvaluaciones(Authentication auth, Model model) {
        Usuario estudiante = (Usuario) auth.getPrincipal();
        List<Matricula> matriculas = matriculaRepository.findByEstudiante(estudiante);
        
        // Obtenemos todas las evaluaciones de los cursos en los que está matriculado
        List<Evaluacion> evaluaciones = matriculas.stream()
                .flatMap(m -> evaluacionRepository.findByCurso(m.getCurso()).stream())
                .toList();

        model.addAttribute("usuarioEstudiante", estudiante);
        model.addAttribute("evaluaciones", evaluaciones);
        return "estudiante/evaluaciones";
    }

    // 2. Mostrar el examen para resolverlo
    @GetMapping("/rendir/{idEv}")
    public String rendirEvaluacion(@PathVariable Long idEv, Authentication auth, Model model) {
        Evaluacion evaluacion = evaluacionRepository.findById(idEv).orElse(null);
        Usuario estudiante = (Usuario) auth.getPrincipal();

        model.addAttribute("usuarioEstudiante", estudiante);
        model.addAttribute("evaluacion", evaluacion);
        model.addAttribute("preguntas", preguntaRepository.findByEvaluacionOrderByOrdenAsc(evaluacion));
        return "estudiante/rendir-evaluacion";
    }

    // 3. Calificar automáticamente y guardar
    @PostMapping("/calificar/{idEv}")
    public String calificar(@PathVariable Long idEv, 
                            @RequestParam Map<String, String> respuestas,
                            Authentication auth, 
                            RedirectAttributes redirectAttributes) {
        
        Evaluacion evaluacion = evaluacionRepository.findById(idEv).orElse(null);
        Usuario estudiante = (Usuario) auth.getPrincipal();
        
        List<Pregunta> preguntas = preguntaRepository.findByEvaluacionOrderByOrdenAsc(evaluacion);
        int puntajeObtenido = 0;

        // Recorremos las respuestas del alumno (los inputs se llaman resp_1, resp_2, etc)
        for (Pregunta p : preguntas) {
            String opcionElegida = respuestas.get("pregunta_" + p.getId());
            if (opcionElegida != null) {
                Opcion opcion = opcionRepository.findById(Long.valueOf(opcionElegida)).orElse(null);
                if (opcion != null && opcion.getEsCorrecta()) {
                    puntajeObtenido += p.getPuntaje();
                }
            }
        }

        // Guardamos la nota en la base de datos
        EvaluacionEstudiante registro = evalEstudianteRepository.findByEstudianteAndEvaluacion(estudiante, evaluacion);
        if (registro == null) {
            registro = new EvaluacionEstudiante();
            registro.setEstudiante(estudiante);
            registro.setEvaluacion(evaluacion);
        }
        registro.setNotaObtenida((double) puntajeObtenido);
        registro.setFechaFin(LocalDateTime.now());
        registro.setCompletado(true);
        
        evalEstudianteRepository.save(registro);

        redirectAttributes.addFlashAttribute("mensaje", "¡Examen enviado! Tu nota es: " + puntajeObtenido + " pts.");
        return "redirect:/estudiante/evaluaciones";
    }


        // 4. Ver historial de notas del estudiante
    @GetMapping("/mis-notas")
    public String verMisNotas(Authentication auth, Model model) {
        Usuario estudiante = (Usuario) auth.getPrincipal();
        
        // Buscamos todas las evaluaciones que este alumno ha rendido
        List<EvaluacionEstudiante> misNotas = evalEstudianteRepository.findByEstudiante(estudiante);

        model.addAttribute("usuarioEstudiante", estudiante);
        model.addAttribute("misNotas", misNotas);
        return "estudiante/mis-notas";
    }
}