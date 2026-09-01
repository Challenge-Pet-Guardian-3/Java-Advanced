-- ==========================================================
-- CLÍNICAS VETERINÁRIAS
-- ==========================================================
CREATE TABLE clinica (
                         id_clinica BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(150) NOT NULL,
                         rua VARCHAR(150) NOT NULL,
                         numero VARCHAR(20) NOT NULL,
                         bairro VARCHAR(100) NOT NULL,
                         cidade VARCHAR(100) NOT NULL,
                         telefone VARCHAR(20) NOT NULL,
                         avaliacao DOUBLE NOT NULL,
                         distancia_km DOUBLE,
                         atendimento_24h BOOLEAN NOT NULL DEFAULT FALSE,
                         pronto_socorro BOOLEAN NOT NULL DEFAULT FALSE,
                         patrocinada BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE clinica_especialidade (
                                       clinica_id_clinica BIGINT NOT NULL,
                                       especialidade VARCHAR(80) NOT NULL,
                                       CONSTRAINT fk_especialidade_clinica FOREIGN KEY (clinica_id_clinica) REFERENCES clinica(id_clinica) ON DELETE CASCADE
);

-- ==========================================================
-- ASSISTENTE DE IA — HISTÓRICO DE CONVERSA
-- ==========================================================
CREATE TABLE ia_mensagem (
                             id_ia_mensagem BIGINT AUTO_INCREMENT PRIMARY KEY,
                             usuario_id_usuario BIGINT NOT NULL,
                             pet_id_pet BIGINT,
                             pergunta VARCHAR(500) NOT NULL,
                             resposta VARCHAR(1000) NOT NULL,
                             data_hora TIMESTAMP NOT NULL,
                             CONSTRAINT fk_ia_mensagem_usuario FOREIGN KEY (usuario_id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
                             CONSTRAINT fk_ia_mensagem_pet FOREIGN KEY (pet_id_pet) REFERENCES pet(id_pet) ON DELETE SET NULL
);

