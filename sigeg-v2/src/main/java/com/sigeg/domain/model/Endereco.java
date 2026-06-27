package com.sigeg.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Endereço de entrega do cliente.
 * Tabela: enderecos – conforme populate.sql do grupo.
 * Épico 1 – o cliente seleciona o endereço ao finalizar o pedido.
 * Épico 3 / 10 – coordenadas usadas para rastreamento e roteirização.
 */
@Entity
@Table(
    name = "enderecos",
    indexes = @Index(name = "idx_enderecos_usuario_id", columnList = "usuario_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "logradouro", nullable = false, length = 150)
    private String logradouro;

    @Column(name = "numero", length = 20)
    private String numero;

    @Column(name = "bairro", length = 100)
    private String bairro;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "cep", length = 9)
    private String cep;

    @Column(name = "complemento", length = 100)
    private String complemento;

    /** Latitude para cálculo de distância (Épico 3, Épico 10). */
    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    /** Longitude para cálculo de distância. */
    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    /** Apelido do endereço, ex: "Casa", "Trabalho". */
    @Column(name = "apelido", length = 50)
    private String apelido;

    @Column(name = "principal", nullable = false)
    private boolean principal = false;
}
