package com.sigeg.service.impl;

import com.sigeg.domain.enums.StatusPedido;
import com.sigeg.domain.model.*;
import com.sigeg.dto.request.PedidoRequest;
import com.sigeg.dto.response.ItemPedidoResponse;
import com.sigeg.dto.response.PedidoResponse;
import com.sigeg.exceptions.BusinessException;
import com.sigeg.exceptions.ResourceNotFoundException;
import com.sigeg.repository.*;
import com.sigeg.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final RestauranteRepository restauranteRepository;
    private final ProdutoRepository produtoRepository;
    private final EnderecoRepository enderecoRepository;

    @Override
    @Transactional
    public PedidoResponse criar(PedidoRequest request, String emailCliente) {
        Cliente cliente = clienteRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        Restaurante restaurante = restauranteRepository.findById(request.getRestauranteId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante", request.getRestauranteId()));

        if (!restaurante.isAtivo()) throw new BusinessException("Restaurante indisponível.");

        Endereco endereco = enderecoRepository.findById(request.getEnderecoEntregaId())
                .orElseThrow(() -> new ResourceNotFoundException("Endereço", request.getEnderecoEntregaId()));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setEnderecoEntrega(endereco);
        pedido.setMetodoPagamento(request.getMetodoPagamento());
        pedido.setObservacoes(request.getObservacoes());
        pedido.setStatus(StatusPedido.RECEBIDO);
        pedido.setTaxaEntrega(restaurante.getTaxaEntrega() != null ? restaurante.getTaxaEntrega() : BigDecimal.ZERO);

        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (PedidoRequest.ItemPedidoRequest itemReq : request.getItens()) {
            Produto produto = produtoRepository.findById(itemReq.getProdutoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto", itemReq.getProdutoId()));

            if (!produto.getRestaurante().getId().equals(restaurante.getId())) {
                throw new BusinessException("Produto " + produto.getNome() + " não pertence ao restaurante selecionado.");
            }
            if (!produto.isDisponivel()) throw new BusinessException("Produto indisponível: " + produto.getNome());

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemReq.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(itemReq.getQuantidade())));
            item.setObservacao(itemReq.getObservacao());
            itens.add(item);
            subtotal = subtotal.add(item.getSubtotal());

            produto.setTotalVendido(produto.getTotalVendido() + itemReq.getQuantidade());
            produtoRepository.save(produto);
        }

        pedido.setItens(itens);
        pedido.setSubtotal(subtotal);
        pedido.setTotal(subtotal.add(pedido.getTaxaEntrega()));

        return toResponse(pedidoRepository.save(pedido));
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(UUID id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponse> listarPorCliente(String emailCliente) {
        Cliente cliente = clienteRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
        return pedidoRepository.findByClienteIdOrderByCriadoEmDesc(cliente.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponse> listarPorRestaurante(UUID restauranteId, String emailUsuario) {
        return pedidoRepository.findByRestauranteIdOrderByCriadoEmDesc(restauranteId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PedidoResponse atualizarStatus(UUID id, StatusPedido novoStatus, String emailUsuario) {
        Pedido pedido = findById(id);
        validarTransicao(pedido.getStatus(), novoStatus);
        pedido.setStatus(novoStatus);
        return toResponse(pedidoRepository.save(pedido));
    }

    private Pedido findById(UUID id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
    }

    private void validarTransicao(StatusPedido atual, StatusPedido novo) {
        boolean valido = switch (atual) {
            case RECEBIDO -> novo == StatusPedido.EM_PREPARO || novo == StatusPedido.CANCELADO;
            case EM_PREPARO -> novo == StatusPedido.SAIU_PARA_ENTREGA || novo == StatusPedido.CANCELADO;
            case SAIU_PARA_ENTREGA -> novo == StatusPedido.ENTREGUE;
            default -> false;
        };
        if (!valido) throw new BusinessException("Transição de status inválida: " + atual + " -> " + novo);
    }

    public PedidoResponse toResponse(Pedido p) {
        List<ItemPedidoResponse> itensResp = p.getItens() == null ? List.of() :
                p.getItens().stream().map(i -> ItemPedidoResponse.builder()
                        .id(i.getId())
                        .produtoId(i.getProduto().getId())
                        .nomeProduto(i.getProduto().getNome())
                        .quantidade(i.getQuantidade())
                        .precoUnitario(i.getPrecoUnitario())
                        .subtotal(i.getSubtotal())
                        .observacao(i.getObservacao())
                        .build()).collect(Collectors.toList());

        return PedidoResponse.builder()
                .id(p.getId())
                .clienteId(p.getCliente().getId())
                .nomeCliente(p.getCliente().getNome())
                .restauranteId(p.getRestaurante().getId())
                .nomeRestaurante(p.getRestaurante().getNomeFantasia())
                .status(p.getStatus().name())
                .metodoPagamento(p.getMetodoPagamento().name())
                .subtotal(p.getSubtotal())
                .taxaEntrega(p.getTaxaEntrega())
                .total(p.getTotal())
                .observacoes(p.getObservacoes())
                .itens(itensResp)
                .criadoEm(p.getCriadoEm())
                .atualizadoEm(p.getAtualizadoEm())
                .build();
    }
}
