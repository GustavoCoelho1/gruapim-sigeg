package com.sigeg.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UsuarioResponse {
    private UUID id;
    private String nome;
    private String email;
    private String tipoPerfil;
    private boolean ativo;
    private LocalDateTime criadoEm;
}
