package com.sigeg.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AvaliacaoResponse {
    private UUID id;
    private UUID pedidoId;
    private UUID restauranteId;
    private UUID entregadorId;
    private Integer nota;
    private String comentario;
    private LocalDateTime criadoEm;
}
