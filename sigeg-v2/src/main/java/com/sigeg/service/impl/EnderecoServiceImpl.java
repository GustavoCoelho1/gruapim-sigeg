package com.sigeg.service.impl;

import com.sigeg.domain.model.Endereco;
import com.sigeg.domain.model.Usuario;
import com.sigeg.dto.request.EnderecoRequest;
import com.sigeg.dto.response.EnderecoResponse;
import com.sigeg.exceptions.BusinessException;
import com.sigeg.exceptions.ResourceNotFoundException;
import com.sigeg.repository.EnderecoRepository;
import com.sigeg.repository.UsuarioRepository;
import com.sigeg.service.EnderecoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnderecoServiceImpl implements EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public EnderecoResponse criar(EnderecoRequest request, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Endereco endereco = Endereco.builder()
                .usuario(usuario)
                .logradouro(request.getLogradouro())
                .numero(request.getNumero())
                .bairro(request.getBairro())
                .cidade(request.getCidade())
                .cep(request.getCep())
                .complemento(request.getComplemento())
                .apelido(request.getApelido())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .principal(Boolean.TRUE.equals(request.getPrincipal()))
                .build();

        return toResponse(enderecoRepository.save(endereco));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoResponse> listarMeus(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
        return enderecoRepository.findByUsuarioId(usuario.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletar(UUID id, String emailUsuario) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço", id));
        if (!endereco.getUsuario().getEmail().equals(emailUsuario)) {
            throw new BusinessException("Sem permissão para excluir este endereço.");
        }
        enderecoRepository.delete(endereco);
    }

    private EnderecoResponse toResponse(Endereco e) {
        return EnderecoResponse.builder()
                .id(e.getId())
                .logradouro(e.getLogradouro())
                .numero(e.getNumero())
                .bairro(e.getBairro())
                .cidade(e.getCidade())
                .cep(e.getCep())
                .complemento(e.getComplemento())
                .apelido(e.getApelido())
                .latitude(e.getLatitude())
                .longitude(e.getLongitude())
                .principal(e.isPrincipal())
                .build();
    }
}
