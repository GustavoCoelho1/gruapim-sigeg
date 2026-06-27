package com.sigeg.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entrega – rastreamento de um Pedido em trânsito.
 * Tabela: entregas – conforme populate.sql do grupo.
 *
 * Sprint 5 – Épico 3  – Rastreamento: atualização periódica da localização.
 * Sprint 7 – Épico 9  – Áreas de demanda (concentração de pedidos).
 * Sprint 7 – Épico 10 – Roteirização: distância e tempo estimado.
 */
@Entity
@Table(
    name = "entregas",
    indexes = {
        @Index(name = "idx_entregas_pedido_id", columnList = "pedido_id"),
        @Index(name = "idx_entregas_entregador_id", columnList = "entregador_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entrega extends BaseEntity {

    /** Pedido vinculado (1-to-1, UNIQUE no banco). */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entregador_id", nullable = false)
    private Entregador entregador;

    /** Latitude atual do entregador – Épico 3. */
    @Column(name = "lat_atual", precision = 10, scale = 8)
    private BigDecimal latAtual;

    /** Longitude atual do entregador – Épico 3. */
    @Column(name = "lng_atual", precision = 11, scale = 8)
    private BigDecimal lngAtual;

    /** Distância estimada em km – Épico 10. */
    @Column(name = "distancia_estimada_km", precision = 8, scale = 2)
    private BigDecimal distanciaEstimadaKm;

    /** Tempo estimado de chegada em minutos – Épico 3. */
    @Column(name = "tempo_estimado_min")
    private Integer tempoEstimadoMin;

    /**
     * Histórico completo de localizações do entregador durante esta entrega.
     * Épico 3 – atualização periódica.
     */
    @OneToMany(mappedBy = "entrega", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LocalizacaoEntregador> localizacoes = new ArrayList<>();
}
