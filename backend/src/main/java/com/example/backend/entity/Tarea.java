package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTarea;

    private String titulo;
    private String descripcion;
    private Date fechaCreacion;
    private Date fechaLimite;

    @Enumerated(EnumType.STRING)
    private EstadoTarea estado;   

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios;
}