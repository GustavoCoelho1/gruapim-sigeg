package com.sigeg.service;

import com.sigeg.dto.request.RestauranteRequest;
import com.sigeg.dto.response.RestauranteResponse;
import java.util.List;
import java.util.UUID;

public interface RestauranteService {
    RestauranteResponse criar(RestauranteRequest request, String emailUsuario);
    RestauranteResponse atualizar(UUID id, RestauranteRequest request, String emailUsuario);
    RestauranteResponse buscarPorId(UUID id);
    List<RestauranteResponse> listarAtivos();
    List<RestauranteResponse> buscar(String query);
}
