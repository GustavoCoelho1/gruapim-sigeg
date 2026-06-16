-- =====================================================
-- USUÁRIOS
-- =====================================================

CREATE TABLE usuarios (
    id UUID PRIMARY KEY,

    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,

    tipo_perfil VARCHAR(20) NOT NULL,

    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- ENDEREÇOS
-- =====================================================

CREATE TABLE enderecos (
    id UUID PRIMARY KEY,

    usuario_id UUID NOT NULL,

    logradouro VARCHAR(150) NOT NULL,
    numero VARCHAR(20),
    bairro VARCHAR(100),
    cidade VARCHAR(100),

    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),

    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
);

-- =====================================================
-- RESTAURANTES
-- =====================================================

CREATE TABLE restaurantes (
    id UUID PRIMARY KEY,

    usuario_id UUID NOT NULL UNIQUE,

    nome_fantasia VARCHAR(150) NOT NULL,

    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),

    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
);

-- =====================================================
-- PRODUTOS
-- =====================================================

CREATE TABLE produtos (
    id UUID PRIMARY KEY,

    restaurante_id UUID NOT NULL,

    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500),

    preco DECIMAL(10,2) NOT NULL,

    url_imagem VARCHAR(500),

    disponivel BOOLEAN NOT NULL DEFAULT TRUE,

    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP,

    FOREIGN KEY (restaurante_id)
        REFERENCES restaurantes(id)
);

-- =====================================================
-- PEDIDOS
-- =====================================================

CREATE TABLE pedidos (
    id UUID PRIMARY KEY,

    cliente_id UUID NOT NULL,
    restaurante_id UUID NOT NULL,

    endereco_entrega_id UUID NOT NULL,

    status VARCHAR(30) NOT NULL,

    metodo_pagamento VARCHAR(20) NOT NULL,

    total DECIMAL(10,2) NOT NULL,

    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (cliente_id)
        REFERENCES usuarios(id),

    FOREIGN KEY (restaurante_id)
        REFERENCES restaurantes(id),

    FOREIGN KEY (endereco_entrega_id)
        REFERENCES enderecos(id)
);

-- =====================================================
-- ITENS PEDIDO
-- =====================================================

CREATE TABLE itens_pedido (
    id UUID PRIMARY KEY,

    pedido_id UUID NOT NULL,
    produto_id UUID NOT NULL,

    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (pedido_id)
        REFERENCES pedidos(id)
        ON DELETE CASCADE,

    FOREIGN KEY (produto_id)
        REFERENCES produtos(id),

    CONSTRAINT chk_quantidade
        CHECK (quantidade > 0)
);

-- =====================================================
-- ENTREGAS
-- =====================================================

CREATE TABLE entregas (
    id UUID PRIMARY KEY,

    pedido_id UUID NOT NULL UNIQUE,

    entregador_id UUID NOT NULL,

    lat_atual DECIMAL(10,8),
    lng_atual DECIMAL(11,8),

    distancia_estimada_km DECIMAL(8,2),

    tempo_estimado_min INT,

    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (pedido_id)
        REFERENCES pedidos(id),

    FOREIGN KEY (entregador_id)
        REFERENCES usuarios(id)
);

-- =====================================================
-- HISTÓRICO DE LOCALIZAÇÃO
-- =====================================================

CREATE TABLE localizacoes_entregador (
    id UUID PRIMARY KEY,

    entrega_id UUID NOT NULL,

    latitude DECIMAL(10,8) NOT NULL,
    longitude DECIMAL(11,8) NOT NULL,

    registrado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (entrega_id)
        REFERENCES entregas(id)
        ON DELETE CASCADE
);

-- =====================================================
-- AVALIAÇÕES
-- =====================================================

CREATE TABLE avaliacoes (
    id UUID PRIMARY KEY,

    pedido_id UUID NOT NULL UNIQUE,

    nota INT NOT NULL,

    comentario TEXT,

    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (pedido_id)
        REFERENCES pedidos(id),

    CONSTRAINT chk_nota
        CHECK (nota BETWEEN 1 AND 5)
);

-- =====================================================
-- CHAMADOS
-- =====================================================

CREATE TABLE chamados (
    id UUID PRIMARY KEY,

    usuario_id UUID NOT NULL,

    categoria VARCHAR(50) NOT NULL,

    descricao TEXT NOT NULL,

    status VARCHAR(20) NOT NULL,

    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
);
