package com.example.sistema_tareas.service;

import com.example.sistema_tareas.model.Usuario;
import com.example.sistema_tareas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registrarUsuario(Usuario usuario) {
        System.out.println("🟢 Registrando usuario: " + usuario.getNombreUsuario()); // <-- cambia a nombreUsuario
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol("ADMIN");
        usuarioRepository.save(usuario);
        System.out.println("✅ Usuario guardado correctamente");
    }

    public Optional<Usuario> buscarPorUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public boolean passwordCoincide(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}