package com.sigeg.dto.request;

import com.sigeg.domain.enums.MetodoPagamento;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class PedidoRequest {
    @NotNull
    private UUID restauranteId;

    @NotNull
    private UUID enderecoEntregaId;

    @NotNull
    private MetodoPagamento metodoPagamento;

    @NotEmpty
    private List<ItemPedidoRequest> itens;

    private String observacoes;

    @Data
    public static class ItemPedidoRequest {
        @NotNull
        private UUID produtoId;

        @NotNull
        private Integer quantidade;

        private String observacao;
    }
}
