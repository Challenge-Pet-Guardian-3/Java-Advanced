-- V3__seed_clinica.sql
INSERT INTO clinica (nome, rua, numero, bairro, cidade, telefone, avaliacao, distancia_km, atendimento_24h, pronto_socorro, patrocinada)
VALUES
    ('Clínica Vida Animal', 'Rua das Flores', '120', 'Centro', 'São Paulo', '1133224455', 4.7, 1.2, TRUE, TRUE, TRUE),
    ('PetCare 24h', 'Av. Brasil', '450', 'Jardim América', 'São Paulo', '1155667788', 4.3, 3.5, TRUE, FALSE, FALSE),
    ('Hospital Veterinário Amigo Fiel', 'Rua Sete de Setembro', '89', 'Vila Nova', 'São Paulo', '1199887766', 4.9, 0.8, FALSE, TRUE, FALSE);

INSERT INTO clinica_especialidade (clinica_id_clinica, especialidade) VALUES
                                                                          (1, 'Clínica Geral'), (1, 'Cirurgia'),
                                                                          (2, 'Emergência'), (2, 'Odontologia'),
                                                                          (3, 'Clínica Geral'), (3, 'Cardiologia'), (3, 'Ortopedia');