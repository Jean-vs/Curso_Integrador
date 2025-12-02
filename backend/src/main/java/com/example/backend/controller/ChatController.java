package com.example.sistema_tareas.controller;

import com.example.sistema_tareas.model.Mensaje;
import com.example.sistema_tareas.model.Usuario;
import com.example.sistema_tareas.service.MensajeService;
import com.example.sistema_tareas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/enviarMensaje")
    public void procesarMensaje(Map<String, Object> data) {

        Long tareaId = Long.valueOf(data.get("tareaId").toString());
        Long usuarioId = Long.valueOf(data.get("usuarioId").toString());
        String contenido = data.get("contenido").toString();

        // Buscar usuario real
        Usuario usuario = usuarioService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Preparar mensaje para guardar en BD
        Mensaje msg = new Mensaje();
        msg.setTareaId(tareaId);
        msg.setContenido(contenido);
        msg.setUsuario(usuario);
        msg.setFechaHora(LocalDateTime.now());

        Mensaje guardado = mensajeService.guardar(msg);

        // Preparar respuesta para WebSocket
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("tareaId", tareaId);
        respuesta.put("contenido", contenido);
        String rol = usuario.getRol().equals("ADMIN") ? "ADMIN" : "LIDER";
        respuesta.put("emisor", rol);
        respuesta.put("fechaHora", guardado.getFechaHora().toString());

        // Enviar el mensaje al topic de la tarea específica
        messagingTemplate.convertAndSend(
                "/topic/tarea/" + tareaId,
                respuesta);
    }

     @GetMapping("/tarea/{tareaId}/mensajes")
    @ResponseBody
    public List<Map<String, Object>> obtenerMensajes(@PathVariable Long tareaId) {

        List<Mensaje> mensajes = mensajeService.listarPorTarea(tareaId);
        List<Map<String, Object>> respuesta = new ArrayList<>();

        for (Mensaje m : mensajes) {

            String rol = m.getUsuario().getRol().toUpperCase().contains("ADMIN")
                    ? "ADMIN"
                    : "LIDER";

            Map<String, Object> item = new HashMap<>();
            item.put("contenido", m.getContenido());
            item.put("emisor", rol);
            item.put("fechaHora", m.getFechaHora().toString());

            respuesta.add(item);
        }

        return respuesta;
    }
}
