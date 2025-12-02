package com.example.sistema_tareas.controller;

import com.example.sistema_tareas.model.Usuario;
import com.example.sistema_tareas.service.UsuarioService;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String nombreUsuario,
            @RequestParam String password,
            Model model) {

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorUsuario(nombreUsuario);

        // Si no lo encuentra por nombreUsuario, buscar por correo
        if (usuarioOpt.isEmpty()) {
            usuarioOpt = usuarioService.buscarPorCorreo(nombreUsuario);
        }

        if (usuarioOpt.isEmpty()) {
            model.addAttribute("error", "Usuario o correo no encontrado");
            return "login";
        }

        Usuario usuario = usuarioOpt.get();

        // Validar contraseña
        if (!usuarioService.passwordCoincide(password, usuario.getPassword())) {
            model.addAttribute("error", "Contraseña incorrecta");
            return "login";
        }

        // Redirección por rol
        if (usuario.getRol().equalsIgnoreCase("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        if (usuario.getRol().equalsIgnoreCase("LIDER")) {
            return "redirect:/lider/dashboard";
        }

        model.addAttribute("error", "Rol no válido");
        return "login";
    }

    // Vista del dashboard admin
    @GetMapping("/admin/dashboard")
    public String mostrarDashboard(Model model) {
        return "dashboard";
    }

    // Vista del dashboard para líderes
    @GetMapping("/lider/dashboard")
    public String mostrarDashboardLider(Model model) {
        return "dashboard_lider"; // crea este HTML luego
    }

}
