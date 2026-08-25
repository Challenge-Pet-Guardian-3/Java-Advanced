-- Usuário (sem telefone)
CREATE TABLE usuario (
                         id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(50) NOT NULL,
                         senha VARCHAR(255) NOT NULL
);

-- Pet e raça
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
                             CONSTRAINT fk_up_usuario FOREIGN KEY (usuario_id_usuario) REFERENCES usuario(id_usuario),
                             CONSTRAINT fk_up_pet FOREIGN KEY (pet_id_pet) REFERENCES pet(id_pet)
);

-- Status (tabela de domínio)
CREATE TABLE status (
                        id_status BIGINT AUTO_INCREMENT PRIMARY KEY,
                        nome_status VARCHAR(15) NOT NULL
);

-- Tarefa
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
                        CONSTRAINT fk_tarefa_pet FOREIGN KEY (pet_id_pet) REFERENCES pet(id_pet),
                        CONSTRAINT fk_tarefa_usuario FOREIGN KEY (usuario_id_usuario) REFERENCES usuario(id_usuario)
);

-- Família, membros e mural de recados
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
                                CONSTRAINT fk_membro_familia FOREIGN KEY (familia_id_familia) REFERENCES familia(id_familia),
                                CONSTRAINT fk_membro_usuario FOREIGN KEY (usuario_id_usuario) REFERENCES usuario(id_usuario),
                                CONSTRAINT uq_familia_usuario UNIQUE (familia_id_familia, usuario_id_usuario)
);

CREATE TABLE familia_recado (
                                id_recado BIGINT AUTO_INCREMENT PRIMARY KEY,
                                familia_id_familia BIGINT NOT NULL,
                                autor_id_usuario BIGINT NOT NULL,
                                texto VARCHAR(500) NOT NULL,
                                data_hora TIMESTAMP NOT NULL,
                                editado BOOLEAN NOT NULL DEFAULT FALSE,
                                CONSTRAINT fk_recado_familia FOREIGN KEY (familia_id_familia) REFERENCES familia(id_familia),
                                CONSTRAINT fk_recado_autor FOREIGN KEY (autor_id_usuario) REFERENCES usuario(id_usuario)
);