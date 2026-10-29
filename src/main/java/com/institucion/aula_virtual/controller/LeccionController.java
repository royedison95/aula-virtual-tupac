package com.institucion.aula_virtual.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
import com.institucion.aula_virtual.model.Leccion;
import com.institucion.aula_virtual.model.Matricula;
import com.institucion.aula_virtual.model.Progreso;
import com.institucion.aula_virtual.model.Usuario;
import com.institucion.aula_virtual.repository.MatriculaRepository; 
import com.institucion.aula_virtual.service.CursoService;
import com.institucion.aula_virtual.service.LeccionService;
import com.institucion.aula_virtual.service.ProgresoService;


@Controller
@RequestMapping("/lecciones")
public class LeccionController {

    private final LeccionService leccionService;
    private final CursoService cursoService;
     private final MatriculaRepository matriculaRepository; 
     private final ProgresoService progresoService;

    public LeccionController(LeccionService leccionService, CursoService cursoService, MatriculaRepository matriculaRepository, ProgresoService progresoService) {
        this.leccionService = leccionService;
        this.cursoService = cursoService;
        this.matriculaRepository = matriculaRepository;
        this.progresoService = progresoService;
    }

    // 1. Reproductor del Estudiante (Ver Lecciones de un Curso)
    @GetMapping("/curso/{idCurso}")
    public String verLecciones(@PathVariable Long idCurso, 
                               @RequestParam(name = "leccionId", required = false) Long leccionId,
                               Authentication auth,
                               Model model, RedirectAttributes redirectAttributes) {
        
        Curso curso = cursoService.buscarPorId(idCurso).orElse(null);
        
        if (curso == null) {
            return "redirect:/estudiante/mis-cursos";
        }

        // Obtenemos al estudiante logueado para que no falle el HTML
        Usuario usuario = (Usuario) auth.getPrincipal();
        model.addAttribute("usuarioEstudiante", usuario);

        // =========================================================
        // VALIDACIÓN DE SEGURIDAD: ¿Está matriculado en este curso?
        // =========================================================
        List<Matricula> misMatriculas = matriculaRepository.findByEstudiante(usuario);
        boolean estaMatriculado = misMatriculas.stream()
                .anyMatch(m -> m.getCurso().getId().equals(curso.getId()));

         if (!estaMatriculado) {
            // Lo enviamos a "Mis Cursos" con un mensaje en la URL
            return "redirect:/estudiante/mis-cursos?errorAcceso=true";
        }
        // =========================================================
        model.addAttribute("usuarioEstudiante", usuario);

        List<Leccion> lecciones = leccionService.listarPorCurso(curso);
        model.addAttribute("curso", curso);
        model.addAttribute("lecciones", lecciones);
        
        // Lógica para mostrar la lección seleccionada o la primera por defecto
        Leccion leccionActual = null;
        if (lecciones != null && !lecciones.isEmpty()) {
            if (leccionId != null) {
                // Buscamos la lección seleccionada
                for (Leccion l : lecciones) {
                    if (l.getId().equals(leccionId)) {
                        leccionActual = l;
                        break;
                    }
                }
                // Si no la encuentra, muestra la primera
                if (leccionActual == null) leccionActual = lecciones.get(0);
            } else {
                leccionActual = lecciones.get(0);
            }
        }

        model.addAttribute("leccionActual", leccionActual);

         // =========================================================
        // CALCULAR PROGRESO DEL ESTUDIANTE
        // =========================================================
        Progreso progreso = progresoService.obtenerProgreso(usuario, curso).orElse(new Progreso());
        
        if (progreso.getEstudiante() == null) {
            progreso.setEstudiante(usuario);
            progreso.setCurso(curso);
            progreso.setLeccionesCompletadas(0);
             progreso.setTotalLecciones(lecciones != null ? lecciones.size() : 0); 
            progreso.setPorcentajeAvance(0.0);
            progresoService.guardar(progreso);
        }
        
        model.addAttribute("progreso", progreso);
        return "lecciones/reproductor"; 
    }

    // 2. Panel del Profesor (Crear Lecciones)
    @GetMapping("/profesor/curso/{idCurso}")
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMIN')")
    public String gestionarLecciones(@PathVariable Long idCurso, Model model) {
        Curso curso = cursoService.buscarPorId(idCurso).orElse(null);
        model.addAttribute("curso", curso);
        model.addAttribute("lecciones", leccionService.listarPorCurso(curso));
        model.addAttribute("nuevaLeccion", new Leccion());
        return "lecciones/gestion"; // Una vista para que el profe suba material
    }

    // 3. Guardar la Lección creada por el Profesor
    @PostMapping("/guardar/{idCurso}")
    @PreAuthorize("hasRole('PROFESOR') or hasRole('ADMIN')")
    public String guardarLeccion(@PathVariable Long idCurso, @ModelAttribute Leccion leccion, RedirectAttributes redirectAttributes) {
        Curso curso = cursoService.buscarPorId(idCurso).orElse(null);
        if (curso != null) {
            leccion.setCurso(curso);
            leccionService.guardar(leccion);
            redirectAttributes.addFlashAttribute("mensaje", "Lección creada correctamente.");
        }
        return "redirect:/lecciones/profesor/curso/" + idCurso;
    }

        // 4. Marcar Lección como Completada y actualizar Progreso
    @PostMapping("/completar/{idCurso}/{idLeccion}")
    public String marcarCompletada(@PathVariable Long idCurso, @PathVariable("idLeccion") Long idLeccion, Authentication auth, RedirectAttributes redirectAttributes) {
        Curso curso = cursoService.buscarPorId(idCurso).orElse(null);
        if (curso != null) {
            Usuario usuario = (Usuario) auth.getPrincipal();

            // 1. MARCAR LA LECCIÓN ESPECÍFICA COMO COMPLETADA
            Leccion leccion = leccionService.buscarPorId(idLeccion).orElse(null);
            if (leccion != null) {
                leccion.setContenido("COMPLETADA"); // Usamos este campo como bandera
                leccionService.guardar(leccion);
            }
            Progreso progreso = progresoService.obtenerProgreso(usuario, curso).orElse(new Progreso());
            
            // Si es un progreso nuevo, configuramos los datos base
            if (progreso.getEstudiante() == null) {
                progreso.setEstudiante(usuario);
                progreso.setCurso(curso);
                progreso.setTotalLecciones(leccionService.listarPorCurso(curso).size());
            }
                        // 1. Obtenemos los valores, si son nulos usamos 0
            Integer c = progreso.getLeccionesCompletadas();
            Integer t = progreso.getTotalLecciones();
            
            // 2. Asignamos valores seguros
            int completadas = (c != null) ? c : 0;
            int total = (t != null && t > 0) ? t : 1;

            // 3. Sumamos
            if (completadas < total) {
                completadas = completadas + 1;
            }

            // Calculamos el porcentaje (Regla de 3)
            double porcentaje = ((double) completadas / total) * 100;
            
            progreso.setLeccionesCompletadas(completadas);
            progreso.setPorcentajeAvance(porcentaje);
            progreso.setCompletado(completadas == total); // Si completó todo, true

            progresoService.guardar(progreso);
            redirectAttributes.addFlashAttribute("mensaje", "¡Lección marcada como completada!");
        }
        return "redirect:/lecciones/curso/" + idCurso;
    }


}