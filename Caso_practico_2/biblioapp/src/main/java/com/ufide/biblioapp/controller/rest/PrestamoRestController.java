package com.ufide.biblioapp.controller.rest;

import com.ufide.biblioapp.entity.Prestamo;
import com.ufide.biblioapp.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoRestController {

    @Autowired
    private PrestamoService prestamoService;

    @PreAuthorize("hasRole(T(com.ufide.biblioapp.enums.Rol).BIBLIOTECARIO.name())")
    @GetMapping("/atrasados")
    public ResponseEntity<List<Prestamo>> atrasados() {
        return ResponseEntity.ok(prestamoService.listarAtrasados());
    }
}
