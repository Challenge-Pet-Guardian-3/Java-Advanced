# PetGuardian API

> **Challenge FIAP - Java Advanced (Spring Boot)**
>
> Plataforma corporativa para gestão da saúde e rotina de cuidados do pet em família sob a **Arquitetura Pet-Centric**.

<p>
  <img src="https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white" alt="Java 17" />
  <img src="https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot 4.1.1" />
  <img src="https://img.shields.io/badge/Build-Gradle-02303A?logo=gradle&logoColor=white" alt="Gradle" />
  <img src="https://img.shields.io/badge/Database-PostgreSQL_16-336791?logo=postgresql&logoColor=white" alt="PostgreSQL 16" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white" alt="Docker Compose" />
  <img src="https://img.shields.io/badge/Security-JWT_RSA-F80000?logo=jsonwebtokens&logoColor=white" alt="JWT RSA" />
  <img src="https://img.shields.io/badge/API-REST-2B2B2B" alt="REST API" />
</p>

| Link rápido | URL                                                     |
|---|---------------------------------------------------------|
| Repositório GitHub | https://github.com/Challenge-Pet-Guardian-3/Java-Advanced |
| API Railway (Produção) | https://java-advanced-production-35ab.up.railway.app |
| Swagger UI (Produção) | https://java-advanced-production-35ab.up.railway.app/swagger-ui/index.html |
| Swagger UI (Local) | http://localhost:8080/swagger-ui/index.html |
| Arquivo Insomnia | [/docs/Insomnia_2026-05-21.yaml](./docs/Insomnia_2026-05-21.yaml) |

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
- **Gamificação Pet-Centric:** O score de bem-estar (`pontos_tarefa` e `pontos_aula`) acumula conforme as rotinas de cuidado e aulas educativas são concluídas.
- **Rotina Familiar Colaborativa:** Cuidadores e tutores gerenciam as tarefas diárias do pet (alimentação, remédios, passeios, higiene) com sincronização em tempo real entre co-cuidadores.
- **Rede de Cuidados (Care Circle):** Visualização consolidada de vínculos de tutoria, histórico de cuidados e pontos acumulados.
- **Gamificação Educativa (Trilhas & Módulos):** Conteúdos de adestramento e boas práticas divididos em trilhas temáticas com controle de acesso baseado em perfis (RBAC: `COMUM`, `PREMIUM`, `ADMIN`).

---

### Gamificação: Score e Bem-Estar no Pet

O sistema incentiva o cuidado preventivo e a consistência da rotina:
- Cada tarefa de rotina concluída gera **pontos de bem-estar** para o cuidador e para o histórico do Pet.
- O ciclo de vida das tarefas (`PENDENTE`, `CONCLUIDO`, `EXPIRADO`) garante previsibilidade e histórico auditável.
- As aulas educativas de adestramento concluídas por tutores `PREMIUM` agregam pontuação educacional ao score do Pet.

### Rede de Cuidado Familiar (Care Circle)

A rede de cuidado organiza os vínculos de tutores em torno de cada animal:
- Vínculos colaborativos entre usuários e pets em `usuario_pet`;
- Visão agregada por usuário em `/usuarios/{id}/rede-cuidado`;
- Histórico compartilhado entre todos os membros da família em `/pets/{id}/historico`.

### Rotina e Tarefas

As tarefas representam cuidados diários da rotina familiar (alimentar, medicar, passear, higienizar):
- Criadas diretamente pelos tutores da família vinculados ao Pet;
- Conclusão reativa que credita pontos ao cuidador;
- Suporte a desmarcação de tarefas (`/desmarcar`) para correções operacionais imediatas;
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
├── auth/                # Autenticação, SecurityConfig (RBAC), Tokens JWT com chaves RSA
├── config/              # Configurações (OpenAPI/Swagger, Beans)
├── exception/           # Tratamento centralizado de erros (GlobalExceptionHandler)
├── validation/          # Validações customizadas (CEP, DDD, Enum, Usuários)
│
├── usuario/             # Usuário (CRUD, paginação, busca por nome/email)
├── usuariopet/          # Relação N:N Usuário x Pet (Care Circle, vínculos, co-cuidadores)
├── pet/                 # Pet (CRUD, paginação, score consolidado)
│   ├── raca/            # Raça do pet
│   └── historico/       # Histórico clínico e eventos de saúde do pet
│
├── tarefa/              # Tarefa gamificada (criação, conclusão, desmarcação, pontos)
│   └── status/          # Status de domínio das tarefas (PENDENTE, CONCLUIDO, EXPIRADO)
│
├── trilha/              # Trilhas educativas de adestramento (PREMIUM / ADMIN)
│   ├── modulo/          # Módulos temáticos da trilha
│   └── aula/            # Aulas com pontuação e controle de conclusão
│
├── endereco/            # Endereço (integração declarativa ViaCEP via @HttpExchange)
│   ├── bairro/
│   ├── cidade/
│   └── estado/
│
└── telefone/            # Telefone do usuário
```

---

## Tecnologias Utilizadas

| Tecnologia | Finalidade |
|---|---|
| Java 17 LTS | Linguagem principal da aplicação |
| Spring Boot 4.1.1 | Framework base de microsserviço corporativo |
| Spring Data JPA / Hibernate | Mapeamento Objeto-Relacional e persistência com validação de schema |
| PostgreSQL 16 | Banco de dados relacional oficial (Docker local e Railway cloud) |
| Flyway Migration | Controle versionado e reprodutível de migrações de banco |
| Spring Security + OAuth2 Resource Server | Autenticação stateless baseada em JWT assinado via par de chaves RSA assimétricas |
| Spring Validation | Bean Validation para integridade declarativa nos DTOs |
| SpringDoc OpenAPI 3 | Documentação interativa Swagger UI |
| HTTP Service Interfaces | Cliente HTTP declarativo (`@HttpExchange`) para consumo de APIs externas (ViaCEP) |
| Docker & Docker Compose | Orquestração do ambiente de desenvolvimento do banco PostgreSQL |
| Lombok | Redução de código boilerplate |
| Gradle | Gerenciamento de build e dependências |

---

## Endpoints da API

Todos os endpoints operam com DTOs (Records), Bean Validation e documentação OpenAPI Swagger.

### Autenticação (`/login`)

| Método | Endpoint | Descrição | Permissão |
|---|---|---|---|
| `POST` | `/login` | Autenticar usuário e emitir token JWT assinado via RSA | Pública |

### Usuários (`/usuarios`)

| Método | Endpoint | Descrição | Permissão |
|---|---|---|---|
| `GET` | `/usuarios` | Listar todos os usuários (paginado) | Autenticado |
| `GET` | `/usuarios/by-nome` | Buscar usuários por nome (paginado, `?nome=`) | Autenticado |
| `GET` | `/usuarios/by-email` | Buscar usuário por e-mail (`?email=`) | Autenticado |
| `GET` | `/usuarios/{id}` | Buscar usuário por ID | Autenticado |
| `GET` | `/usuarios/{id}/rede-cuidado` | Visão agregada da rede de cuidado (Care Circle) | Autenticado |
| `POST` | `/usuarios` | Cadastrar novo tutor/usuário | Pública |
| `PUT` | `/usuarios/{id}` | Atualizar dados do usuário | Autenticado |
| `DELETE` | `/usuarios/{id}` | Deletar usuário | Autenticado |

### Pets (`/pets`)

| Método | Endpoint | Descrição | Permissão |
|---|---|---|---|
| `GET` | `/pets` | Listar todos os pets (paginado) | Autenticado |
| `GET` | `/pets/by-nome` | Buscar pets por nome (paginado, `?nome=`) | Autenticado |
| `GET` | `/pets/{id}` | Buscar pet por ID | Autenticado |
| `GET` | `/pets/{id}/historico` | Histórico consolidado (tarefas concluídas) | Autenticado |
| `GET` | `/pets/{id}/pontos` | Score total consolidado (Tarefas de rotina + Aulas) | Autenticado |
| `POST` | `/pets` | Cadastrar pet e atribuir tutor criador como responsável principal | Autenticado |
| `PUT` | `/pets/{id}` | Atualizar dados do pet | Autenticado |
| `DELETE` | `/pets/{id}` | Deletar pet | Autenticado |

### Care Circle (`/pets/{petId}`)

| Método | Endpoint | Descrição | Permissão |
|---|---|---|---|
| `GET` | `/pets/{petId}/cuidadores` | Listar cuidadores vinculados ao pet | Autenticado |
| `POST` | `/pets/{petId}/cuidadores` | Convidar co-cuidador por e-mail | Autenticado |
| `DELETE` | `/pets/{petId}/cuidadores/{usuarioId}` | Desvincular co-cuidador do pet (`?solicitanteId=`) | Autenticado |
| `PATCH` | `/pets/{petId}/responsavel-principal` | Transferir a titularidade de responsável principal | Autenticado |

### Tarefas (`/tarefas`)

| Método | Endpoint | Descrição | Permissão |
|---|---|---|---|
| `GET` | `/tarefas` | Listar todas as tarefas (paginado, com auto-expiração) | Autenticado |
| `GET` | `/tarefas/by-usuario` | Listar tarefas do cuidador com filtro opcional (`?usuarioId=&status=ALL\|PENDENTE...`) | Autenticado |
| `GET` | `/tarefas/by-pet/{petId}` | Listar todas as tarefas de um pet (paginado) | Autenticado |
| `GET` | `/tarefas/{id}` | Buscar tarefa por ID | Autenticado |
| `GET` | `/tarefas/by-usuario/{usuarioId}/{id}` | Buscar tarefa por cuidador e ID | Autenticado |
| `GET` | `/tarefas/by-usuario/pontos` | Total de pontos acumulados pelo cuidador (`?usuarioId=`) | Autenticado |
| `POST` | `/tarefas` | Criar nova tarefa vinculada a um cuidador do pet | Autenticado |
| `PUT` | `/tarefas/{id}` | Atualizar dados e status da tarefa | Autenticado |
| `PATCH` | `/tarefas/{id}/concluir` | Concluir tarefa (com `concluinteId` no body) | Autenticado |
| `PATCH` | `/tarefas/{id}/desmarcar` | Desmarcar tarefa concluída voltando ao status `PENDENTE` (`?usuarioId=`) | Autenticado |
| `DELETE` | `/tarefas/{id}` | Deletar tarefa | Autenticado |

### Histórico Clínico (`/historicos`)

| Método | Endpoint | Descrição | Permissão |
|---|---|---|---|
| `GET` | `/historicos` | Listar todos os registros clínicos (paginado) | Autenticado |
| `GET` | `/historicos/pet/{petId}` | Listar eventos clínicos do pet ordenados por data | Autenticado |
| `GET` | `/historicos/{id}` | Buscar registro clínico por ID | Autenticado |
| `POST` | `/historicos` | Cadastrar evento clínico (vacina, consulta, cirurgia, etc.) | Autenticado |
| `PUT` | `/historicos/{id}` | Atualizar registro clínico | Autenticado |
| `DELETE` | `/historicos/{id}` | Deletar registro clínico | Autenticado |

### Trilhas Educativas (`/trilhas`)

| Método | Endpoint | Descrição | Permissão |
|---|---|---|---|
| `GET` | `/trilhas` | Listar todas as trilhas (paginado) | `PREMIUM`, `ADMIN` |
| `GET` | `/trilhas/pet/{petId}` | Listar trilhas disponíveis para o pet | `PREMIUM`, `ADMIN` |
| `GET` | `/trilhas/{id}` | Buscar trilha por ID | `PREMIUM`, `ADMIN` |
| `POST` | `/trilhas` | Cadastrar nova trilha | `ADMIN` |
| `PUT` | `/trilhas/{id}` | Atualizar trilha existente | `ADMIN` |
| `DELETE` | `/trilhas/{id}` | Deletar trilha e seus módulos/aulas | `ADMIN` |

### Módulos das Trilhas (`/modulos`)

| Método | Endpoint | Descrição | Permissão |
|---|---|---|---|
| `GET` | `/modulos` | Listar todos os módulos (paginado) | `PREMIUM`, `ADMIN` |
| `GET` | `/modulos/trilha/{trilhaId}` | Listar módulos de uma trilha | `PREMIUM`, `ADMIN` |
| `GET` | `/modulos/{id}` | Buscar módulo por ID | `PREMIUM`, `ADMIN` |
| `POST` | `/modulos` | Criar módulo associado a uma trilha | `ADMIN` |
| `PUT` | `/modulos/{id}` | Atualizar módulo existente | `ADMIN` |
| `DELETE` | `/modulos/{id}` | Deletar módulo e suas aulas | `ADMIN` |

### Aulas Educativas (`/aulas`)

| Método | Endpoint | Descrição | Permissão |
|---|---|---|---|
| `GET` | `/aulas` | Listar todas as aulas (paginado) | `PREMIUM`, `ADMIN` |
| `GET` | `/aulas/modulo/{moduloId}` | Listar aulas de um módulo | `PREMIUM`, `ADMIN` |
| `GET` | `/aulas/{id}` | Buscar aula por ID | `PREMIUM`, `ADMIN` |
| `POST` | `/aulas` | Criar nova aula | `ADMIN` |
| `PUT` | `/aulas/{id}` | Atualizar aula | `ADMIN` |
| `PATCH` | `/aulas/{id}/concluir` | Concluir aula (`concluida = true`) e somar pontos ao pet | `PREMIUM`, `ADMIN` |
| `PATCH` | `/aulas/{id}/desmarcar` | Desmarcar aula concluída (`concluida = false`) e estornar pontos | `PREMIUM`, `ADMIN` |
| `DELETE` | `/aulas/{id}` | Deletar aula | `ADMIN` |

### Endereços (`/enderecos`)

| Método | Endpoint | Descrição | Permissão |
|---|---|---|---|
| `GET` | `/enderecos` | Listar todos os endereços (paginado) | Autenticado |
| `GET` | `/enderecos/{id}` | Buscar endereço por ID | Autenticado |
| `POST` | `/enderecos` | Criar endereço integrado ao ViaCEP via `@HttpExchange` | Autenticado |
| `PUT` | `/enderecos/{id}` | Atualizar endereço | Autenticado |
| `DELETE` | `/enderecos/{id}` | Deletar endereço | Autenticado |

---

## Como Executar

### Pré-requisitos

- Java 17 LTS instalado
- (Opcional) Docker e Docker Compose instalados, caso deseje rodar o banco localmente

---

### Configuração de Variáveis de Ambiente (`.env`)

A aplicação suporta conexão transparente tanto ao banco de dados em nuvem no **Railway** quanto a uma instância local via **Docker Compose**. O `application.properties` já possui as credenciais do Railway configuradas como **fallback padrão**, permitindo que a aplicação execute out-of-the-box sem dependência de Docker local.

Caso queira alternar entre ambientes, crie um arquivo `.env` na raiz do projeto (baseando-se no `.env.example`):

```env
# ==============================================================
# OPÇÃO 1: Conectar no PostgreSQL do Railway (Padrão / Nuvem)
# ==============================================================
# Não precisa configurar nada: o application.properties já usa o Railway como padrão.
# PGHOST=altaria.proxy.rlwy.net
# PGPORT=41468
# PGDATABASE=railway
# PGUSER=postgres
# PGPASSWORD=PpPfEBowUuHgGMRqDzZjnOZbqKlbKrZl

# ==============================================================
# OPÇÃO 2: Conectar no PostgreSQL Local via Docker (compose.yml)
# ==============================================================
# Para apontar para o Docker local, inicie o container (docker compose up -d)
# e descomente as variáveis abaixo:
# PGHOST=localhost
# PGPORT=5432
# PGDATABASE=petguardian
# PGUSER=petguardian
# PGPASSWORD=petguardian

# Porta da Aplicação
PORT=8080
```

---

### Passos para Execução

#### Modo 1: Executar Direto com Railway (Padrão / Sem Docker Desktop)
Não requer Docker Desktop nem configuração prévia de `.env`. A aplicação inicializa conectando diretamente na nuvem:

```bash
./gradlew bootRun
```
*(Ou dê **Run** na classe `PetGuardianApplication` pela sua IDE).*

#### Modo 2: Executar Localmente com Docker Compose
1. Subir o contêiner do banco:
```bash
docker compose up -d
```

2. Definir as variáveis do banco local no arquivo `.env` (ou no terminal / Run Configuration da IDE):
```powershell
$env:PGHOST="localhost"
$env:PGPORT="5432"
$env:PGDATABASE="petguardian"
$env:PGUSER="petguardian"
$env:PGPASSWORD="petguardian"
```

3. Executar a aplicação:
```bash
./gradlew bootRun
```

As migrações do schema e tabelas serão executadas automaticamente pelo **Flyway** na inicialização da aplicação.

---

### Script de Seed e Testes E2E Automatizados (`seed-railway.ps1`)

O projeto inclui um script PowerShell completo e idempotente para popular o banco de dados e validar todos os endpoints (usuários com ViaCEP, pets, co-cuidadores, tarefas futuras, histórico clínico, trilhas, módulos e aulas):

```powershell
powershell -ExecutionPolicy Bypass -File .\seed-railway.ps1
```

> **Dica:** Para rodar contra o servidor local após subir a aplicação em `localhost:8080`, basta passar o parâmetro `-BaseUrl`:
> ```powershell
> powershell -ExecutionPolicy Bypass -File .\seed-railway.ps1 -BaseUrl "http://localhost:8080"
> ```

---

### Tabela de Acessos

| Ambiente | Recurso | URL |
|---|---|---|
| **Produção (Railway)** | API Base | `https://java-advanced-production-35ab.up.railway.app` |
| **Produção (Railway)** | Swagger UI | `https://java-advanced-production-35ab.up.railway.app/swagger-ui/index.html` |
| **Produção (Railway)** | Actuator Health | `https://java-advanced-production-35ab.up.railway.app/actuator/health` |
| **Local** | API Base | `http://localhost:8080` |
| **Local** | Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| **Local** | OpenAPI Docs | `http://localhost:8080/v3/api-docs` |
| **Local** | Actuator Health | `http://localhost:8080/actuator/health` |


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
