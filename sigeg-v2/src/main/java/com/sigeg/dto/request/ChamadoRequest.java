package com.sigeg.dto.request;

import com.sigeg.domain.enums.CategoriaChamado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class ChamadoRequest {
    @NotNull
    private CategoriaChamado categoria;

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;

    private UUID pedidoId;
}
