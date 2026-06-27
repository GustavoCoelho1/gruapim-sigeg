package com.sigeg.service.impl;

import com.sigeg.domain.model.Restaurante;
import com.sigeg.domain.model.Usuario;
import com.sigeg.dto.request.RestauranteRequest;
import com.sigeg.dto.response.RestauranteResponse;
import com.sigeg.exceptions.BusinessException;
import com.sigeg.exceptions.ResourceNotFoundException;
import com.sigeg.repository.RestauranteRepository;
import com.sigeg.repository.UsuarioRepository;
import com.sigeg.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestauranteServiceImpl implements RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public RestauranteResponse criar(RestauranteRequest request, String emailUsuario) {
        Usuario usuario = findUsuario(emailUsuario);
        if (restauranteRepository.findByUsuarioId(usuario.getId()).isPresent()) {
            throw new BusinessException("Este usuário já possui um restaurante cadastrado.");
        }
        Restaurante r = toEntity(request, new Restaurante());
        r.setUsuario(usuario);
        return toResponse(restauranteRepository.save(r));
    }

    @Override
    @Transactional
    public RestauranteResponse atualizar(UUID id, RestauranteRequest request, String emailUsuario) {
        Restaurante r = findById(id);
        validarProprietario(r, emailUsuario);
        toEntity(request, r);
        return toResponse(restauranteRepository.save(r));
    }

    @Override
    @Transactional(readOnly = true)
    public RestauranteResponse buscarPorId(UUID id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponse> listarAtivos() {
        return restauranteRepository.findByAtivoTrue().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponse> buscar(String query) {
        return restauranteRepository.search(query).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    // helpers
    private Restaurante findById(UUID id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante", id));
    }

    private Usuario findUsuario(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + email));
    }

    private void validarProprietario(Restaurante r, String email) {
        if (!r.getUsuario().getEmail().equals(email)) {
            throw new BusinessException("Você não tem permissão para alterar este restaurante.");
        }
    }

    private Restaurante toEntity(RestauranteRequest req, Restaurante r) {
        r.setNomeFantasia(req.getNomeFantasia());
        r.setDescricao(req.getDescricao());
        r.setUrlImagem(req.getUrlImagem());
        r.setLogradouro(req.getLogradouro());
        r.setNumero(req.getNumero());
        r.setBairro(req.getBairro());
        r.setCidade(req.getCidade());
        r.setCep(req.getCep());
        r.setLatitude(req.getLatitude());
        r.setLongitude(req.getLongitude());
        if (req.getTaxaEntrega() != null) r.setTaxaEntrega(req.getTaxaEntrega());
        r.setTempoEstimadoMin(req.getTempoEstimadoMin());
        return r;
    }

    private RestauranteResponse toResponse(Restaurante r) {
        return RestauranteResponse.builder()
                .id(r.getId())
                .nomeFantasia(r.getNomeFantasia())
                .descricao(r.getDescricao())
                .urlImagem(r.getUrlImagem())
                .logradouro(r.getLogradouro())
                .numero(r.getNumero())
                .bairro(r.getBairro())
                .cidade(r.getCidade())
                .cep(r.getCep())
                .latitude(r.getLatitude())
                .longitude(r.getLongitude())
                .taxaEntrega(r.getTaxaEntrega())
                .tempoEstimadoMin(r.getTempoEstimadoMin())
                .ativo(r.isAtivo())
                .criadoEm(r.getCriadoEm())
                .build();
    }
}
