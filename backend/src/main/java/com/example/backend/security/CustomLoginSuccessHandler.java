package com.example.sistema_tareas.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String rol = authentication.getAuthorities().iterator().next().getAuthority();

        if (rol.equals("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else if (rol.equals("ROLE_LIDER")) {
            response.sendRedirect("/lider/tareas");
        } else {
            response.sendRedirect("/login?error=rol_no_valido");
        }
    }
}