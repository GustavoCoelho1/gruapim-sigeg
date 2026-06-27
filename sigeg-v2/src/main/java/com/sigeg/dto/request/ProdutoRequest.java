package com.sigeg.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoRequest {
    @NotBlank
    private String nome;

    private String descricao;

    @NotNull @DecimalMin("0.01")
    private BigDecimal preco;

    private String urlImagem;
    private String categoria;
    private Boolean disponivel = true;
}
