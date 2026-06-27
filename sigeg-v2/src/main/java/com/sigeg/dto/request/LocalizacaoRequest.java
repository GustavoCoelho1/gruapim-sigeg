package com.sigeg.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class LocalizacaoRequest {
    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;
}
