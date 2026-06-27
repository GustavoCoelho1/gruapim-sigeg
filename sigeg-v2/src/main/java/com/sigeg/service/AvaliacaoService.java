package com.sigeg.service;

import com.sigeg.dto.request.AvaliacaoRequest;
import com.sigeg.dto.response.AvaliacaoResponse;
import java.util.List;
import java.util.UUID;

public interface AvaliacaoService {
    AvaliacaoResponse avaliar(AvaliacaoRequest request, String emailCliente);
    List<AvaliacaoResponse> listarPorRestaurante(UUID restauranteId);
    Double mediaRestaurante(UUID restauranteId);
}
