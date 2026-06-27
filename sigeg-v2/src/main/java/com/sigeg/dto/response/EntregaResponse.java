package com.sigeg.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EntregaResponse {
    private UUID id;
    private UUID pedidoId;
    private UUID entregadorId;
    private String nomeEntregador;
    private BigDecimal latAtual;
    private BigDecimal lngAtual;
    private BigDecimal distanciaEstimadaKm;
    private Integer tempoEstimadoMin;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
