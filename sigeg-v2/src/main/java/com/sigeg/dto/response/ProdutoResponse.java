package com.sigeg.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ProdutoResponse {
    private UUID id;
    private UUID restauranteId;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String urlImagem;
    private String categoria;
    private boolean disponivel;
    private Long totalVendido;
    private LocalDateTime criadoEm;
}
