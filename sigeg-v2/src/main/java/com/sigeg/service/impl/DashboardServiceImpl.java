package com.sigeg.service.impl;

import com.sigeg.dto.response.DashboardResponse;
import com.sigeg.exceptions.ResourceNotFoundException;
import com.sigeg.repository.AvaliacaoRepository;
import com.sigeg.repository.PedidoRepository;
import com.sigeg.repository.ProdutoRepository;
import com.sigeg.repository.RestauranteRepository;
import com.sigeg.service.DashboardService;
import com.sigeg.service.impl.ProdutoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final RestauranteRepository restauranteRepository;
    private final ProdutoServiceImpl produtoService;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getDashboard(UUID restauranteId, LocalDateTime inicio, LocalDateTime fim, String emailUsuario) {
        restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante", restauranteId));

        LocalDateTime ini = inicio != null ? inicio : LocalDateTime.now().minusDays(30);
        LocalDateTime f = fim != null ? fim : LocalDateTime.now();

        Long total = pedidoRepository.countByRestauranteAndPeriodo(restauranteId, ini, f);
        var faturamento = pedidoRepository.faturamentoByRestauranteAndPeriodo(restauranteId, ini, f);
        Double media = avaliacaoRepository.mediaNotaRestaurante(restauranteId);

        var maisVendidos = produtoRepository.findByRestauranteIdOrderByTotalVendidoDesc(restauranteId)
                .stream().limit(5).map(produtoService::toResponse).collect(Collectors.toList());

        return DashboardResponse.builder()
                .totalPedidos(total)
                .faturamento(faturamento)
                .mediaAvaliacao(media)
                .maisVendidos(maisVendidos)
                .build();
    }
}
