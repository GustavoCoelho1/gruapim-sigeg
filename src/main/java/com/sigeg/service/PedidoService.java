package com.sigeg.service;

import com.sigeg.domain.enums.StatusPedido;
import com.sigeg.dto.request.PedidoRequest;
import com.sigeg.dto.response.PedidoResponse;
import java.util.List;
import java.util.UUID;

public interface PedidoService {
    PedidoResponse criar(PedidoRequest request, String emailCliente);
    PedidoResponse buscarPorId(UUID id);
    List<PedidoResponse> listarPorCliente(String emailCliente);
    List<PedidoResponse> listarPorRestaurante(UUID restauranteId, String emailUsuario);
    PedidoResponse atualizarStatus(UUID id, StatusPedido status, String emailUsuario);
}
