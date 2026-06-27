package com.sigeg.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Produto do cardápio de um Restaurante.
 * Tabela: produtos – conforme populate.sql do grupo.
 *
 * Sprint 2 – Cadastro de Produtos.
 * Épico 6  – CRUD de produtos, upload de imagem, definição de preços.
 * Épico 2  – total_vendido alimenta recomendações.
 * Épico 8  – produtos mais vendidos no painel analítico.
 */
@Entity
@Table(
    name = "produtos",
    indexes = {
        @Index(name = "idx_produtos_restaurante_id", columnList = "restaurante_id"),
        @Index(name = "idx_produtos_disponivel", columnList = "disponivel")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    /** URL da imagem (Épico 6). */
    @Column(name = "url_imagem", length = 500)
    private String urlImagem;

    @Column(name = "categoria", length = 100)
    private String categoria; // Ex: Entradas, Pratos Principais, Bebidas

    @Column(name = "disponivel", nullable = false)
    private boolean disponivel = true;

    /**
     * Contador de vendas do produto.
     * Utilizado nas recomendações (Épico 2) e no painel analítico (Épico 8).
     */
    @Column(name = "total_vendido", nullable = false)
    private Long totalVendido = 0L;
}
