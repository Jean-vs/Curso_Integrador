package com.example.backend.entity;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "equipo")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String lider;

    @Column(nullable = false)
    private String estado; 

    @Column(nullable = false)
    private int participantes;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Tarea> tareas;

    public Equipo() {}

    public Equipo(String nombre, String lider, String estado, int participantes) {
        this.nombre = nombre;
        this.lider = lider;
        this.estado = estado;
        this.participantes = participantes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getLider() { return lider; }
    public void setLider(String lider) { this.lider = lider; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getParticipantes() { return participantes; }
    public void setParticipantes(int participantes) { this.participantes = participantes; }

    public List<Tarea> getTareas() { return tareas; }
    public void setTareas(List<Tarea> tareas) { this.tareas = tareas; }
}
