package com.ufide.eventapp.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Entidad Evento - representa un evento publico (concierto, taller, charla...).
 */
@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Digite el nombre")
    @Size(max = 120, message = "Nombre muy largo")
    @Column(nullable = false, length = 120)
    private String nombre;

    @Size(max = 500, message = "Descripcion muy larga")
    @Column(length = 500)
    private String descripcion;

    /** Fecha del evento (sin hora). */
    @NotNull(message = "Digite la fecha")
    @FutureOrPresent(message = "La fecha no puede ser pasada")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotBlank(message = "Digite el lugar")
    @Size(max = 100, message = "Lugar muy largo")
    @Column(length = 100)
    private String lugar;

    /** Categoria libre: "Musica", "Conferencia", "Deporte", "Taller", etc. */
    @NotBlank(message = "Digite la categoria")
    @Size(max = 50, message = "Categoria muy larga")
    @Column(length = 50)
    private String categoria;

    @NotBlank(message = "Digite el organizador")
    @Size(max = 80, message = "Organizador muy largo")
    @Column(length = 80)
    private String organizador;

    /** Cupo total disponible. */
    @Min(value = 1, message = "El cupo debe ser mayor a 0")
    private int cupoMaximo;

    /** Tickets ya vendidos. */
    @PositiveOrZero(message = "No puede ser negativo")
    private int cuposVendidos;

    /** Precio de la entrada (0 si es gratis). */
    @PositiveOrZero(message = "No puede ser negativo")
    private double precio;

    public Evento() {}

    public Evento(String nombre, String descripcion, LocalDate fecha, String lugar,
                  String categoria, String organizador,
                  int cupoMaximo, int cuposVendidos, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.lugar = lugar;
        this.categoria = categoria;
        this.organizador = organizador;
        this.cupoMaximo = cupoMaximo;
        this.cuposVendidos = cuposVendidos;
        this.precio = precio;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getOrganizador() { return organizador; }
    public void setOrganizador(String organizador) { this.organizador = organizador; }

    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }

    public int getCuposVendidos() { return cuposVendidos; }
    public void setCuposVendidos(int cuposVendidos) { this.cuposVendidos = cuposVendidos; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}
