package com.sigeg.domain.enums;

/**
 * Métodos de pagamento aceitos.
 * Épico 1 – PIX, cartão ou dinheiro.
 * Corresponde à coluna metodo_pagamento da tabela pedidos.
 */
public enum MetodoPagamento {
    PIX,
    CARTAO_CREDITO,
    CARTAO_DEBITO,
    DINHEIRO
}
