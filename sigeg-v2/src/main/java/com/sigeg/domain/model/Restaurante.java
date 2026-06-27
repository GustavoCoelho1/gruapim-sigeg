package com.sigeg.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Restaurante cadastrado no SIGEG.
 * Tabela: restaurantes – conforme populate.sql do grupo.
 *
 * Sprint 2 – Cadastro de restaurantes e cardápios.
 * Épico 6  – CRUD, imagem, preços.
 * Épico 7  – Gestão de pedidos pelo restaurante.
 * Épico 8  – Painel analítico.
 */
@Entity
@Table(
    name = "restaurantes",
    indexes = {
        @Index(name = "idx_restaurantes_usuario_id", columnList = "usuario_id"),
        @Index(name = "idx_restaurantes_ativo", columnList = "ativo")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurante extends BaseEntity {

    /**
     * Usuário proprietário do restaurante (1-to-1 com usuarios).
     * Alinhado ao usuario_id UNIQUE do populate.sql.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "nome_fantasia", nullable = false, length = 150)
    private String nomeFantasia;

    @Column(name = "descricao", length = 500)
    private String descricao;

    /** URL da imagem (Épico 6 – upload de imagem). */
    @Column(name = "url_imagem", length = 500)
    private String urlImagem;

    @Column(name = "logradouro", length = 150)
    private String logradouro;

    @Column(name = "numero", length = 20)
    private String numero;

    @Column(name = "bairro", length = 100)
    private String bairro;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "cep", length = 9)
    private String cep;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    /** Taxa de entrega cobrada pelo restaurante. */
    @Column(name = "taxa_entrega", precision = 8, scale = 2)
    private BigDecimal taxaEntrega = BigDecimal.ZERO;

    /** Tempo médio estimado de entrega em minutos. */
    @Column(name = "tempo_estimado_min")
    private Integer tempoEstimadoMin;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    /** Cardápio do restaurante. */
    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Produto> produtos = new ArrayList<>();

    /** Pedidos recebidos (Épico 7). */
    @OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();

    /** Avaliações recebidas (Épico 4). */
    @OneToMany(mappedBy = "restaurante", fetch = FetchType.LAZY)
    private List<Avaliacao> avaliacoes = new ArrayList<>();
}
