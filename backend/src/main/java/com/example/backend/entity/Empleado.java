package com.example.sistema_tareas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombres;
    private String apellidos;
    private String dni;
    private String telefono;
    private String direccion;

    @Column(length = 20, nullable = false)
    private String rolEmpleado; 

    @OneToOne(mappedBy = "empleado")
    private Usuario usuario;

    public Empleado() {
    }

    public Empleado(String nombres, String apellidos, String dni, String telefono, String direccion,
                    String rolEmpleado) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rolEmpleado = rolEmpleado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getRolEmpleado() { return rolEmpleado; }
    public void setRolEmpleado(String rolEmpleado) { this.rolEmpleado = rolEmpleado; }
}
