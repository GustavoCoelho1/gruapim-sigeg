-- =====================================================================
-- SIGEG – V1: Schema inicial
-- Grupo: Gustavo Coelho, Hugo Barbosa, Luis Fernando
-- Sprint 1 (base) – alinhado ao populate.sql do grupo
-- =====================================================================

-- =====================================================================
-- USUÁRIOS (tabela base – herança SINGLE_TABLE via discriminador tipo_perfil)
-- =====================================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id              UUID         PRIMARY KEY,
    tipo_perfil     VARCHAR(20)  NOT NULL,               -- CLIENTE | RESTAURANTE | ENTREGADOR | ADMIN

    nome            VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL,
    senha_hash      VARCHAR(255) NOT NULL,

    -- Campos de Cliente
    cpf             VARCHAR(14)  UNIQUE,

    -- Campos de Entregador
    veiculo         VARCHAR(50),
    placa           VARCHAR(10),
    disponivel      BOOLEAN      NOT NULL DEFAULT FALSE,

    ativo           BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   TIMESTAMP,

    CONSTRAINT uk_usuarios_email UNIQUE (email)
);

CREATE INDEX IF NOT EXISTS idx_usuarios_email       ON usuarios (email);
CREATE INDEX IF NOT EXISTS idx_usuarios_tipo_perfil ON usuarios (tipo_perfil);

-- =====================================================================
-- ENDEREÇOS (do cliente)
-- =====================================================================
CREATE TABLE IF NOT EXISTS enderecos (
    id          UUID         PRIMARY KEY,
    usuario_id  UUID         NOT NULL,

    logradouro  VARCHAR(150) NOT NULL,
    numero      VARCHAR(20),
    bairro      VARCHAR(100),
    cidade      VARCHAR(100),
    cep         VARCHAR(9),
    complemento VARCHAR(100),
    apelido     VARCHAR(50),

    latitude    DECIMAL(10,8),
    longitude   DECIMAL(11,8),

    principal   BOOLEAN      NOT NULL DEFAULT FALSE,

    criado_em   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP,

    CONSTRAINT fk_enderecos_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE INDEX IF NOT EXISTS idx_enderecos_usuario_id ON enderecos (usuario_id);

-- =====================================================================
-- RESTAURANTES
-- =====================================================================
CREATE TABLE IF NOT EXISTS restaurantes (
    id                  UUID          PRIMARY KEY,
    usuario_id          UUID          NOT NULL UNIQUE,

    nome_fantasia       VARCHAR(150)  NOT NULL,
    descricao           VARCHAR(500),
    url_imagem          VARCHAR(500),

    logradouro          VARCHAR(150),
    numero              VARCHAR(20),
    bairro              VARCHAR(100),
    cidade              VARCHAR(100),
    cep                 VARCHAR(9),

    latitude            DECIMAL(10,8),
    longitude           DECIMAL(11,8),

    taxa_entrega        DECIMAL(8,2)  NOT NULL DEFAULT 0,
    tempo_estimado_min  INTEGER,

    ativo               BOOLEAN       NOT NULL DEFAULT TRUE,

    criado_em           TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em       TIMESTAMP,

    CONSTRAINT fk_restaurantes_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE INDEX IF NOT EXISTS idx_restaurantes_usuario_id ON restaurantes (usuario_id);
CREATE INDEX IF NOT EXISTS idx_restaurantes_ativo      ON restaurantes (ativo);

-- =====================================================================
-- PRODUTOS
-- =====================================================================
CREATE TABLE IF NOT EXISTS produtos (
    id              UUID          PRIMARY KEY,
    restaurante_id  UUID          NOT NULL,

    nome            VARCHAR(100)  NOT NULL,
    descricao       VARCHAR(500),
    preco           DECIMAL(10,2) NOT NULL,
    url_imagem      VARCHAR(500),
    categoria       VARCHAR(100),

    disponivel      BOOLEAN       NOT NULL DEFAULT TRUE,
    total_vendido   BIGINT        NOT NULL DEFAULT 0,

    criado_em       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   TIMESTAMP,

    CONSTRAINT fk_produtos_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id)
);

CREATE INDEX IF NOT EXISTS idx_produtos_restaurante_id ON produtos (restaurante_id);
CREATE INDEX IF NOT EXISTS idx_produtos_disponivel      ON produtos (disponivel);

-- =====================================================================
-- PEDIDOS
-- =====================================================================
CREATE TABLE IF NOT EXISTS pedidos (
    id                   UUID          PRIMARY KEY,
    cliente_id           UUID          NOT NULL,
    restaurante_id       UUID          NOT NULL,
    endereco_entrega_id  UUID          NOT NULL,

    status               VARCHAR(30)   NOT NULL,    -- StatusPedido enum
    metodo_pagamento     VARCHAR(20)   NOT NULL,    -- MetodoPagamento enum

    subtotal             DECIMAL(10,2) NOT NULL,
    taxa_entrega         DECIMAL(8,2)  NOT NULL DEFAULT 0,
    total                DECIMAL(10,2) NOT NULL,

    observacoes          VARCHAR(500),

    criado_em            TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em        TIMESTAMP,

    CONSTRAINT fk_pedidos_cliente     FOREIGN KEY (cliente_id)          REFERENCES usuarios(id),
    CONSTRAINT fk_pedidos_restaurante FOREIGN KEY (restaurante_id)       REFERENCES restaurantes(id),
    CONSTRAINT fk_pedidos_endereco    FOREIGN KEY (endereco_entrega_id)  REFERENCES enderecos(id)
);

CREATE INDEX IF NOT EXISTS idx_pedidos_cliente_id    ON pedidos (cliente_id);
CREATE INDEX IF NOT EXISTS idx_pedidos_restaurante_id ON pedidos (restaurante_id);
CREATE INDEX IF NOT EXISTS idx_pedidos_status        ON pedidos (status);
CREATE INDEX IF NOT EXISTS idx_pedidos_criado_em     ON pedidos (criado_em);

-- =====================================================================
-- ITENS DO PEDIDO
-- =====================================================================
CREATE TABLE IF NOT EXISTS itens_pedido (
    id              UUID          PRIMARY KEY,
    pedido_id       UUID          NOT NULL,
    produto_id      UUID          NOT NULL,

    quantidade      INT           NOT NULL,
    preco_unitario  DECIMAL(10,2) NOT NULL,
    subtotal        DECIMAL(10,2) NOT NULL,
    observacao      VARCHAR(300),

    criado_em       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   TIMESTAMP,

    CONSTRAINT fk_itens_pedido   FOREIGN KEY (pedido_id)  REFERENCES pedidos(id)  ON DELETE CASCADE,
    CONSTRAINT fk_itens_produto  FOREIGN KEY (produto_id) REFERENCES produtos(id),
    CONSTRAINT chk_quantidade    CHECK (quantidade > 0)
);

CREATE INDEX IF NOT EXISTS idx_itens_pedido_id  ON itens_pedido (pedido_id);
CREATE INDEX IF NOT EXISTS idx_itens_produto_id ON itens_pedido (produto_id);

-- =====================================================================
-- ENTREGAS (rastreamento – Épico 3 / 10)
-- =====================================================================
CREATE TABLE IF NOT EXISTS entregas (
    id                      UUID          PRIMARY KEY,
    pedido_id               UUID          NOT NULL UNIQUE,
    entregador_id           UUID          NOT NULL,

    lat_atual               DECIMAL(10,8),
    lng_atual               DECIMAL(11,8),

    distancia_estimada_km   DECIMAL(8,2),
    tempo_estimado_min      INT,

    criado_em               TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em           TIMESTAMP,

    CONSTRAINT fk_entregas_pedido     FOREIGN KEY (pedido_id)    REFERENCES pedidos(id),
    CONSTRAINT fk_entregas_entregador FOREIGN KEY (entregador_id) REFERENCES usuarios(id)
);

CREATE INDEX IF NOT EXISTS idx_entregas_pedido_id     ON entregas (pedido_id);
CREATE INDEX IF NOT EXISTS idx_entregas_entregador_id ON entregas (entregador_id);

-- =====================================================================
-- HISTÓRICO DE LOCALIZAÇÃO (Épico 3)
-- =====================================================================
CREATE TABLE IF NOT EXISTS localizacoes_entregador (
    id            UUID          PRIMARY KEY,
    entrega_id    UUID          NOT NULL,

    latitude      DECIMAL(10,8) NOT NULL,
    longitude     DECIMAL(11,8) NOT NULL,
    registrado_em TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    criado_em     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP,

    CONSTRAINT fk_localizacoes_entrega FOREIGN KEY (entrega_id) REFERENCES entregas(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_localizacoes_entrega_id ON localizacoes_entregador (entrega_id);

-- =====================================================================
-- AVALIAÇÕES (Épico 4)
-- =====================================================================
CREATE TABLE IF NOT EXISTS avaliacoes (
    id              UUID      PRIMARY KEY,
    pedido_id       UUID      NOT NULL UNIQUE,
    restaurante_id  UUID      NOT NULL,
    entregador_id   UUID,

    nota            INT       NOT NULL,
    comentario      TEXT,

    criado_em       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   TIMESTAMP,

    CONSTRAINT fk_avaliacoes_pedido      FOREIGN KEY (pedido_id)      REFERENCES pedidos(id),
    CONSTRAINT fk_avaliacoes_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id),
    CONSTRAINT fk_avaliacoes_entregador  FOREIGN KEY (entregador_id)  REFERENCES usuarios(id),
    CONSTRAINT chk_nota                  CHECK (nota BETWEEN 1 AND 5)
);

CREATE INDEX IF NOT EXISTS idx_avaliacoes_pedido_id      ON avaliacoes (pedido_id);
CREATE INDEX IF NOT EXISTS idx_avaliacoes_restaurante_id ON avaliacoes (restaurante_id);

-- =====================================================================
-- CHAMADOS DE SUPORTE (Épico 5)
-- =====================================================================
CREATE TABLE IF NOT EXISTS chamados (
    id          UUID      PRIMARY KEY,
    usuario_id  UUID      NOT NULL,
    pedido_id   UUID,

    categoria   VARCHAR(50)  NOT NULL,    -- CategoriaChamado enum
    status      VARCHAR(20)  NOT NULL,    -- StatusChamado enum
    titulo      VARCHAR(200) NOT NULL,
    descricao   TEXT         NOT NULL,
    resposta    TEXT,

    criado_em   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP,

    CONSTRAINT fk_chamados_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_chamados_pedido  FOREIGN KEY (pedido_id)  REFERENCES pedidos(id)
);

CREATE INDEX IF NOT EXISTS idx_chamados_usuario_id ON chamados (usuario_id);
CREATE INDEX IF NOT EXISTS idx_chamados_pedido_id  ON chamados (pedido_id);
CREATE INDEX IF NOT EXISTS idx_chamados_status     ON chamados (status);
