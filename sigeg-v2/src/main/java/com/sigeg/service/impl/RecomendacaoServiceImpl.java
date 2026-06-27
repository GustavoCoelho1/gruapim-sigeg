package com.sigeg.service.impl;

import com.sigeg.domain.model.Pedido;
import com.sigeg.domain.model.Produto;
import com.sigeg.domain.model.Restaurante;
import com.sigeg.dto.response.RecomendacaoResponse;
import com.sigeg.dto.response.RestauranteResponse;
import com.sigeg.dto.response.ProdutoResponse;
import com.sigeg.exceptions.ResourceNotFoundException;
import com.sigeg.repository.ClienteRepository;
import com.sigeg.repository.PedidoRepository;
import com.sigeg.service.RecomendacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecomendacaoServiceImpl implements RecomendacaoService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;

    @Override
    @Transactional(readOnly = true)
    public RecomendacaoResponse recomendar(String emailCliente) {
        var cliente = clienteRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        List<Pedido> historico = pedidoRepository.historicoCliente(cliente.getId());

        Map<Restaurante, Long> freqRestaurante = historico.stream()
                .collect(Collectors.groupingBy(Pedido::getRestaurante, Collectors.counting()));

        List<RestauranteResponse> restaurantesFreq = freqRestaurante.entrySet().stream()
                .sorted(Map.Entry.<Restaurante, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> {
                    Restaurante r = e.getKey();
                    return RestauranteResponse.builder()
                            .id(r.getId()).nomeFantasia(r.getNomeFantasia())
                            .descricao(r.getDescricao()).urlImagem(r.getUrlImagem())
                            .logradouro(r.getLogradouro()).cidade(r.getCidade())
                            .taxaEntrega(r.getTaxaEntrega()).tempoEstimadoMin(r.getTempoEstimadoMin())
                            .ativo(r.isAtivo()).build();
                }).collect(Collectors.toList());

        Map<Produto, Long> freqProduto = historico.stream()
                .flatMap(p -> p.getItens().stream())
                .collect(Collectors.groupingBy(i -> i.getProduto(), Collectors.summingLong(i -> i.getQuantidade())));

        List<ProdutoResponse> produtosFav = freqProduto.entrySet().stream()
                .sorted(Map.Entry.<Produto, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> {
                    Produto p = e.getKey();
                    return ProdutoResponse.builder()
                            .id(p.getId()).restauranteId(p.getRestaurante().getId())
                            .nome(p.getNome()).descricao(p.getDescricao())
                            .preco(p.getPreco()).urlImagem(p.getUrlImagem())
                            .categoria(p.getCategoria()).disponivel(p.isDisponivel())
                            .totalVendido(p.getTotalVendido()).build();
                }).collect(Collectors.toList());

        return RecomendacaoResponse.builder()
                .restaurantesFrequentes(restaurantesFreq)
                .produtosFavoritos(produtosFav)
                .build();
    }
}
