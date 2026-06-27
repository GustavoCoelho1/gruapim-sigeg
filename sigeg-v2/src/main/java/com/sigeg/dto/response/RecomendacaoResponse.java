package com.sigeg.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class RecomendacaoResponse {
    private List<RestauranteResponse> restaurantesFrequentes;
    private List<ProdutoResponse> produtosFavoritos;
}
