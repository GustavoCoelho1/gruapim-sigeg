package com.sigeg.service.impl;

import com.sigeg.domain.model.Produto;
import com.sigeg.domain.model.Restaurante;
import com.sigeg.dto.request.ProdutoRequest;
import com.sigeg.dto.response.ProdutoResponse;
import com.sigeg.exceptions.BusinessException;
import com.sigeg.exceptions.ResourceNotFoundException;
import com.sigeg.repository.ProdutoRepository;
import com.sigeg.repository.RestauranteRepository;
import com.sigeg.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final RestauranteRepository restauranteRepository;

    @Override
    @Transactional
    public ProdutoResponse criar(UUID restauranteId, ProdutoRequest request, String emailUsuario) {
        Restaurante r = findRestaurante(restauranteId);
        validarProprietario(r, emailUsuario);
        Produto p = toEntity(request, new Produto());
        p.setRestaurante(r);
        return toResponse(produtoRepository.save(p));
    }

    @Override
    @Transactional
    public ProdutoResponse atualizar(UUID id, ProdutoRequest request, String emailUsuario) {
        Produto p = findById(id);
        validarProprietario(p.getRestaurante(), emailUsuario);
        toEntity(request, p);
        return toResponse(produtoRepository.save(p));
    }

    @Override
    @Transactional
    public void deletar(UUID id, String emailUsuario) {
        Produto p = findById(id);
        validarProprietario(p.getRestaurante(), emailUsuario);
        p.setDisponivel(false);
        produtoRepository.save(p);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarPorRestaurante(UUID restauranteId) {
        return produtoRepository.findByRestauranteId(restauranteId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(UUID id) {
        return toResponse(findById(id));
    }

    private Produto findById(UUID id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", id));
    }

    private Restaurante findRestaurante(UUID id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante", id));
    }

    private void validarProprietario(Restaurante r, String email) {
        if (!r.getUsuario().getEmail().equals(email)) {
            throw new BusinessException("Sem permissão para gerenciar produtos deste restaurante.");
        }
    }

    private Produto toEntity(ProdutoRequest req, Produto p) {
        p.setNome(req.getNome());
        p.setDescricao(req.getDescricao());
        p.setPreco(req.getPreco());
        p.setUrlImagem(req.getUrlImagem());
        p.setCategoria(req.getCategoria());
        if (req.getDisponivel() != null) p.setDisponivel(req.getDisponivel());
        return p;
    }

    public ProdutoResponse toResponse(Produto p) {
        return ProdutoResponse.builder()
                .id(p.getId())
                .restauranteId(p.getRestaurante().getId())
                .nome(p.getNome())
                .descricao(p.getDescricao())
                .preco(p.getPreco())
                .urlImagem(p.getUrlImagem())
                .categoria(p.getCategoria())
                .disponivel(p.isDisponivel())
                .totalVendido(p.getTotalVendido())
                .criadoEm(p.getCriadoEm())
                .build();
    }
}
