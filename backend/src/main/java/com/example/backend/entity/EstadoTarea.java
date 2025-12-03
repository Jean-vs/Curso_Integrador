package com.example.sistema_tareas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estado_tarea")
public class EstadoTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre; 

    // Constructor vacío
    public EstadoTarea() {}

    public EstadoTarea(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
