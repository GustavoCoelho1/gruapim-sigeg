-- ==========================================
-- USUÁRIOS
-- ==========================================

CREATE TABLE usuarios (
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    tipo_perfil VARCHAR(20) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- RESTAURANTES
-- ==========================================

CREATE TABLE restaurantes (
    id UUID PRIMARY KEY,
    usuario_id UUID UNIQUE NOT NULL,
    nome_fantasia VARCHAR(150) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
);

-- ==========================================
-- PRODUTOS
-- ==========================================

CREATE TABLE produtos (
    id UUID PRIMARY KEY,
    restaurante_id UUID NOT NULL,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (restaurante_id)
        REFERENCES restaurantes(id)
);

-- ==========================================
-- PEDIDOS
-- ==========================================

CREATE TABLE pedidos (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    restaurante_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    metodo_pagamento VARCHAR(20) NOT NULL,
    total DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (cliente_id)
        REFERENCES usuarios(id),

    FOREIGN KEY (restaurante_id)
        REFERENCES restaurantes(id)
);

-- ==========================================
-- ITENS PEDIDO
-- ==========================================

CREATE TABLE itens_pedido (
    id UUID PRIMARY KEY,
    pedido_id UUID NOT NULL,
    produto_id UUID NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (pedido_id)
        REFERENCES pedidos(id),

    FOREIGN KEY (produto_id)
        REFERENCES produtos(id)
);

-- ==========================================
-- ENTREGAS
-- ==========================================

CREATE TABLE entregas (
    id UUID PRIMARY KEY,
    pedido_id UUID UNIQUE NOT NULL,
    entregador_id UUID NOT NULL,
    lat_atual DECIMAL(10,8),
    lng_atual DECIMAL(11,8),

    FOREIGN KEY (pedido_id)
        REFERENCES pedidos(id),

    FOREIGN KEY (entregador_id)
        REFERENCES usuarios(id)
);
