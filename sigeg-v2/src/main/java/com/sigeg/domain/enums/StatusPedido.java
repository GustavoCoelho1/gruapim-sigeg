package com.sigeg.domain.enums;

/**
 * Ciclo de vida de um pedido.
 * Épico 7 – Gestão de Pedidos.
 * Corresponde à coluna status da tabela pedidos.
 */
public enum StatusPedido {
    RECEBIDO,
    EM_PREPARO,
    SAIU_PARA_ENTREGA,
    ENTREGUE,
    CANCELADO
}
