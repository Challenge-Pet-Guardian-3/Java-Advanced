# PetGuardian API

> **Challenge FIAP - Java Advanced (Spring Boot)**
>
> Plataforma corporativa para gestão da saúde e rotina de cuidados do pet em família sob a **Arquitetura Pet-Centric**.

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
| :--- | :---: | :---: | :--- | :--- |
| **Enzo Okuizumi** | **561432** | 2TDSPG | [EnzoOkuizumiFiap](https://github.com/EnzoOkuizumiFiap) | [Enzo Okuizumi](https://www.linkedin.com/in/enzo-okuizumi-b60292256/) |
| **Gustavo Okada** | **563428** | 2TDSPG | [Gdev3356](https://github.com/Gdev3356) | [Gustavo Okada](https://www.linkedin.com/in/gustavo-okada-53a3b8359/) |
| **Lucas Barros Gouveia** | **566422** | 2TDSPG | [LuzBGouveia](https://github.com/LuzBGouveia) | [Lucas Barros Gouveia](https://www.linkedin.com/in/lucas-barros-gouveia-09b147355/) |
| **Luna de Carvalho Guimarães** | **562290** | 2TDSPG | [lunaguima](https://github.com/lunaguima) | [Luna M. Guimarães](https://www.linkedin.com/in/luna-m-guimar%C3%A3es-1850ab173/) |
| **Milton Marcelino** | **564836** | 2TDSPG | [MiltonMarcelino](https://github.com/MiltonMarcelino) | [Milton Marcelino](http://linkedin.com/in/milton-marcelino-250298142) |

---

## Sobre o Projeto

O **PetGuardian** é uma API REST corporativa em Spring Boot desenvolvida sob a **Arquitetura Pet-Centric** (diretriz da Mentoria Clyvo para o Challenge 2026), focada na saúde contínua, governança de cuidados e gamificação centrada no animal.

### 🌟 Pilares da Arquitetura Pet-Centric
- **Ecossistema Centrado no Pet:** O animal é a entidade nuclear (`pet`), possuindo sua própria evolução de bem-estar e histórico consolidado.
- **Gamificação Pet-Centric:** O score de bem-estar (`pontos_tarefa`) acumula conforme as rotinas de cuidado são concluídas pelos cuidadores.
- **Rotina Familiar Colaborativa:** Cuidadores e tutores gerenciam as tarefas diárias do pet (alimentação, remédios, passeios, higiene) com sincronização em tempo real entre co-cuidadores.
- **Rede de Cuidados (Care Circle):** Visualização consolidada de vínculos de tutoria, histórico de cuidados e pontos acumulados.

---

### Gamificação: Score e Bem-Estar no Pet

O sistema incentiva o cuidado preventivo e a consistência da rotina:
- Cada tarefa de rotina concluída gera **pontos de bem-estar** para o cuidador e para o histórico do Pet.
- O ciclo de vida das tarefas (`PENDENTE`, `CONCLUIDO`, `EXPIRADO`) garante previsibilidade e histórico auditável.

### Rede de Cuidado Familiar (Care Circle)

A rede de cuidado organiza os vínculos de tutores em torno de cada animal:
- Vínculos colaborativos entre usuários e pets em `usuario_pet`;
- Visão agregada por usuário em `/usuarios/{id}/rede-cuidado`;
- Histórico compartilhado entre todos os membros da família em `/pets/{id}/historico`.

### Rotina e Tarefas

As tarefas representam cuidados diários da rotina familiar (alimentar, medicar, passear, higienizar):
- Criadas diretamente pelos tutores da família vinculados ao Pet;
- Conclusão reativa que credita pontos ao cuidador;
- Status controlado por ciclo de vida (`PENDENTE`, `CONCLUIDO`, `EXPIRADO`).

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
├── config/              # Configuracoes (Swagger, seed)
├── exception/           # Tratamento centralizado de erros
├── validation/          # Validacoes customizadas (CEP, DDD, Enum)
│
├── usuario/             # Usuario (CRUD + paginacao + rede de cuidado)
├── usuariopet/          # Relacao N:N Usuario x Pet (vinculos e co-cuidadores)
├── pet/                 # Pet (CRUD + paginacao + historico de tarefas)
│   └── raca/            # Raca do pet
│
├── tarefa/              # Tarefa gamificada (criacao/conclusao/pontos)
├── status/              # Status de dominio
├── endereco/            # Endereco (integracao ViaCEP)
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
| SpringDoc OpenAPI | Documentacao Swagger |
| H2 Database | Banco em memoria |
| Lombok | Reducao de boilerplate |
| Maven | Build e dependencias |

---

## Endpoints da API

Todos os endpoints usam DTOs (Records), Bean Validation e documentação Swagger.

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
| `GET` | `/pets/{id}/historico` | Historico consolidado (tarefas concluidas) |
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
