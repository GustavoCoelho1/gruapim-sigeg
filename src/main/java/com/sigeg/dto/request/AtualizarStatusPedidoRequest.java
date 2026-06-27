package com.sigeg.dto.request;

import com.sigeg.domain.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtualizarStatusPedidoRequest {
    @NotNull
    private StatusPedido status;
}
