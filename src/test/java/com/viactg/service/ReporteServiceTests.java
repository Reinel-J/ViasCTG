package com.viactg.service;

import com.viactg.model.EstadoReporte;
import com.viactg.model.Reporte;
import com.viactg.repository.BarrioRepository;
import com.viactg.repository.CategoriaRepository;
import com.viactg.repository.ReporteRepository;
import com.viactg.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReporteServiceTests {

    @Test
    void cambiarEstadoInsertaHistorialEnLaActualizacionAtomica() {
        ReporteRepository reporteRepository = mock(ReporteRepository.class);
        UsuarioService usuarioService = mock(UsuarioService.class);
        MongoTemplate mongoTemplate = mock(MongoTemplate.class);
        Reporte reporte = Reporte.builder().id("reporte-1").estado(EstadoReporte.PENDIENTE)
                .fechaCreacion(Instant.now()).fechaActualizacion(Instant.now()).build();
        Reporte actualizado = Reporte.builder().id("reporte-1").estado(EstadoReporte.EN_REVISION)
                .historialEstados(java.util.List.of()).build();
        when(usuarioService.esAdministradorOModerador("admin-1")).thenReturn(true);
        when(reporteRepository.findById("reporte-1")).thenReturn(Optional.of(reporte));
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any(FindAndModifyOptions.class), eq(Reporte.class)))
                .thenReturn(actualizado);

        Reporte resultado = new ReporteService(reporteRepository, mock(UsuarioRepository.class), mock(BarrioRepository.class),
                mock(CategoriaRepository.class), usuarioService, mongoTemplate)
                .cambiarEstadoReporte("reporte-1", EstadoReporte.EN_REVISION, "admin-1", "En revisión");

        assertThat(resultado.getEstado()).isEqualTo(EstadoReporte.EN_REVISION);
    }
}
