package com.sigeg.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class EnderecoRequest {
    @NotBlank
    private String logradouro;

    private String numero;
    private String bairro;
    private String cidade;
    private String cep;
    private String complemento;
    private String apelido;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean principal = false;
}
