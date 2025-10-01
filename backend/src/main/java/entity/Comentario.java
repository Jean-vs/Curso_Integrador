package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComentario;

    private String contenido;
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "tarea_id")
    private Tarea tarea;
}