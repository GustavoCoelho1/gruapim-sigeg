package com.sigeg.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class EnderecoResponse {
    private UUID id;
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String cep;
    private String complemento;
    private String apelido;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private boolean principal;
}
