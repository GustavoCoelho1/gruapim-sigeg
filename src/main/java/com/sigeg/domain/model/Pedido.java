package com.sigeg.domain.model;

import com.sigeg.domain.enums.MetodoPagamento;
import com.sigeg.domain.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Pedido – entidade central do SIGEG.
 * Tabela: pedidos – conforme populate.sql do grupo.
 *
 * Sprint 3 – Épico 1  – Realizar Pedidos.
 * Sprint 4 – Épico 7  – Gestão de Pedidos (status, painel restaurante).
 * Sprint 5 – Épico 3  – Rastreamento (via Entrega).
 * Sprint 6 – Épico 4  – Avaliação (pedidos com status ENTREGUE).
 * Sprint 8 – Épico 8  – Painel Analítico (faturamento, pedidos por período).
 */
@Entity
@Table(
    name = "pedidos",
    indexes = {
        @Index(name = "idx_pedidos_cliente_id", columnList = "cliente_id"),
        @Index(name = "idx_pedidos_restaurante_id", columnList = "restaurante_id"),
        @Index(name = "idx_pedidos_status", columnList = "status"),
        @Index(name = "idx_pedidos_criado_em", columnList = "criado_em")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    /**
     * Endereço de entrega selecionado pelo cliente no ato do pedido.
     * Snapshot via FK – o endereço pode ser alterado pelo cliente depois.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "endereco_entrega_id", nullable = false)
    private Endereco enderecoEntrega;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusPedido status = StatusPedido.RECEBIDO;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento", nullable = false, length = 20)
    private MetodoPagamento metodoPagamento;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "taxa_entrega", nullable = false, precision = 8, scale = 2)
    private BigDecimal taxaEntrega = BigDecimal.ZERO;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    /** Itens do pedido (linhas do carrinho confirmado). */
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    /**
     * Entrega associada ao pedido (Épico 3).
     * Criada quando o restaurante confirma a saída para entrega.
     */
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Entrega entrega;

    /** Avaliação do pedido (Épico 4 – apenas status ENTREGUE). */
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Avaliacao avaliacao;

    /** Chamado de suporte relacionado ao pedido (Épico 5 – opcional). */
    @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY)
    private List<Chamado> chamados = new ArrayList<>();
}
