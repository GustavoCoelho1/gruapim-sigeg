package com.sigeg.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PedidoResponse {
    private UUID id;
    private UUID clienteId;
    private String nomeCliente;
    private UUID restauranteId;
    private String nomeRestaurante;
    private String status;
    private String metodoPagamento;
    private BigDecimal subtotal;
    private BigDecimal taxaEntrega;
    private BigDecimal total;
    private String observacoes;
    private List<ItemPedidoResponse> itens;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
