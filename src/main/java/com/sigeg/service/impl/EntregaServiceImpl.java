package com.sigeg.service.impl;

import com.sigeg.domain.enums.StatusPedido;
import com.sigeg.domain.model.*;
import com.sigeg.dto.request.LocalizacaoRequest;
import com.sigeg.dto.response.EntregaResponse;
import com.sigeg.exceptions.BusinessException;
import com.sigeg.exceptions.ResourceNotFoundException;
import com.sigeg.repository.*;
import com.sigeg.service.EntregaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EntregaServiceImpl implements EntregaService {

    private final EntregaRepository entregaRepository;
    private final PedidoRepository pedidoRepository;
    private final EntregadorRepository entregadorRepository;
    private final LocalizacaoEntregadorRepository localizacaoRepository;

    @Override
    @Transactional
    public EntregaResponse atribuirEntregador(UUID pedidoId, UUID entregadorId, String emailUsuario) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", pedidoId));

        if (pedido.getStatus() != StatusPedido.EM_PREPARO && pedido.getStatus() != StatusPedido.SAIU_PARA_ENTREGA) {
            throw new BusinessException("Pedido não está em condição de ser entregue.");
        }

        if (entregaRepository.findByPedidoId(pedidoId).isPresent()) {
            throw new BusinessException("Entrega já atribuída a este pedido.");
        }

        Entregador entregador = entregadorRepository.findById(entregadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Entregador", entregadorId));

        Entrega entrega = new Entrega();
        entrega.setPedido(pedido);
        entrega.setEntregador(entregador);

        // cálculo simples de distância via Haversine MVP
        if (pedido.getRestaurante().getLatitude() != null && pedido.getEnderecoEntrega().getLatitude() != null) {
            double dist = calcularDistancia(
                    pedido.getRestaurante().getLatitude().doubleValue(),
                    pedido.getRestaurante().getLongitude().doubleValue(),
                    pedido.getEnderecoEntrega().getLatitude().doubleValue(),
                    pedido.getEnderecoEntrega().getLongitude().doubleValue()
            );
            entrega.setDistanciaEstimadaKm(BigDecimal.valueOf(dist).setScale(2, RoundingMode.HALF_UP));
            entrega.setTempoEstimadoMin((int) Math.ceil(dist / 0.5)); // ~30 km/h
        }

        return toResponse(entregaRepository.save(entrega));
    }

    @Override
    @Transactional
    public EntregaResponse atualizarLocalizacao(UUID entregaId, LocalizacaoRequest request, String emailEntregador) {
        Entrega entrega = entregaRepository.findById(entregaId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega", entregaId));

        entrega.setLatAtual(request.getLatitude());
        entrega.setLngAtual(request.getLongitude());

        LocalizacaoEntregador loc = new LocalizacaoEntregador();
        loc.setEntrega(entrega);
        loc.setLatitude(request.getLatitude());
        loc.setLongitude(request.getLongitude());
        localizacaoRepository.save(loc);

        // recalcula ETA se o destino tiver coordenadas
        if (entrega.getPedido().getEnderecoEntrega().getLatitude() != null) {
            double dist = calcularDistancia(
                    request.getLatitude().doubleValue(),
                    request.getLongitude().doubleValue(),
                    entrega.getPedido().getEnderecoEntrega().getLatitude().doubleValue(),
                    entrega.getPedido().getEnderecoEntrega().getLongitude().doubleValue()
            );
            entrega.setTempoEstimadoMin((int) Math.ceil(dist / 0.5));
        }

        return toResponse(entregaRepository.save(entrega));
    }

    @Override
    @Transactional(readOnly = true)
    public EntregaResponse buscarPorPedido(UUID pedidoId) {
        Entrega entrega = entregaRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega para o pedido " + pedidoId + " não encontrada."));
        return toResponse(entrega);
    }

    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private EntregaResponse toResponse(Entrega e) {
        return EntregaResponse.builder()
                .id(e.getId())
                .pedidoId(e.getPedido().getId())
                .entregadorId(e.getEntregador().getId())
                .nomeEntregador(e.getEntregador().getNome())
                .latAtual(e.getLatAtual())
                .lngAtual(e.getLngAtual())
                .distanciaEstimadaKm(e.getDistanciaEstimadaKm())
                .tempoEstimadoMin(e.getTempoEstimadoMin())
                .criadoEm(e.getCriadoEm())
                .atualizadoEm(e.getAtualizadoEm())
                .build();
    }
}
