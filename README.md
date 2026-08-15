# PetGuardian API

> **Challenge FIAP - Java Advanced (Spring Boot)**
>
> Plataforma colaborativa para gestao de cuidados de pets, com tarefas prescritas por veterinarios e execucao por cuidadores.

<p>
  <img src="https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white" alt="Java 17" />
  <img src="https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot 4.0.6" />
  <img src="https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white" alt="Maven" />
  <img src="https://img.shields.io/badge/Database-H2-0B5394" alt="H2" />
  <img src="https://img.shields.io/badge/API-REST-2B2B2B" alt="REST API" />
</p>

| Link rápido | URL                                                     |
|---|---------------------------------------------------------|
| Repositório GitHub | https://github.com/Challenge-Pet-Guardian-3/Java-Advanced |
| Arquivo Insomnia | [/docs/Insomnia_2026-05-21.yaml](/docs/Insomnia_2026-05-21.yaml)      |
| Swagger UI (local) | http://localhost:8080/swagger-ui/index.html             |

---

## Integrantes

| Nome | RM | Turma | GitHub | LinkedIn |
|---|---:|---|---|---|
| Enzo Okuizumi | 561432 | 2TDSPG | [EnzoOkuizumiFiap](https://github.com/EnzoOkuizumiFiap) | [Enzo Okuizumi](https://www.linkedin.com/in/enzo-okuizumi-b60292256/) |
| Lucas Barros Gouveia | 566422 | 2TDSPG | [LuzBGouveia](https://github.com/LuzBGouveia) | [Lucas Barros Gouveia](https://www.linkedin.com/in/lucas-barros-gouveia-09b147355/) |
| Milton Marcelino | 564836 | 2TDSPG | [MiltonMarcelino](https://github.com/MiltonMarcelino) | [Milton Marcelino](http://linkedin.com/in/milton-marcelino-250298142) |
| Luna de Carvalho Guimaraes | 562290 | 2TDSPG | [lunaguima](https://github.com/lunaguima) | [Luna M. Guimaraes](https://www.linkedin.com/in/luna-m-guimar%C3%A3es-1850ab173/) |
| Gustavo Okada | 563428 | 2TDSPG | [Gdev3356](https://github.com/Gdev3356) | [Gustavo Okada](https://www.linkedin.com/in/gustavo-okada-53a3b8359/) |

---

## Sobre o Projeto

O **PetGuardian** é uma API REST em Spring Boot para organizar o cuidado diário de pets em cenários com vários cuidadores.

A plataforma é centrada no **Pet**:
- um pet pode ter vários cuidadores (`usuario_pet`);
- existe um responsável principal por pet (`respon_princ`);
- veterinários criam tarefas de cuidado;
- cuidadores concluem tarefas e acumulam pontos;
- atendimentos e tarefas concluidas formam o historico consolidado do pet.

---

### Gamificação: Pontos por Conclusão

O sistema incentiva engajamento por meio de pontos:
- cada tarefa possui `pontos_tarefa`;
- ao concluir, o cuidador executor recebe esses pontos;
- os pontos são calculados por soma das tarefas concluidas do usuario.

### Rede de Cuidado (care circle)

A antiga ideia de familia rígida foi substituída por uma rede dinâmica por pet:
- vínculos entre usuários e pets em `usuario_pet`;
- visão agregada por usuário em `/usuarios/{id}/rede-cuidado`;
- co-cuidadores e pets compartilhados são calculados a partir desses vínculos.

### Tarefas

As tarefas representam cuidados como alimentar, medicar, passear, curativo etc.

Regras atuais:
- tarefa é criada pelo fluxo de prescrição (sem executor inicial);
- apenas usuario vinculado ao pet pode concluir;
- status controlado por tabela (`PENDENTE`, `CONCLUIDO`, `EXPIRADO`).

---

### Atendimentos Veterinários

Cada atendimento está vinculado a:
- um pet,
- um veterinário,
- um tipo de atendimento,
- um status.

---

## Modelagem Lógica e Relacional do Banco de Dados

### Modelo Lógico 
![Modelo Lógico](docs/Logical.png)

### Modelo Relacional

![Modelo Relacional](docs/Relational.png)

---

## Arquitetura

```
src/main/java/fiap/com/br/petguardian/
├── config/              # Configuracoes (Swagger, seed, cache)
├── exception/           # Tratamento centralizado de erros
├── validation/          # Validacoes customizadas (CEP, DDD, Enum)
│
├── usuario/             # Usuario (CRUD + paginação)
├── usuariopet/          # Relacao N:N Usuario x Pet
├── pet/                 # Pet (CRUD + paginacao + historico)
│   └── raca/            # Raca do pet
│
├── tarefa/              # Tarefa gamificada (prescricao/conclusao/pontos)
├── status/              # Status de dominio
├── atendimento/         # Atendimento veterinario
│   └── tipoatendimento/ # Tipo de atendimento
│
├── clinica/             # Clinica veterinaria
├── veterinario/         # Veterinario
├── endereco/            # Endereco
│   ├── bairro/
│   ├── cidade/
│   └── estado/
│
└── telefone/            # Telefone
```

---

## Tecnologias Utilizadas

| Tecnologia | Finalidade |
|---|---|
| Java 17 | Linguagem principal |
| Spring Boot 4.0.6 | Framework principal |
| Spring Data JPA | Persistencia e ORM |
| Spring Validation | Bean Validation |
| Spring Cache | Cache de consultas |
| SpringDoc OpenAPI | Documentacao Swagger |
| H2 Database | Banco em memoria |
| Lombok | Reducao de boilerplate |
| Maven | Build e dependencias |

---

## Endpoints da API

Todos os endpoints usam DTOs, Bean Validation e documentação Swagger.

### Usuarios (`/usuarios`)

| Metodo | Endpoint | Descricao |
|---|---|---|
| `GET` | `/usuarios` | Listar todos os usuários (paginado) |
| `GET` | `/usuarios/by-nome` | Buscar usuários por nome (paginado, `?nome=`) |
| `GET` | `/usuarios/by-email` | Buscar usuário por e-mail (`?email=`) |
| `GET` | `/usuarios/{id}` | Buscar usuário por ID |
| `GET` | `/usuarios/{id}/rede-cuidado` | Visao agregada da rede de cuidado (Care Circle) |
| `POST` | `/usuarios` | Criar usuário |
| `PUT` | `/usuarios/{id}` | Atualizar usuário |
| `DELETE` | `/usuarios/{id}` | Deletar usuário |

### Pets (`/pets`)

| Metodo | Endpoint | Descricao |
|---|---|---|
| `GET` | `/pets` | Listar todos os pets (paginado) |
| `GET` | `/pets/by-nome` | Buscar pets por nome (paginado, `?nome=`) |
| `GET` | `/pets/{id}` | Buscar pet por ID |
| `GET` | `/pets/{id}/historico` | Historico consolidado (atendimentos + tarefas concluidas) |
| `POST` | `/pets` | Criar pet |
| `PUT` | `/pets/{id}` | Atualizar pet |
| `DELETE` | `/pets/{id}` | Deletar pet |
| `POST` | `/pets/{id}/usuarios/{usuarioId}` | Vincular usuario ao pet (`?principal=true/false`) |
| `DELETE` | `/pets/{id}/usuarios/{usuarioId}` | Desvincular usuario do pet |
| `POST` | `/pets/{id}/convidar` | Convidar co-cuidador por ID (`?responsavelPrincipalId=&usuarioConvidadoId=`) |
| `POST` | `/pets/{id}/convidar-email` | Convidar co-cuidador por e-mail (`?responsavelPrincipalId=&email=`) |

### Tarefas (`/tarefas`)

| Metodo | Endpoint | Descricao |
|---|---|---|
| `GET` | `/tarefas` | Listar todas as tarefas (paginado) |
| `GET` | `/tarefas/by-usuario` | Listar tarefas pendentes por usuario (paginado, `?usuarioId=`) |
| `GET` | `/tarefas/{id}` | Buscar tarefa por ID |
| `GET` | `/tarefas/by-usuario/{usuarioId}/{id}` | Buscar tarefa por usuario e ID |
| `POST` | `/tarefas` | Criar tarefa |
| `PUT` | `/tarefas/{id}` | Atualizar tarefa |
| `PATCH` | `/tarefas/{id}/concluir` | Concluir tarefa (enviar `concluinteId` no body) |
| `GET` | `/tarefas/by-usuario/pontos` | Pontos totais do cuidador (`?usuarioId=`) |
| `DELETE` | `/tarefas/{id}` | Deletar tarefa |

### Atendimentos (`/atendimentos`)

| Metodo | Endpoint | Descricao |
|---|---|---|
| `GET` | `/atendimentos` | Listar todos os atendimentos (paginado) |
| `GET` | `/atendimentos/by-usuario` | Listar atendimentos por usuario (paginado, `?usuarioId=`) |
| `GET` | `/atendimentos/{id}` | Buscar atendimento por ID |
| `POST` | `/atendimentos` | Criar atendimento |
| `PUT` | `/atendimentos/{id}` | Atualizar atendimento |
| `DELETE` | `/atendimentos/{id}` | Deletar atendimento |

### Clinicas (`/clinicas`)

| Metodo | Endpoint | Descricao |
|---|---|---|
| `GET` | `/clinicas` | Listar todas as clinicas (paginado) |
| `GET` | `/clinicas/by-nome` | Buscar clinicas por nome (paginado, `?nome=`) |
| `GET` | `/clinicas/{id}` | Buscar clinica por ID |
| `POST` | `/clinicas` | Criar clinica |
| `PUT` | `/clinicas/{id}` | Atualizar clinica |
| `DELETE` | `/clinicas/{id}` | Deletar clinica |

### Veterinarios (`/veterinarios`)

| Metodo | Endpoint | Descricao |
|---|---|---|
| `GET` | `/veterinarios` | Listar todos os veterinarios (paginado) |
| `GET` | `/veterinarios/by-nome` | Buscar veterinarios por nome (paginado, `?nome=`) |
| `GET` | `/veterinarios/by-email` | Buscar veterinario por e-mail (`?email=`) |
| `GET` | `/veterinarios/{id}` | Buscar veterinario por ID |
| `POST` | `/veterinarios` | Criar veterinario |
| `PUT` | `/veterinarios/{id}` | Atualizar veterinario |
| `DELETE` | `/veterinarios/{id}` | Deletar veterinario |

### Enderecos (`/enderecos`)

| Metodo | Endpoint | Descricao |
|---|---|---|
| `GET` | `/enderecos` | Listar todos os enderecos (paginado) |
| `GET` | `/enderecos/{id}` | Buscar endereco por ID |
| `POST` | `/enderecos` | Criar endereco |
| `PUT` | `/enderecos/{id}` | Atualizar endereco |
| `DELETE` | `/enderecos/{id}` | Deletar endereco |

---

## Como Executar

### Pre-requisitos

- Java 17+
- Maven (ou Maven Wrapper)

### Passos

Linux/Mac:
```bash
./mvnw spring-boot:run
```

Windows:
```bat
mvnw.cmd spring-boot:run
```

### Acessos

| Recurso | URL |
|---|---|
| API | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| H2 Console | `http://localhost:8080/h2-console` |
| Actuator | `http://localhost:8080/actuator` |

Configuracao H2:
- JDBC URL: `jdbc:h2:mem:petguardian`
- User: `sa`
- Password: vazio

---

## Tratamento de Erros

A API usa handler global e respostas padronizadas.

Formato:
```json
{
  "timestamp": "2026-05-20T22:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Mensagem de erro",
  "path": "/endpoint"
}
```

Tipos tratados:
- validacao de campos (`400`)
- regra de negocio (`400`)
- JSON invalido (`400`)
- integridade de dados (`400`)
- recurso nao encontrado (`404`)
- erro inesperado (`500`)

---

## Cronograma de Desenvolvimento

Abaixo consta o resumo das principais entregas e marcos do desenvolvimento técnico da API, centralizado no desenvolvedor principal, **Enzo Okuizumi**:

| Data / Período | Atividade Realizada | Responsável | Status |
|---|---|---|---|
| **01/05/2026** a **02/05/2026** | Inicialização, Setup e Estrutura de Configuração Base | Enzo Okuizumi | Concluído |
| **05/05/2026** a **10/05/2026** | Modelagem JPA Completa e Mapeamento de Entidades | Enzo Okuizumi | Concluído |
| **12/05/2026** a **14/05/2026** | Lógica de DTOs, Services e CEP | Enzo Okuizumi | Concluído |
| **18/05/2026** a **20/05/2026** | Refatorações Complexas, Tratamento Global de Erros e Validations | Enzo Okuizumi | Concluído |
| **21/05/2026** a **22/05/2026** | Paginação Geral (Swagger), Ordenação e Restauração de @PageableDefault | Enzo Okuizumi | Concluído |


## Print Trello (Tirado em 22/05/2026)

![Print Trello](/docs/cronograma-trello.png)

## Print Trello Java

![Print Trello Java](/docs/cronograma-java.png)

---
