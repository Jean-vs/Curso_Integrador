package com.example.sistema_tareas.repository;

import com.example.sistema_tareas.model.Empleado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    boolean existsByDni(String dni);

    List<Empleado> findByRolEmpleado(String rolEmpleado);

    Empleado findByDni(String dni);

}


