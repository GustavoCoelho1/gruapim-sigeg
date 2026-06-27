package com.sigeg.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Subtipo Cliente do Usuario.
 * Sprint 1 – Cadastro/Login.
 * Épico 1  – Realizar Pedidos (carrinho, endereço de entrega).
 * Épico 2  – Recomendações (histórico de pedidos).
 */
@Entity
@DiscriminatorValue("CLIENTE")
@Getter
@Setter
@NoArgsConstructor
public class Cliente extends Usuario {

    @Column(name = "cpf", length = 14, unique = true)
    private String cpf;

    /** Endereços de entrega cadastrados pelo cliente. */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Endereco> enderecos = new ArrayList<>();

    /** Histórico de pedidos – base do Épico 2 (recomendações). */
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();
}
