package com.ufide.biblioapp.service;

import com.ufide.biblioapp.entity.Libro;
import com.ufide.biblioapp.entity.Prestamo;
import com.ufide.biblioapp.entity.Usuario;
import com.ufide.biblioapp.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private LibroService libroService;

    @Autowired
    private UsuarioService usuarioService;

    public List<Prestamo> listarTodos() {
        return prestamoRepository.listarConDatos();
    }

    public List<Prestamo> listarPorUsuario(Usuario usuario) {
        return prestamoRepository.findByUsuario(usuario);
    }

    public List<Prestamo> listarAtrasados() {
        return prestamoRepository.prestamosAtrasados();
    }

    @Transactional
    public Prestamo registrar(Long libroId, Long usuarioId) {
        Libro libro = libroService.buscarPorId(libroId).orElse(null);
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        if (libro == null || usuario == null) {
            throw new IllegalArgumentException("Libro o usuario no encontrado");
        }

        if (libro.getCopiasDisponibles() == null || libro.getCopiasDisponibles() <= 0) {
            throw new IllegalArgumentException("No hay copias disponibles");
        }

        LocalDate hoy = LocalDate.now();

        Prestamo prestamo = new Prestamo();
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);
        prestamo.setFechaPrestamo(hoy);
        prestamo.setFechaLimite(hoy.plusDays(14));
        prestamo.setFechaDevolucion(null);

        Prestamo guardado = prestamoRepository.save(prestamo);
        libroService.descontarCopia(libro);

        return guardado;
    }

    @Transactional
    public void devolver(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);

        if (prestamo == null) {
            throw new IllegalArgumentException("Prestamo no encontrado");
        }

        if (prestamo.getFechaDevolucion() != null) {
            throw new IllegalArgumentException("El libro ya fue devuelto");
        }

        prestamo.setFechaDevolucion(LocalDate.now());
        prestamoRepository.save(prestamo);
        libroService.devolverCopia(prestamo.getLibro());
    }
}
