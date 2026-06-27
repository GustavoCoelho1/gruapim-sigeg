package com.sigeg.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Subtipo Entregador do Usuario.
 * Sprint 7 – Funcionalidades do entregador.
 * Épico 3  – Rastreamento: localização atualizada via tabela 'entregas'.
 * Épico 9  – Visualizar áreas com maior demanda.
 * Épico 10 – Roteirização por distância.
 */
@Entity
@DiscriminatorValue("ENTREGADOR")
@Getter
@Setter
@NoArgsConstructor
public class Entregador extends Usuario {

    @Column(name = "cpf", length = 14, unique = true)
    private String cpf;

    @Column(name = "veiculo", length = 50)
    private String veiculo; // Ex: Moto, Bicicleta, Carro

    @Column(name = "placa", length = 10)
    private String placa;

    /** Indica disponibilidade para receber entregas (Épico 9). */
    @Column(name = "disponivel", nullable = false)
    private boolean disponivel = false;

    /** Entregas realizadas/em andamento por este entregador. */
    @OneToMany(mappedBy = "entregador", fetch = FetchType.LAZY)
    private List<Entrega> entregas = new ArrayList<>();
}
