package com.ufide.biblioapp.repository;

import com.ufide.biblioapp.entity.Prestamo;
import com.ufide.biblioapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuario(Usuario usuario);

    @Query("SELECT p FROM Prestamo p JOIN FETCH p.libro JOIN FETCH p.usuario ORDER BY p.fechaPrestamo DESC")
    List<Prestamo> listarConDatos();

    @Query("SELECT p FROM Prestamo p JOIN FETCH p.libro JOIN FETCH p.usuario " +
           "WHERE p.fechaDevolucion IS NULL AND p.fechaLimite < CURRENT_DATE " +
           "ORDER BY p.fechaLimite ASC")
    List<Prestamo> prestamosAtrasados();
}
