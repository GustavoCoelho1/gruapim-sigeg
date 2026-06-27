package com.sigeg.service;

import com.sigeg.dto.request.ProdutoRequest;
import com.sigeg.dto.response.ProdutoResponse;
import java.util.List;
import java.util.UUID;

public interface ProdutoService {
    ProdutoResponse criar(UUID restauranteId, ProdutoRequest request, String emailUsuario);
    ProdutoResponse atualizar(UUID id, ProdutoRequest request, String emailUsuario);
    void deletar(UUID id, String emailUsuario);
    List<ProdutoResponse> listarPorRestaurante(UUID restauranteId);
    ProdutoResponse buscarPorId(UUID id);
}
