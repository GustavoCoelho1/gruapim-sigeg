package com.sigeg.service;

import com.sigeg.domain.enums.StatusChamado;
import com.sigeg.dto.request.ChamadoRequest;
import com.sigeg.dto.response.ChamadoResponse;
import java.util.List;
import java.util.UUID;

public interface ChamadoService {
    ChamadoResponse abrir(ChamadoRequest request, String emailUsuario);
    List<ChamadoResponse> listarMeus(String emailUsuario);
    List<ChamadoResponse> listarTodos();
    ChamadoResponse atualizarStatus(UUID id, StatusChamado status, String resposta);
}
