package com.sigeg.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Registro histórico de localização do entregador durante uma entrega.
 * Tabela: localizacoes_entregador – conforme populate.sql do grupo.
 *
 * Épico 3 – Rastreamento em tempo real (MVP: atualização periódica).
 * Cada atualização do entregador gera um novo registro nesta tabela,
 * permitindo exibir o trajeto completo e calcular o ETA.
 */
@Entity
@Table(
    name = "localizacoes_entregador",
    indexes = @Index(name = "idx_localizacoes_entrega_id", columnList = "entrega_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalizacaoEntregador extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entrega_id", nullable = false)
    private Entrega entrega;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "registrado_em", nullable = false)
    private LocalDateTime registradoEm;

    @PrePersist
    public void preencherTimestamp() {
        this.registradoEm = LocalDateTime.now();
    }
}
