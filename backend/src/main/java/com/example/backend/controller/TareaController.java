package com.example.sistema_tareas.controller;

import com.example.sistema_tareas.model.*;
import com.example.sistema_tareas.repository.TareaRepository;
import com.example.sistema_tareas.service.*;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;



import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private EstadoTareaService estadoTareaService;

    @Autowired
    private TareaRepository tareaRepository;

    // 🔹 Mostrar tareas
    @GetMapping
    public String listarTareas(Model model, Principal principal) {

        if (principal != null) {

            String correo = principal.getName(); // 🔥 ahora correo
            model.addAttribute("nombreUsuario", correo);

            usuarioService.buscarPorCorreo(correo).ifPresent(usuario -> {
                model.addAttribute("tareas", tareaService.listarTareasPorUsuario(usuario.getNombreUsuario()));
            });

        } else {
            model.addAttribute("tareas", tareaService.listarTareas());
        }

        List<Equipo> equiposLibres = equipoService.listarEquipos()
                .stream()
                .filter(e -> "Libre".equalsIgnoreCase(e.getEstado()))
                .collect(Collectors.toList());

        model.addAttribute("equipos", equiposLibres);
        model.addAttribute("servicios", servicioService.listarServicios());
        model.addAttribute("tarea", new Tarea());

        return "tareas";
    }

    // 🔹 Guardar nueva tarea
    @PostMapping("/guardar")
    public String guardarTarea(@ModelAttribute("tarea") Tarea tarea,
            @RequestParam("equipoId") Long equipoId,
            @RequestParam("servicioId") Long servicioId,
            Principal principal) {

        // 🟢 ASIGNAR USUARIO AUTENTICADO POR CORREO
        if (principal != null) {
            usuarioService.buscarPorCorreo(principal.getName())
                    .ifPresentOrElse(
                            tarea::setUsuario,
                            () -> {
                                throw new RuntimeException("Error: No se encontró el usuario autenticado.");
                            });
        }

        // 🟢 Asociar equipo
        equipoService.obtenerPorId(equipoId).ifPresent(equipo -> {
            equipo.setEstado("Ocupado");
            equipoService.guardar(equipo);
            tarea.setEquipo(equipo);
        });

        // 🟢 Asociar servicio
        servicioService.obtenerPorId(servicioId).ifPresent(tarea::setServicio);

        // 🟢 Calcular estado automático
        LocalDateTime ahora = LocalDateTime.now();
        String estadoInicial;

        if (tarea.getFechaInicio() == null || tarea.getFechaFin() == null) {
            estadoInicial = "Pendiente";
        } else if (ahora.isBefore(tarea.getFechaInicio())) {
            estadoInicial = "Pendiente";
        } else if (!ahora.isAfter(tarea.getFechaFin())) {
            estadoInicial = "En progreso";
        } else {
            estadoInicial = "Completada";
        }

        EstadoTarea estado = estadoTareaService.obtenerPorNombre(estadoInicial)
                .orElseGet(() -> estadoTareaService.crearEstadoSiNoExiste(estadoInicial));
        tarea.setEstado(estado);

        tareaService.guardarTarea(tarea);
        return "redirect:/tareas";
    }

    // 🔹 Editar tarea
    @GetMapping("/editar/{id}")
    public String editarTarea(@PathVariable Long id, Model model, Principal principal) {
        Tarea tarea = tareaService.obtenerPorId(id);

        if (tarea != null && principal != null &&
                tarea.getUsuario() != null &&
                tarea.getUsuario().getCorreo().equals(principal.getName())) {

            model.addAttribute("nombreUsuario", principal.getName());
            model.addAttribute("tarea", tarea);

            usuarioService.buscarPorCorreo(principal.getName()).ifPresent(usuario -> model.addAttribute("tareas",
                    tareaService.listarTareasPorUsuario(usuario.getNombreUsuario())));

            List<Equipo> equiposDisponibles = equipoService.listarEquipos()
                    .stream()
                    .filter(e -> "Libre".equalsIgnoreCase(e.getEstado())
                            || (tarea.getEquipo() != null && e.getId().equals(tarea.getEquipo().getId())))
                    .collect(Collectors.toList());

            model.addAttribute("equipos", equiposDisponibles);
            model.addAttribute("servicios", servicioService.listarServicios());

            return "tareas";
        }
        return "redirect:/tareas";
    }

    // 🔹 Eliminar tarea
    @GetMapping("/eliminar/{id}")
    public String eliminarTarea(@PathVariable Long id, Principal principal) {
        Tarea tarea = tareaService.obtenerPorId(id);

        if (tarea != null && principal != null &&
                tarea.getUsuario() != null &&
                tarea.getUsuario().getCorreo().equals(principal.getName())) {

            Equipo equipo = tarea.getEquipo();
            if (equipo != null) {
                equipo.setEstado("Libre");
                equipoService.guardar(equipo);
            }

            tareaService.eliminarTarea(id);
        }

        return "redirect:/tareas";
    }

    // 🔹 Recargar tabla
    @GetMapping("/tabla")
    public String obtenerTablaTareas(Model model, Principal principal) {

        if (principal != null) {
            usuarioService.buscarPorCorreo(principal.getName()).ifPresent(usuario -> model.addAttribute("tareas",
                    tareaService.listarTareasPorUsuario(usuario.getNombreUsuario())));
        } else {
            model.addAttribute("tareas", tareaService.listarTareas());
        }

        return "fragments/tabla-tareas :: tablaTareas";
    }

    @GetMapping("/filtrar")
    @ResponseBody
    public List<Tarea> filtrar(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long servicioId,
            @RequestParam(required = false) Long equipoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            Principal principal) {

        String correoUsuario = principal.getName();

        List<Tarea> tareas = tareaRepository.filtrarTareas(
                texto, estado, servicioId, equipoId, desde, hasta, correoUsuario);

        // Evita proxys cargando datos mínimos necesarios
        tareas.forEach(t -> {
            t.getEstado().getNombre();
            t.getServicio().getNombreServicio();
            t.getEquipo().getNombre();
            if (t.getUsuario() != null) {
                t.getUsuario().getNombreUsuario();
            }
        });

        return tareas;
    }

    @GetMapping("/exportar/excel")
    public void exportarExcel(
            HttpServletResponse response,
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long servicioId,
            @RequestParam(required = false) Long equipoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @RequestParam(required = false) String usuarioCorreo) throws IOException {

        List<Tarea> lista = tareaService.filtrarTareas(
                texto, estado, servicioId, equipoId, desde, hasta, usuarioCorreo);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=tareas.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Tareas");

        // Formato fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // === Estilo header ===
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // === Estilo filas ===
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        int rowNum = 0;
        Row header = sheet.createRow(rowNum++);

        String[] columnas = { "ID", "Nombre", "Estado", "Servicio", "Equipo", "Fecha Inicio", "Fecha Fin" };

        for (int i = 0; i < columnas.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(headerStyle);
        }

        for (Tarea t : lista) {
            Row fila = sheet.createRow(rowNum++);

            String fechaInicio = t.getFechaInicio().format(formatter);
            String fechaFin = t.getFechaFin().format(formatter);

            String[] datos = {
                    t.getId().toString(),
                    t.getNombre(),
                    t.getEstado().getNombre(),
                    t.getServicio().getNombreServicio(),
                    t.getEquipo().getNombre(),
                    fechaInicio,
                    fechaFin
            };

            for (int i = 0; i < datos.length; i++) {
                Cell cell = fila.createCell(i);
                cell.setCellValue(datos[i]);
                cell.setCellStyle(cellStyle);
            }
        }

        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/exportar/pdf")
    public void exportarPDF(
            HttpServletResponse response,
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long servicioId,
            @RequestParam(required = false) Long equipoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @RequestParam(required = false) String usuarioCorreo) throws IOException {

        List<Tarea> lista = tareaService.filtrarTareas(
                texto, estado, servicioId, equipoId, desde, hasta, usuarioCorreo);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=tareas.pdf");

        // Formato fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Document pdf = new Document();
        PdfWriter.getInstance(pdf, response.getOutputStream());
        pdf.open();

        // ==== TÍTULO ====
        Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph titulo = new Paragraph("Reporte de Tareas", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        pdf.add(titulo);

        // ==== TABLA ====
        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);

        String[] columnas = { "ID", "Nombre", "Estado", "Servicio", "Equipo", "Fecha Inicio", "Fecha Fin" };

        // ==== HEADER ====
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);

        for (String col : columnas) {
            PdfPCell celda = new PdfPCell(new Paragraph(col, headerFont));
            celda.setBackgroundColor(new Color(30, 136, 229)); // azul fuerte
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setPadding(8);
            tabla.addCell(celda);
        }

        // ==== FILAS ====
        for (Tarea t : lista) {

            tabla.addCell(t.getId().toString());
            tabla.addCell(t.getNombre());
            tabla.addCell(t.getEstado().getNombre());
            tabla.addCell(t.getServicio().getNombreServicio());
            tabla.addCell(t.getEquipo().getNombre());
            tabla.addCell(t.getFechaInicio().format(formatter));
            tabla.addCell(t.getFechaFin().format(formatter));
        }

        pdf.add(tabla);
        pdf.close();
    }

    // 🔹 Mostrar detalle de una tarea
    @GetMapping("/detalle/{id}")
    public String verDetalleTarea(@PathVariable Long id, Model model, Principal principal) {

        Tarea tarea = tareaService.obtenerPorId(id);

        if (tarea == null) {
            return "redirect:/tareas";
        }

        // Agregar la tarea
        model.addAttribute("tarea", tarea);

        // Agregar información del equipo
        Equipo equipo = tarea.getEquipo();
        if (equipo != null) {
            model.addAttribute("equipo", equipo);
            model.addAttribute("lider", equipo.getLider());
            model.addAttribute("empleados", equipo.getEmpleados());
        }

        // Agregar información del servicio
        model.addAttribute("servicio", tarea.getServicio());

        // 🔥🔥 AGREGAR ID DEL USUARIO AUTENTICADO
        if (principal != null) {
            usuarioService.buscarPorCorreo(principal.getName())
                    .ifPresent(usuario -> model.addAttribute("idUsuario", usuario.getId()));
        }

        return "tarea_detalle"; // 👈 tu vista HTML
    }

}
