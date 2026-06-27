package com.sigeg.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ChamadoResponse {
    private UUID id;
    private UUID usuarioId;
    private String nomeUsuario;
    private UUID pedidoId;
    private String categoria;
    private String status;
    private String titulo;
    private String descricao;
    private String resposta;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
