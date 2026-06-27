package com.sigeg.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Item de um Pedido (linha do carrinho confirmado).
 * Tabela: itens_pedido – conforme populate.sql do grupo.
 * Inclui constraint CHECK (quantidade > 0) replicada via validação Jakarta.
 *
 * Épico 1 – o cliente adiciona itens ao carrinho.
 * O preço unitário é snapshot do momento da compra.
 */
@Entity
@Table(
    name = "itens_pedido",
    indexes = {
        @Index(name = "idx_itens_pedido_id", columnList = "pedido_id"),
        @Index(name = "idx_itens_produto_id", columnList = "produto_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedido extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    /**
     * Snapshot do preço no momento da compra.
     * Protege o histórico contra alterações de preço posteriores.
     */
    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "observacao", length = 300)
    private String observacao; // Ex: "sem cebola"

    /** Calcula e retorna o subtotal da linha. */
    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        if (precoUnitario != null && quantidade != null) {
            this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        }
    }
}
