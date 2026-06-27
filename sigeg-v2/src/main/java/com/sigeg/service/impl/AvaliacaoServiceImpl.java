package com.sigeg.service.impl;

import com.sigeg.domain.enums.StatusPedido;
import com.sigeg.domain.model.Avaliacao;
import com.sigeg.domain.model.Cliente;
import com.sigeg.domain.model.Pedido;
import com.sigeg.dto.request.AvaliacaoRequest;
import com.sigeg.dto.response.AvaliacaoResponse;
import com.sigeg.exceptions.BusinessException;
import com.sigeg.exceptions.ResourceNotFoundException;
import com.sigeg.repository.AvaliacaoRepository;
import com.sigeg.repository.ClienteRepository;
import com.sigeg.repository.PedidoRepository;
import com.sigeg.service.AvaliacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliacaoServiceImpl implements AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public AvaliacaoResponse avaliar(AvaliacaoRequest request, String emailCliente) {
        Cliente cliente = clienteRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        Pedido pedido = pedidoRepository.findById(request.getPedidoId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", request.getPedidoId()));

        if (!pedido.getCliente().getId().equals(cliente.getId())) {
            throw new BusinessException("Este pedido não pertence ao usuário logado.");
        }
        if (pedido.getStatus() != StatusPedido.ENTREGUE) {
            throw new BusinessException("Só é possível avaliar pedidos com status ENTREGUE.");
        }
        if (avaliacaoRepository.findByPedidoId(pedido.getId()).isPresent()) {
            throw new BusinessException("Este pedido já foi avaliado.");
        }

        Avaliacao avaliacao = Avaliacao.builder()
                .pedido(pedido)
                .restaurante(pedido.getRestaurante())
                .entregador(pedido.getEntrega() != null ? pedido.getEntrega().getEntregador() : null)
                .nota(request.getNota())
                .comentario(request.getComentario())
                .build();

        return toResponse(avaliacaoRepository.save(avaliacao));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvaliacaoResponse> listarPorRestaurante(UUID restauranteId) {
        return avaliacaoRepository.findByRestauranteId(restauranteId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double mediaRestaurante(UUID restauranteId) {
        return avaliacaoRepository.mediaNotaRestaurante(restauranteId);
    }

    private AvaliacaoResponse toResponse(Avaliacao a) {
        return AvaliacaoResponse.builder()
                .id(a.getId())
                .pedidoId(a.getPedido().getId())
                .restauranteId(a.getRestaurante().getId())
                .entregadorId(a.getEntregador() != null ? a.getEntregador().getId() : null)
                .nota(a.getNota())
                .comentario(a.getComentario())
                .criadoEm(a.getCriadoEm())
                .build();
    }
}
