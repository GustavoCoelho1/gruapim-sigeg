package com.sigeg.domain.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Avaliação de um Pedido concluído.
 * Tabela: avaliacoes – conforme populate.sql do grupo.
 *
 * Épico 4 – Avaliação de Pedidos:
 *   - Disponível apenas para pedidos com status ENTREGUE.
 *   - Nota de 1 a 5 estrelas (CHECK constraint no banco).
 *   - Campo comentario opcional.
 *   - Uma avaliação por pedido (pedido_id UNIQUE).
 */
@Entity
@Table(
    name = "avaliacoes",
    indexes = {
        @Index(name = "idx_avaliacoes_pedido_id", columnList = "pedido_id"),
        @Index(name = "idx_avaliacoes_restaurante_id", columnList = "restaurante_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avaliacao extends BaseEntity {

    /** Pedido avaliado – único por avaliação. */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entregador_id")
    private Entregador entregador;

    /**
     * Nota de 1 a 5.
     * Validação de range também no banco via CHECK constraint (populate.sql).
     */
    @Column(name = "nota", nullable = false)
    private Integer nota;

    /** Comentário opcional do cliente. */
    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;
}
