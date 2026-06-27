package com.sigeg.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class AvaliacaoRequest {
    @NotNull
    private UUID pedidoId;

    @NotNull @Min(1) @Max(5)
    private Integer nota;

    private String comentario;
}
