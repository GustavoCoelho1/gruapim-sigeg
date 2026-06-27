package com.sigeg.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DashboardResponse {
    private Long totalPedidos;
    private BigDecimal faturamento;
    private Double mediaAvaliacao;
    private List<ProdutoResponse> maisVendidos;
}
