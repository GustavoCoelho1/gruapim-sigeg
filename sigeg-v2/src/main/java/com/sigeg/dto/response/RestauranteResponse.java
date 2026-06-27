package com.sigeg.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class RestauranteResponse {
    private UUID id;
    private String nomeFantasia;
    private String descricao;
    private String urlImagem;
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String cep;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal taxaEntrega;
    private Integer tempoEstimadoMin;
    private boolean ativo;
    private LocalDateTime criadoEm;
}
