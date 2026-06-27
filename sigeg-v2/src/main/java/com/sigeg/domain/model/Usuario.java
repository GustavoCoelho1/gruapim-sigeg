package com.sigeg.domain.model;

import com.sigeg.domain.enums.TipoPerfil;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade base de usuário – tabela 'usuarios'.
 *
 * Sprint 1 – Cadastro/Login de clientes, restaurantes e entregadores.
 * RNF 2   – senha armazenada como hash BCrypt (campo senha_hash).
 *
 * Usa SINGLE_TABLE com discriminador tipo_perfil, mantendo a estrutura
 * simples do populate.sql do grupo. Subtipos (Cliente, Restaurante,
 * Entregador) mapeados via @DiscriminatorValue em tabelas separadas
 * ou no próprio Usuario para o MVP.
 */
@Entity
@Table(
    name = "usuarios",
    uniqueConstraints = @UniqueConstraint(name = "uk_usuarios_email", columnNames = "email"),
    indexes = @Index(name = "idx_usuarios_email", columnList = "email")
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_perfil", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario extends BaseEntity {

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    /**
     * Senha hasheada com BCrypt.
     * NUNCA retorne este campo nos DTOs de response.
     */
    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_perfil", nullable = false, insertable = false, updatable = false, length = 20)
    private TipoPerfil tipoPerfil;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;
}
