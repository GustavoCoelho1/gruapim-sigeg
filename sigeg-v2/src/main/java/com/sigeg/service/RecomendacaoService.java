package com.sigeg.service;

import com.sigeg.dto.response.RecomendacaoResponse;

public interface RecomendacaoService {
    RecomendacaoResponse recomendar(String emailCliente);
}
