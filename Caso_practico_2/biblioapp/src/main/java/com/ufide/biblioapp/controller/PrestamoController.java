package com.ufide.biblioapp.controller;

import com.ufide.biblioapp.entity.Usuario;
import com.ufide.biblioapp.service.LibroService;
import com.ufide.biblioapp.service.PrestamoService;
import com.ufide.biblioapp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private UsuarioService usuarioService;

    @PreAuthorize("hasRole(T(com.ufide.biblioapp.enums.Rol).BIBLIOTECARIO.name())")
    @GetMapping("/prestamos")
    public String listarTodos(Model model) {
        model.addAttribute("prestamos", prestamoService.listarTodos());
        model.addAttribute("titulo", "Todos los prestamos");
        return "prestamos/lista";
    }

    @GetMapping("/mis-prestamos")
    public String misPrestamos(Authentication auth, Model model) {
        Usuario usuario = usuarioService.buscarPorUsername(auth.getName());
        model.addAttribute("prestamos", prestamoService.listarPorUsuario(usuario));
        model.addAttribute("titulo", "Mis prestamos");
        return "prestamos/lista";
    }

    @PreAuthorize("hasRole(T(com.ufide.biblioapp.enums.Rol).BIBLIOTECARIO.name())")
    @GetMapping("/prestamos/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("libros", libroService.listar());
        model.addAttribute("usuarios", usuarioService.listarLectores());
        return "prestamos/form";
    }

    @PreAuthorize("hasRole(T(com.ufide.biblioapp.enums.Rol).BIBLIOTECARIO.name())")
    @PostMapping("/prestamos/guardar")
    public String guardar(@RequestParam Long libroId,
                          @RequestParam Long usuarioId,
                          RedirectAttributes redirect) {
        try {
            prestamoService.registrar(libroId, usuarioId);
            redirect.addFlashAttribute("mensaje", "Prestamo registrado correctamente");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/prestamos/nuevo";
        }

        return "redirect:/prestamos";
    }

    @PreAuthorize("hasRole(T(com.ufide.biblioapp.enums.Rol).BIBLIOTECARIO.name())")
    @PostMapping("/prestamos/devolver/{id}")
    public String devolver(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            prestamoService.devolver(id);
            redirect.addFlashAttribute("mensaje", "Devolucion registrada correctamente");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/prestamos";
    }

    @PreAuthorize("hasRole(T(com.ufide.biblioapp.enums.Rol).BIBLIOTECARIO.name())")
    @GetMapping("/prestamos/atrasados")
    public String atrasados(Model model) {
        model.addAttribute("prestamos", prestamoService.listarAtrasados());
        return "prestamos/atrasados";
    }
}
