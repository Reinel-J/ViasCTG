package com.viactg.dto;

import com.viactg.model.PrioridadReporte;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReporteActualizarRequest(
        @NotBlank @Size(min = 10, max = 2000) String descripcion,
        @Size(max = 500) String fotoUrl,
        @NotNull Double latitud,
        @NotNull Double longitud,
        @NotNull PrioridadReporte prioridad
) {
}
