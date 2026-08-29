# 🐾 AGENT.md - PetGuardian API (Java-Advanced)

Guia completo e documentação técnica da arquitetura, regras de negócio, endpoints, validações e padrões de implementação do projeto **PetGuardian** (`Java-Advanced`).

---

## 🏛️ 1. Visão Geral da Arquitetura & Stack

- **Java Version:** Java 17 LTS
- **Framework:** Spring Boot 3.4.3
- **Persistência & ORM:** Spring Data JPA + Hibernate (com `ddl-auto=validate`)
- **Migrações de Banco:** Flyway (`org.flywaydb:flyway-core`) com scripts em `src/main/resources/db/migration/`
- **Segurança:** Spring Security + OAuth2 Resource Server com tokens JWT assinados via par de chaves assimétricas RSA (PKCS#8).
- **Cache:** Spring Starter Cache (cache em memória para lookups de Status).
- **Documentação de API:** SpringDoc OpenAPI 3 (`/swagger-ui.html` e `/v3/api-docs`).
- **Banco de Dados:** H2 Database (em memória para desenvolvimento/testes rápidos) e compatibilidade com Oracle Database / PostgreSQL.

---

## 🔐 2. Autenticação & Segurança (Spring Security + RSA JWT)

- **Fluxo Stateless:** Todas as rotas autenticadas utilizam tokens Bearer JWT no header `Authorization: Bearer <token>`.
- **Rotas Públicas (`permitAll`):**
  - `POST /login` (autenticação de tutores)
  - `POST /usuarios` (cadastro de novos tutores)
  - `/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html`
  - `/h2-console/**` (com suporte a frameOptions `sameOrigin`)
  - `/actuator/health`, `/actuator/info`
- **Token JWT:**
  - Emissor: `pet-guardian`
  - Expiração: 1 hora a partir da emissão.
  - Claims: `role` (ex: `ROLE_USER`), `email`, `sub` (e-mail do usuário).

---

## 🌐 3. Catálogo Completo de Endpoints

### 🔑 Autenticação (`/login`)
| Método | Endpoint | Request Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/login` | `LoginRequest` (`email`, `senha`) | `LoginResponse` (`token`, `UsuarioResponse`) | Autentica o usuário e retorna o token JWT e o perfil. |

---

### 👤 Usuários (`/usuarios`)
| Método | Endpoint | Parâmetros / Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/usuarios` | `Pageable` (`page`, `size`, `sort`) | `Page<UsuarioResponse>` | Lista usuários paginados (ordenados por nome). |
| `GET` | `/usuarios/by-nome` | `@RequestParam String nome`, `Pageable` | `Page<UsuarioResponse>` | Busca usuários por trecho do nome (case-insensitive). |
| `GET` | `/usuarios/by-email` | `@RequestParam String email` | `UsuarioResponse` | Busca usuário por e-mail exato. |
| `GET` | `/usuarios/{id}` | `@PathVariable Long id` | `UsuarioResponse` | Busca usuário por ID. |
| `GET` | `/usuarios/{id}/rede-cuidado` | `@PathVariable Long id` | `RedeCuidadoResponse` | Retorna o Care Circle consolidado (pets, co-cuidadores e tarefas). |
| `POST` | `/usuarios` | `UsuarioRequest` | `UsuarioResponse` (201 Created) | Cadastra um novo usuário, telefone e endereço (via CEP). |
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
| `POST` | `/tarefas` | `TarefaRequest` | `TarefaResponse` (201 Created) | Cria nova tarefa com status `PENDENTE`. |
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

### 🎓 Trilhas de Aprendizado (`/trilhas`)
| Método | Endpoint | Parâmetros / Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/trilhas/pet/{petId}` | `@PathVariable Long petId` | `List<TrilhaResponse>` | Lista trilhas cadastradas para o pet. |
| `GET` | `/trilhas/{id}` | `@PathVariable Long id` | `TrilhaResponse` | Busca trilha por ID. |
| `POST` | `/trilhas` | `TrilhaRequest` | `TrilhaResponse` (201 Created) | Cria nova trilha para o pet. |
| `PUT` | `/trilhas/{id}` | `TrilhaRequest` | `TrilhaResponse` (200 OK) | Atualiza trilha existente. |
| `DELETE`| `/trilhas/{id}` | `@PathVariable Long id` | 204 No Content | Deleta uma trilha e seus módulos/aulas em cascata. |

---

### 📦 Módulos das Trilhas (`/modulos`)
| Método | Endpoint | Parâmetros / Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/modulos/trilha/{trilhaId}` | `@PathVariable Long trilhaId` | `List<ModuloResponse>` | Lista módulos pertencentes a uma trilha. |
| `GET` | `/modulos/{id}` | `@PathVariable Long id` | `ModuloResponse` | Busca módulo por ID. |
| `POST` | `/modulos` | `ModuloRequest` | `ModuloResponse` (201 Created) | Cria novo módulo associado a uma trilha. |
| `PUT` | `/modulos/{id}` | `ModuloRequest` | `ModuloResponse` (200 OK) | Atualiza módulo existente. |
| `DELETE`| `/modulos/{id}` | `@PathVariable Long id` | 204 No Content | Deleta módulo e suas aulas em cascata. |

---

### 📝 Aulas e Conteúdos Educativos (`/aulas`)
| Método | Endpoint | Parâmetros / Body | Response | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/aulas/modulo/{moduloId}` | `@PathVariable Long moduloId` | `List<AulaResponse>` | Lista aulas pertencentes a um módulo. |
| `GET` | `/aulas/{id}` | `@PathVariable Long id` | `AulaResponse` | Busca aula por ID. |
| `POST` | `/aulas` | `AulaRequest` | `AulaResponse` (201 Created) | Cria nova aula (com pontuação e conteúdo instrutivo). |
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

## 🧠 4. Lógica de Negócio dos Serviços

1. **`UsuarioService`**:
   - Criação de Usuário: normaliza e-mail com `trim().toLowerCase()`, codifica senha com BCrypt (`PasswordEncoder`), cria/recupera `Telefone` e busca/cria `Endereco` completo a partir do CEP consumido pelo `RestClient` do ViaCEP.
2. **`PetService`**:
   - Cadastro e Atualização de Pet: gerencia a data de nascimento (`LocalDate dataNasc`), busca ou cria a `Raca` automaticamente pelo nome normalizado e vincula o tutor criador como `responsavelPrincipal = true`.
   - **Pontuação Consolidada (`calcularPontuacaoTotalPet`)**: soma consultas agregadas do `TarefaRepository` (tarefas `CONCLUIDO`) e do `AulaRepository` (aulas `concluida = true` associadas à trilha do pet).
3. **`TarefaService`**:
   - **Expiração Automática:** Em cada consulta ou conclusão, executa `expirarTarefasPendentesAtrasadas` via query `@Modifying` atualizando tarefas vencidas (`prazo < agora`) de `PENDENTE` para `EXPIRADO`.
   - **Conclusão:** Valida se a tarefa está pendente, se o concluinte é membro do Care Circle do Pet, registra a data de conclusão e atribui o executor.
4. **`EnderecoService`**:
   - Integração com ViaCEP via Spring 6 `RestClient`: monta a cadeia geográfica normalizada `Estado` $\rightarrow$ `Cidade` $\rightarrow$ `Bairro` $\rightarrow$ `Endereco`, reutilizando instâncias existentes com `findBy...` para evitar duplicações no banco.

---

## 👥 5. Como funciona o `UsuarioPet` (Care Circle / Rede de Cuidado)

O `UsuarioPet` é a entidade central para o compartilhamento de responsabilidades sobre um animal:

1. **Mapeamento JPA com Chave Composta:**
   - Possui `@EmbeddedId UsuarioPetId id` com `usuarioId` e `petId`.
   - Mapeado com `@MapsId("usuarioId")` e `@MapsId("petId")` para vincular bidirecionalmente `Usuario` e `Pet`.
2. **Responsável Principal (`respon_princ`):**
   - Cada Pet possui **exatamente um** responsável principal ativo (`responsavelPrincipal = true`).
   - Os demais participantes são co-cuidadores (`responsavelPrincipal = false`).
3. **Regras de Negócio (`UsuarioPetValidator`):**
   - **Convite:** Apenas o `responsavelPrincipal` atual pode convidar novos membros para a rede do animal. Não é permitido convidar um usuário que já faz parte da rede.
   - **Desvinculação:** O responsável principal não pode ser removido sem antes transferir a titularidade. Um co-cuidador pode sair voluntariamente ou ser removido pelo responsável principal.
   - **Transferência de Titularidade:** Apenas o responsável principal atual pode transferir seu cargo para outro co-cuidador já vinculado ao pet (validação garantida também por `@DiferentesUsuariosValidation`).
4. **Visualização Agregada (`RedeCuidadoMapper`):**
   - Constrói o `RedeCuidadoResponse` listando todos os pets do tutor, co-cuidadores de cada pet, contagem de tarefas pendentes/concluídas e pontos acumulados.

---

## 🛡️ 6. Padrões de Validação no Projeto

O projeto adota uma arquitetura em duas camadas de validação:

### A. Validação de Formato e Contrato (DTOs - Bean Validation)
Anotações nativas e personalizadas sem mensagens embutidas (mensagens padronizadas pelo Spring Boot):
- `@NotBlank`, `@NotNull`: Campos obrigatórios.
- `@PastOrPresent`: Data de nascimento do Pet (`dataNasc`).
- `@FutureOrPresent`: Prazo de criação/execução de tarefas (`prazo`).
- `@Positive`: Pontos de tarefas e aulas.
- `@Email`: Validação de padrão de e-mail.
- `@Size(max = ...)`: Limites de tamanho de caracteres (ex: `conteudo` até 1000).
- `@DddValidation` / `@DddValidator`: Valida se o DDD informado é um código telefônico válido no Brasil.
- `@CepValidation` / `@CepValidator`: Valida se o CEP possui formato numérico de 8 dígitos.
- `@EnumValidation` / `@EnumValidator`: Valida dinamicamente se o valor pertence ao Enum informado (ex: `PetPorte`, `EnumStatus`).
- `@DiferentesUsuariosValidation` / `@DiferentesUsuariosValidator`: Garante que `responsavelAtualId` $\neq$ `novoResponsavelId` na transferência de titularidade.

### B. Validação de Regras de Negócio (Spring Components)
- **`TarefaValidator`**: Valida prazos futuros na criação, permissões de cuidadores e transições de status válidas para conclusão.
- **`UsuarioPetValidator`**: Valida permissões de titularidade, vínculo prévio e desvinculação no Care Circle.

### C. Tratamento Global de Erros (`GlobalExceptionHandler`)
Captura e serializa respostas uniformes em JSON (`ApiErrorResponse`):
- `400 Bad Request`: `MethodArgumentNotValidException` (detalhes dos campos), `IllegalArgumentException`, `HttpMessageNotReadableException`, `DataIntegrityViolationException`.
- `401 Unauthorized`: `AuthenticationException` (credenciais inválidas ou token ausente/expirado).
- `403 Forbidden`: `AccessDeniedException` (permissão insuficiente).
- `404 Not Found`: `ResourceNotFoundException`.
- `500 Internal Server Error`: Erros não mapeados.

---

## 📏 7. Convenções de Código do Projeto

1. **Sem `Locale.ROOT`:** Utilizar `.toUpperCase()` ou `.toLowerCase()` padrão.
2. **Uso de `toEntity()` nos DTOs:** Métodos `update` nos Services utilizam `request.toEntity(...)`, `entity.setId(id)` e `repository.save(entity)`, eliminando chamadas repetitivas de múltiplos setters.
3. **Inicialização com `@Builder.Default`:** Todos os campos booleanos e coleções (`Set<...> = new HashSet<>()`) em entidades JPA são anotados com `@Builder.Default` e inicializados, evitando `NullPointerException`.
4. **DTOs Limpos:** Records de DTO contêm apenas as anotações essenciais de validação, sem `@Schema` nem `message = "..."` redundantes.
