package com.sigeg.service;

import com.sigeg.dto.request.LocalizacaoRequest;
import com.sigeg.dto.response.EntregaResponse;
import java.util.UUID;

public interface EntregaService {
    EntregaResponse atribuirEntregador(UUID pedidoId, UUID entregadorId, String emailUsuario);
    EntregaResponse atualizarLocalizacao(UUID entregaId, LocalizacaoRequest request, String emailEntregador);
    EntregaResponse buscarPorPedido(UUID pedidoId);
}
