# 🐾 AGENT.md - PetGuardian API (Java-Advanced)

Guia completo e documentação técnica da arquitetura, regras de negócio, perfis de acesso (RBAC), integração mobile, endpoints, validações e fluxos do projeto **PetGuardian** (`Java-Advanced`).

---

## 🏛️ 1. Visão Geral da Arquitetura & Stack

- **Java Version:** Java 17 LTS
- **Framework:** Spring Boot 3.4.3
- **Persistência & ORM:** Spring Data JPA + Hibernate (com `ddl-auto=validate`)
- **Migrações de Banco:** Flyway (`org.flywaydb:flyway-core`) com scripts em `src/main/resources/db/migration/`
- **Segurança:** Spring Security + OAuth2 Resource Server com tokens JWT assinados via par de chaves assimétricas RSA (PKCS#8).
- **Cache:** Spring Starter Cache (cache em memória para lookups de Status).
- **Documentação de API:** SpringDoc OpenAPI 3 (`/swagger-ui.html` e `/v3/api-docs`).
- **Banco de Dados:** H2 Database (em memória para desenvolvimento/testes rápidos) e Oracle Database 21c (produção/DER oficial).

---

## 👥 2. Perfis de Usuário & Controle de Acesso (RBAC)

O sistema opera com dois perfis de acesso formalizados no Enum `UsuarioRole`:

| Perfil (`UsuarioRole`) | Escopo de Acesso | Recursos Bloqueados |
| :--- | :--- | :--- |
| **`COMUM`** (Tutor Gratuito) | Pets, Care Circle (Rede de Cuidado), Tarefas Diárias, Histórico Clínico, Endereço e Perfil | Trilhas, Módulos, Aulas e Assistente IA |
| **`PREMIUM`** (Tutor Assinante) | Acesso total irrestrito a todos os módulos e gamificação educativa | Nenhum |

### 🔒 Proteção de Rotas:
- **Rotas Exclusivas Premium (`ROLE_PREMIUM`):**
  - `/trilhas/**` (Gestão de Trilhas de Aprendizado)
  - `/modulos/**` (Módulos Educativos)
  - `/aulas/**` (Aulas e Conteúdos Instrutivos)
  - *Assistente IA*
- **Bloqueio Automático:** Requisições de usuários `COMUM` para essas rotas são barradas pelo Spring Security com **`403 Forbidden`** (`AccessDeniedException`).

---

## 🔐 3. Autenticação & Integração com o Mobile (JWT RSA)

```
[ Mobile App ] --- POST /login { email, senha } ---> [ Spring Boot API ]
[ Mobile App ] <-- 200 OK { token, usuario: { role } } -- [ TokenService (RSA) ]
```

- **Fluxo Stateless:** Todas as rotas autenticadas utilizam tokens Bearer JWT no header `Authorization: Bearer <token>`.
- **Rotas Públicas (`permitAll`):**
  - `POST /login` (autenticação de tutores)
  - `POST /usuarios` (cadastro de novos tutores)
  - `/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html`
  - `/h2-console/**` (com frameOptions `sameOrigin`)
  - `/actuator/health`, `/actuator/info`
- **Token JWT:**
  - Emissor: `petguardian-api`
  - Expiração: 1 hora a partir da emissão.
  - Claims: `role` (ex: `COMUM` ou `PREMIUM` -> convertido para `ROLE_COMUM` / `ROLE_PREMIUM`), `sub` (e-mail do usuário).
- **Consumo no Mobile:**
  - O app armazena o token em armazenamento seguro (*SecureStorage* / *KeyStore*).
  - Com base em `usuario.role`, o app libera a interface ou exibe um modal convidando o usuário a assinar o plano Premium.

---

## 🔄 4. Dois Fluxos Funcionais Completos do Sistema

### 🐾 **Fluxo 1: Cuidado Colaborativo e Rotina Diária (Acesso COMUM e PREMIUM)**
1. **Cadastro & Login:** Tutor cadastra-se em `POST /usuarios` (recebe role `COMUM` por padrão ou `PREMIUM`) e faz login em `POST /login`.
2. **Cadastro do Pet:** Criação do animal via `POST /pets` (o tutor criador torna-se automaticamente `responsavelPrincipal = true`).
3. **Formação do Care Circle:** Tutor convida co-cuidadores pelo e-mail via `POST /pets/{petId}/cuidadores`.
4. **Ciclo de Tarefas:** Cuidadores criam tarefas de rotina (`POST /tarefas` vinculadas obrigatoriamente a um cuidador) e concluem com `PATCH /tarefas/{id}/concluir`.
5. **Score:** Consulta de pontos acumulados do cuidador e visualização consolidada da rede em `GET /usuarios/{id}/rede-cuidado`.

### 🎓 **Fluxo 2: Gamificação Educativa & Trilhas (Exclusivo PREMIUM)**
1. **Acesso Protegido:** Tutor `PREMIUM` acessa as trilhas de adestramento do pet via `GET /trilhas/pet/{petId}`.
2. **Progresso de Conteúdo:** Tutor navega pelos módulos (`GET /modulos/trilha/{trilhaId}`) e acessa as aulas (`GET /aulas/modulo/{moduloId}`).
3. **Conclusão de Aulas:** Conclusão de aula marcando `concluida = true` com pontuação educativa.
4. **Gamificação Consolidada:** O endpoint `GET /pets/{id}/pontos` agrega em tempo real os pontos das tarefas de rotina + pontos das aulas concluídas, gerando o score total de evolução do pet.

---

## 🌐 5. Catálogo Completo de Endpoints

### 🔑 Autenticação (`/login`)
| Método | Endpoint | Request Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/login` | `LoginRequest` (`email`, `senha`) | `LoginResponse` (`token`, `UsuarioResponse`) | Autentica o usuário e retorna o token JWT e o perfil com `role`. |

---

### 👤 Usuários (`/usuarios`)
| Método | Endpoint | Parâmetros / Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/usuarios` | `Pageable` (`page`, `size`, `sort`) | `Page<UsuarioResponse>` | Lista usuários paginados (ordenados por nome). |
| `GET` | `/usuarios/by-nome` | `@RequestParam String nome`, `Pageable` | `Page<UsuarioResponse>` | Busca usuários por trecho do nome (case-insensitive). |
| `GET` | `/usuarios/by-email` | `@RequestParam String email` | `UsuarioResponse` | Busca usuário por e-mail exato. |
| `GET` | `/usuarios/{id}` | `@PathVariable Long id` | `UsuarioResponse` | Busca usuário por ID. |
| `GET` | `/usuarios/{id}/rede-cuidado` | `@PathVariable Long id` | `RedeCuidadoResponse` | Retorna o Care Circle consolidado (pets, co-cuidadores e tarefas). |
| `POST` | `/usuarios` | `UsuarioRequest` | `UsuarioResponse` (201 Created) | Cadastra um novo usuário (`role` opcional, default `COMUM`), telefone e endereço. |
| `PUT` | `/usuarios/{id}` | `UsuarioRequest` | `UsuarioResponse` (200 OK) | Atualiza os dados do usuário. |
| `DELETE`| `/usuarios/{id}` | `@PathVariable Long id` | 204 No Content | Remove o usuário do sistema. |

---

### 🐶 Pets (`/pets`)
| Método | Endpoint | Parâmetros / Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/pets` | `Pageable` (`page`, `size`, `sort`) | `Page<PetResponse>` | Lista pets com paginação (otimizado com `raca`). |
| `GET` | `/pets/by-nome` | `@RequestParam String nome`, `Pageable` | `Page<PetResponse>` | Busca pets por nome. |
| `GET` | `/pets/{id}` | `@PathVariable Long id` | `PetResponse` | Busca pet por ID. |
| `GET` | `/pets/{id}/historico` | `@PathVariable Long id` | `PetHistoryResponse` | Histórico consolidado de tarefas concluídas do pet. |
| `GET` | `/pets/{id}/pontos` | `@PathVariable Long id` | `PetPontuacaoResponse` | Retorna a soma de pontos do pet (Tarefas + Aulas). |
| `POST` | `/pets` | `PetRequest` | `PetResponse` (201 Created) | Cria um pet e vincula o criador como `responsavelPrincipal`. |
| `PUT` | `/pets/{id}` | `PetRequest` | `PetResponse` (200 OK) | Atualiza os dados do pet e seu responsável. |
| `DELETE`| `/pets/{id}` | `@PathVariable Long id` | 204 No Content | Remove o pet do sistema. |

---

### 🤝 Care Circle - Rede de Cuidado (`/pets/{petId}`)
| Método | Endpoint | Parâmetros / Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/pets/{petId}/cuidadores` | `@PathVariable Long petId` | `List<CoCuidadorResponse>` | Lista todos os cuidadores vinculados ao pet. |
| `POST` | `/pets/{petId}/cuidadores` | `CoCuidadorRequest` (`responsavelPrincipalId`, `email`) | `CoCuidadorResponse` (201 Created) | Convida um co-cuidador por e-mail (autorizado pelo responsável). |
| `DELETE`| `/pets/{petId}/cuidadores/{usuarioId}` | `petId`, `usuarioId`, `@RequestParam(required = false) solicitanteId` | 204 No Content | Desvincula um co-cuidador (o próprio usuário ou o responsável). |
| `PATCH`| `/pets/{petId}/responsavel-principal` | `TransferirResponsabilidadeRequest` | 204 No Content | Transfere a titularidade de responsável principal para outro co-cuidador. |

---

### 📋 Tarefas da Rotina (`/tarefas`)
| Método | Endpoint | Parâmetros / Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/tarefas` | `Pageable` | `Page<TarefaResponse>` | Lista todas as tarefas (com auto-expiração de prazos). |
| `GET` | `/tarefas/by-usuario` | `@RequestParam Long usuarioId`, `Pageable` | `Page<TarefaResponse>` | Lista tarefas pendentes do cuidador. |
| `GET` | `/tarefas/{id}` | `@PathVariable Long id` | `TarefaResponse` | Busca tarefa por ID. |
| `GET` | `/tarefas/by-usuario/{usuarioId}/{id}` | `usuarioId`, `id` | `TarefaResponse` | Busca tarefa específica pertencente ao cuidador. |
| `GET` | `/tarefas/by-usuario/pontos` | `@RequestParam Long usuarioId` | `Integer` | Consulta total de pontos acumulados pelo cuidador. |
| `POST` | `/tarefas` | `TarefaRequest` (`usuarioId` NOT NULL) | `TarefaResponse` (201 Created) | Cria nova tarefa vinculada obrigatoriamente a um cuidador do pet com status `PENDENTE`. |
| `PUT` | `/tarefas/{id}` | `TarefaRequest` | `TarefaResponse` (200 OK) | Atualiza os dados e status da tarefa. |
| `PATCH`| `/tarefas/{id}/concluir` | `TarefaConclusaoRequest` (`concluinteId`) | `TarefaResponse` (200 OK) | Marca tarefa como `CONCLUIDO`, vincula executor e data de conclusão. |
| `DELETE`| `/tarefas/{id}` | `@PathVariable Long id` | 204 No Content | Deleta uma tarefa. |

---

### 🩺 Histórico Clínico e Eventos (`/historicos`)
| Método | Endpoint | Parâmetros / Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/historicos/pet/{petId}` | `@PathVariable Long petId` | `List<HistoricoResponse>` | Lista eventos de saúde do pet ordenados por data decrescente. |
| `GET` | `/historicos/{id}` | `@PathVariable Long id` | `HistoricoResponse` | Busca evento de histórico por ID. |
| `POST` | `/historicos` | `HistoricoRequest` | `HistoricoResponse` (201 Created) | Registra evento de histórico (Vacina, Consulta, Exame, etc.). |
| `PUT` | `/historicos/{id}` | `HistoricoRequest` | `HistoricoResponse` (200 OK) | Atualiza registro de histórico. |
| `DELETE`| `/historicos/{id}` | `@PathVariable Long id` | 204 No Content | Remove registro de histórico. |

---

### 🎓 Trilhas de Aprendizado (`/trilhas`) - ⭐ EXCLUSIVO PREMIUM
| Método | Endpoint | Parâmetros / Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/trilhas/pet/{petId}` | `@PathVariable Long petId` | `List<TrilhaResponse>` | Lista trilhas cadastradas para o pet. |
| `GET` | `/trilhas/{id}` | `@PathVariable Long id` | `TrilhaResponse` | Busca trilha por ID. |
| `POST` | `/trilhas` | `TrilhaRequest` | `TrilhaResponse` (201 Created) | Cria nova trilha para o pet. |
| `PUT` | `/trilhas/{id}` | `TrilhaRequest` | `TrilhaResponse` (200 OK) | Atualiza trilha existente. |
| `DELETE`| `/trilhas/{id}` | `@PathVariable Long id` | 204 No Content | Deleta uma trilha e seus módulos/aulas em cascata. |

---

### 📦 Módulos das Trilhas (`/modulos`) - ⭐ EXCLUSIVO PREMIUM
| Método | Endpoint | Parâmetros / Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/modulos/trilha/{trilhaId}` | `@PathVariable Long trilhaId` | `List<ModuloResponse>` | Lista módulos pertencentes a uma trilha. |
| `GET` | `/modulos/{id}` | `@PathVariable Long id` | `ModuloResponse` | Busca módulo por ID. |
| `POST` | `/modulos` | `ModuloRequest` | `ModuloResponse` (201 Created) | Cria novo módulo associado a uma trilha. |
| `PUT` | `/modulos/{id}` | `ModuloRequest` | `ModuloResponse` (200 OK) | Atualiza módulo existente. |
| `DELETE`| `/modulos/{id}` | `@PathVariable Long id` | 204 No Content | Deleta módulo e suas aulas em cascata. |

---

### 📝 Aulas e Conteúdos Educativos (`/aulas`) - ⭐ EXCLUSIVO PREMIUM
| Método | Endpoint | Parâmetros / Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/aulas/modulo/{moduloId}` | `@PathVariable Long moduloId` | `List<AulaResponse>` | Lista aulas pertencentes a um módulo. |
| `GET` | `/aulas/{id}` | `@PathVariable Long id` | `AulaResponse` | Busca aula por ID. |
| `POST` | `/aulas` | `AulaRequest` | `AulaResponse` (201 Created) | Cria nova aula (com pontuação, conteúdo NOT NULL até 1000 caracteres e concluida = false). |
| `PUT` | `/aulas/{id}` | `AulaRequest` | `AulaResponse` (200 OK) | Atualiza aula existente (ex: marcar `concluida = true`). |
| `DELETE`| `/aulas/{id}` | `@PathVariable Long id` | 204 No Content | Deleta uma aula. |

---

### 📍 Endereços (`/enderecos`)
| Método | Endpoint | Parâmetros / Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/enderecos` | `Pageable` | `Page<EnderecoResponse>` | Lista endereços cadastrados. |
| `GET` | `/enderecos/{id}` | `@PathVariable Long id` | `EnderecoResponse` | Busca endereço por ID. |
| `POST` | `/enderecos` | `EnderecoRequest` | `EnderecoResponse` (201 Created) | Resolve endereço via ViaCEP e persiste hierarquia geográfica. |
| `PUT` | `/enderecos/{id}` | `EnderecoRequest` | `EnderecoResponse` (200 OK) | Atualiza endereço por ID. |
| `DELETE`| `/enderecos/{id}` | `@PathVariable Long id` | 204 No Content | Deleta endereço. |

---

## 🛡️ 6. Padrões de Validação no Projeto

### A. Validação de Formato e Contrato (DTOs - Bean Validation)
- `@NotBlank`, `@NotNull`: Campos obrigatórios.
- `@PastOrPresent`: Data de nascimento do Pet (`dataNasc`).
- `@FutureOrPresent`: Prazo de tarefas (`prazo`).
- `@Positive`: Pontos de tarefas e aulas.
- `@Email`: Formato do e-mail.
- `@Size(max = ...)`: Limites de tamanho de string.
- `@DddValidation` / `@DddValidator`: Valida DDD válido no Brasil.
- `@CepValidation` / `@CepValidator`: Valida formato numérico de 8 dígitos de CEP.
- `@EnumValidation` / `@EnumValidator`: Valida enums dinâmicos (`PetPorte`, `EnumStatus`, `UsuarioRole`).
- `@DiferentesUsuariosValidation` / `@DiferentesUsuariosValidator`: Garante `responsavelAtualId != novoResponsavelId`.

### B. Validação de Regras de Negócio (Domain / Service Components)
- **`TarefaValidator`**: Valida se o criador/executor é cuidador do pet e se a tarefa está apta para conclusão.
- **`UsuarioPetValidator`**: Valida titularidade única de responsável principal, vínculo prévio e desvinculação no Care Circle.

### C. Tratamento Global de Erros (`GlobalExceptionHandler`)
- `400 Bad Request`: `MethodArgumentNotValidException`, `IllegalArgumentException`, `HttpMessageNotReadableException`, `DataIntegrityViolationException`.
- `401 Unauthorized`: `AuthenticationException`.
- `403 Forbidden`: `AccessDeniedException` (acesso negado para rotas Premium ou operações não autorizadas).
- `404 Not Found`: `ResourceNotFoundException`.
- `500 Internal Server Error`: Erros inesperados.

---

## 📏 7. Convenções de Código do Projeto

1. **Sem `Locale.ROOT`:** Utilizar `.toUpperCase()` ou `.toLowerCase()` padrão.
2. **Uso de `toEntity()` nos DTOs:** Métodos `update` nos Services utilizam `request.toEntity(...)`, `entity.setId(id)` e `repository.save(entity)`.
3. **Inicialização com `@Builder.Default`:** Coleções e campos booleanos sempre inicializados.
4. **DTOs Limpos:** Records de DTO contêm apenas anotações essenciais de validação, sem `@Schema`.
5. **Sem Verificações Redundantes de Null:** Não incluir checagens manuais de `null` ou ternários defensivos em atributos que já possuem valor padrão na entidade (como `role = UsuarioRole.COMUM`) ou que são obrigatórios via `@NotNull`/`@NotBlank`.
