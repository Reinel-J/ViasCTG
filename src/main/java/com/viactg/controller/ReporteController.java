package com.viactg.controller;

import com.viactg.dto.ReporteActualizarRequest;
import com.viactg.dto.ReporteCrearRequest;
import com.viactg.dto.ReporteResponse;
import com.viactg.mapper.ReporteMapper;
import com.viactg.model.EstadoReporte;
import com.viactg.security.UsuarioPrincipal;
import com.viactg.service.ReporteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
    private final ReporteService reporteService;
    private final ReporteMapper reporteMapper;
    public ReporteController(ReporteService reporteService, ReporteMapper reporteMapper) { this.reporteService = reporteService; this.reporteMapper = reporteMapper; }
    @GetMapping public List<ReporteResponse> listar(@RequestParam(required = false) EstadoReporte estado) { return reporteService.listar(estado).stream().map(reporteMapper::toResponse).toList(); }
    @GetMapping("/{id}") public ReporteResponse obtener(@PathVariable String id) { return reporteMapper.toResponse(reporteService.buscarPorId(id)); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public ReporteResponse crear(@AuthenticationPrincipal UsuarioPrincipal principal, @Valid @RequestBody ReporteCrearRequest request) { return reporteMapper.toResponse(reporteService.crear(request, principal.getId())); }
    @PutMapping("/{id}")
    public ReporteResponse actualizar(@PathVariable String id, @AuthenticationPrincipal UsuarioPrincipal principal, @Valid @RequestBody ReporteActualizarRequest request) { return reporteMapper.toResponse(reporteService.actualizar(id, request, principal.getId())); }
}
