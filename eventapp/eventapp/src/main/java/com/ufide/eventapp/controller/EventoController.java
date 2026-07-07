package com.ufide.eventapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ufide.eventapp.entity.Evento;
import com.ufide.eventapp.service.EventoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("eventos", service.listar());
        return "eventos";
    }

    @GetMapping("/categoria/{categoria}")
    public String filtrarPorCategoria(@PathVariable String categoria, Model model) {
        model.addAttribute("eventos", service.buscarPorCategoria(categoria));
        model.addAttribute("filtro", "Categoria: " + categoria);
        return "eventos";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Evento evento = service.buscarPorId(id).orElse(null);
        model.addAttribute("evento", evento);
        return "evento";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("evento", new Evento());
        cargarForm(model, "Nuevo evento", "Guardar");
        return "eventos/form";
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute("evento") Evento evento,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            cargarForm(model, "Nuevo evento", "Guardar");
            return "eventos/form";
        }

        service.guardar(evento);
        return "redirect:/eventos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Evento evento = service.buscarPorId(id).orElse(null);

        if (evento == null) {
            return "redirect:/eventos";
        }

        model.addAttribute("evento", evento);
        cargarForm(model, "Editar evento", "Actualizar");
        return "eventos/form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("evento") Evento evento,
                             BindingResult result,
                             Model model) {
        evento.setId(id);

        if (result.hasErrors()) {
            cargarForm(model, "Editar evento", "Actualizar");
            return "eventos/form";
        }

        service.guardar(evento);
        return "redirect:/eventos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "redirect:/eventos";
    }

    private void cargarForm(Model model, String titulo, String boton) {
        model.addAttribute("titulo", titulo);
        model.addAttribute("textoBoton", boton);
    }
}
