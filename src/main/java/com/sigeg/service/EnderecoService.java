package com.sigeg.service;

import com.sigeg.dto.request.EnderecoRequest;
import com.sigeg.dto.response.EnderecoResponse;
import java.util.List;
import java.util.UUID;

public interface EnderecoService {
    EnderecoResponse criar(EnderecoRequest request, String emailUsuario);
    List<EnderecoResponse> listarMeus(String emailUsuario);
    void deletar(UUID id, String emailUsuario);
}
