-- ==========================================================
-- USUÁRIO E ENDEREÇO
-- ==========================================================
CREATE TABLE usuario (
                         id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(50) NOT NULL UNIQUE,
                         senha VARCHAR(255) NOT NULL,
                         telefone VARCHAR(20)
);

CREATE TABLE endereco (
                          id_endereco BIGINT AUTO_INCREMENT PRIMARY KEY,
                          logradouro VARCHAR(150) NOT NULL,
                          numero VARCHAR(10),
                          complemento VARCHAR(60),
                          bairro VARCHAR(80),
                          cidade VARCHAR(80) NOT NULL,
                          estado VARCHAR(2) NOT NULL,
                          cep VARCHAR(9) NOT NULL,
                          usuario_id_usuario BIGINT NOT NULL UNIQUE,
                          CONSTRAINT fk_endereco_usuario FOREIGN KEY (usuario_id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

-- ==========================================================
-- PET E RAÇA
-- ==========================================================
CREATE TABLE raca (
                      id_raca BIGINT AUTO_INCREMENT PRIMARY KEY,
                      nome_raca VARCHAR(30) NOT NULL
);

CREATE TABLE pet (
                     id_pet BIGINT AUTO_INCREMENT PRIMARY KEY,
                     nome VARCHAR(30) NOT NULL,
                     idade INT NOT NULL,
                     raca_id_raca BIGINT NOT NULL,
                     porte VARCHAR(10) NOT NULL,
                     sexo CHAR(1) NOT NULL,
                     castrado BOOLEAN NOT NULL,
                     peso DOUBLE,
                     ultima_vacina DATE,
                     ultima_consulta DATE,
                     avatar_id INT,
                     CONSTRAINT fk_pet_raca FOREIGN KEY (raca_id_raca) REFERENCES raca(id_raca)
);

CREATE TABLE usuario_pet (
                             usuario_id_usuario BIGINT NOT NULL,
                             pet_id_pet BIGINT NOT NULL,
                             respon_princ BOOLEAN NOT NULL DEFAULT FALSE,
                             PRIMARY KEY (usuario_id_usuario, pet_id_pet),
                             CONSTRAINT fk_up_usuario FOREIGN KEY (usuario_id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
                             CONSTRAINT fk_up_pet FOREIGN KEY (pet_id_pet) REFERENCES pet(id_pet) ON DELETE CASCADE
);

-- ==========================================================
-- STATUS E TAREFAS
-- ==========================================================
CREATE TABLE status (
                        id_status BIGINT AUTO_INCREMENT PRIMARY KEY,
                        nome_status VARCHAR(15) NOT NULL
);

CREATE TABLE tarefa (
                        id_tarefa BIGINT AUTO_INCREMENT PRIMARY KEY,
                        titulo VARCHAR(100) NOT NULL,
                        descricao VARCHAR(255),
                        pontos_tarefa INT NOT NULL,
                        criacao TIMESTAMP NOT NULL,
                        prazo TIMESTAMP NOT NULL,
                        conclusao TIMESTAMP,
                        status_id_status BIGINT NOT NULL,
                        pet_id_pet BIGINT NOT NULL,
                        usuario_id_usuario BIGINT,
                        CONSTRAINT fk_tarefa_status FOREIGN KEY (status_id_status) REFERENCES status(id_status),
                        CONSTRAINT fk_tarefa_pet FOREIGN KEY (pet_id_pet) REFERENCES pet(id_pet) ON DELETE CASCADE,
                        CONSTRAINT fk_tarefa_usuario FOREIGN KEY (usuario_id_usuario) REFERENCES usuario(id_usuario) ON DELETE SET NULL
);

-- ==========================================================
-- FAMÍLIA, MEMBROS E MURAL DE RECADOS
-- ==========================================================
CREATE TABLE familia (
                         id_familia BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(60) NOT NULL,
                         codigo_convite VARCHAR(20) NOT NULL UNIQUE,
                         data_criacao TIMESTAMP NOT NULL
);

CREATE TABLE familia_membro (
                                id_familia_membro BIGINT AUTO_INCREMENT PRIMARY KEY,
                                familia_id_familia BIGINT NOT NULL,
                                usuario_id_usuario BIGINT NOT NULL,
                                funcao VARCHAR(40) NOT NULL,
                                xp INT NOT NULL DEFAULT 0,
                                responsavel_principal BOOLEAN NOT NULL DEFAULT FALSE,
                                data_entrada TIMESTAMP NOT NULL,
                                CONSTRAINT fk_membro_familia FOREIGN KEY (familia_id_familia) REFERENCES familia(id_familia) ON DELETE CASCADE,
                                CONSTRAINT fk_membro_usuario FOREIGN KEY (usuario_id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
                                CONSTRAINT uq_familia_usuario UNIQUE (familia_id_familia, usuario_id_usuario)
);

CREATE TABLE familia_recado (
                                id_recado BIGINT AUTO_INCREMENT PRIMARY KEY,
                                familia_id_familia BIGINT NOT NULL,
                                autor_id_usuario BIGINT NOT NULL,
                                texto VARCHAR(500) NOT NULL,
                                data_hora TIMESTAMP NOT NULL,
                                editado BOOLEAN NOT NULL DEFAULT FALSE,
                                CONSTRAINT fk_recado_familia FOREIGN KEY (familia_id_familia) REFERENCES familia(id_familia) ON DELETE CASCADE,
                                CONSTRAINT fk_recado_autor FOREIGN KEY (autor_id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);