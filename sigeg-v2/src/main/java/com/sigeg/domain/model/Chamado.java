package com.sigeg.domain.model;

import com.sigeg.domain.enums.CategoriaChamado;
import com.sigeg.domain.enums.StatusChamado;
import jakarta.persistence.*;
import lombok.*;

/**
 * Chamado de suporte aberto por um usuário.
 * Tabela: chamados – conforme populate.sql do grupo.
 *
 * Épico 5 – Suporte ao Cliente:
 *   - Usuário abre chamado e informa a categoria.
 *   - Chamado fica registrado com status rastreável.
 *   - MVP: abertura simples de chamados.
 */
@Entity
@Table(
    name = "chamados",
    indexes = {
        @Index(name = "idx_chamados_usuario_id", columnList = "usuario_id"),
        @Index(name = "idx_chamados_pedido_id", columnList = "pedido_id"),
        @Index(name = "idx_chamados_status", columnList = "status")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chamado extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Pedido relacionado ao chamado (opcional). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 50)
    private CategoriaChamado categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusChamado status = StatusChamado.ABERTO;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "resposta", columnDefinition = "TEXT")
    private String resposta;
}
