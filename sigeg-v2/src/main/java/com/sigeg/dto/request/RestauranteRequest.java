package com.sigeg.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RestauranteRequest {
    @NotBlank @Size(max = 150)
    private String nomeFantasia;

    @Size(max = 500)
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
}
