# 📋 Backlog Azure Boards — Sprint 3: Java Advanced (Spring Boot)
> **Projeto:** PetGuardian (Challenge FIAP 2026 - Clyvo Vet)  
> **Disciplina:** Advanced Business Development with Java (Java Advanced)  
> **Epic Principal:** `[EPIC] PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`  
> **Start Date:** `2026-08-24`  
> **Target Date:** `2026-08-29`  
> **Padrão:** Azure DevOps / Azure Boards (Scrum Process Template: *Epic ➔ Feature ➔ PBI ➔ Child Tasks*)  
> **Base de Requisitos:** Diretrizes Oficiais FIAP & Mentoria Clyvo (Arquitetura Pet-Centric & Rede de Co-Cuidadores)  
> **Sequência Estratégica:** 1º Refatoração & Qualidade ➔ 2º Segurança & JWT ➔ 3º Versionamento de Banco (Flyway) ➔ 4º Fluxos Especializados de Negócio ➔ 5º Documentação & Entrega

---

## 🎯 1. Resumo Executivo & Mapeamento de Requisitos da Sprint 3

| Requisito Oficial | Peso / Pontos | Status no Backlog | Destaque Técnico & Mentoria Clyvo |
| :--- | :---: | :---: | :--- |
| **1. Refatoração SOLID, DRY & Clean Code** | **Penalidades (-10 a -15 pts)** | **Concluído (PBI-01 a PBI-04)** | Injeção por construtor (`@RequiredArgsConstructor`), eliminação total de `ResponseStatusException`, Global Exception Handler, DTOs desacoplados com fábrica `toEntity()`/`fromEntity()`, `@Transactional` seletivo, tipos primitivos `boolean` e SpringDoc OpenAPI/Swagger. |
| **2. Spring Security & Controle de Acesso** | **30 pts** | **Crítico (PBI-05 a PBI-08)** | Autenticação via Spring Security OAuth2 Resource Server com par de chaves assimétricas **RSA 2048-bit** (`NimbusJwtEncoder` / `NimbusJwtDecoder`), BCrypt, tokens JWT Stateless para consumo Mobile (React Native), `CorsConfig` dedicado e proteção centralizada via `SecurityFilterChain`. |
| **3. Flyway (Controle de Versão de BD)** | **20 pts** | **Crítico (PBI-09 e PBI-10)** | Migrações versionadas (`V1`, `V2`, `V3`) com tabelas Pet-Centric: `usuario`, `pet`, `usuario_pet` (gestão N:N familiar com PK composta `@EmbeddedId`), `tarefa`, `status`, `endereco`, `bairro`, `cidade`, `estado`, `telefone`. |
| **4. Funcionalidades Completas (Fluxos Não-CRUD)** | **20 pts** | **Crítico (PBI-11 a PBI-14)** | **Dois fluxos ponta a ponta complexos:**<br>1) *Rede Familiar & Co-Cuidadores:* Vínculo N:N entre tutores e pets com `UsuarioPetController` (`/pets/{petId}/cuidadores`), gestão de Responsável Principal, convite de co-cuidadores exclusivamente por **e-mail**, transferência de titularidade, agregação performática da Rede de Cuidado (`RedeCuidadoMapper` com batch query anti-N+1) e histórico consolidado do pet.<br>2) *Rotina Familiar, Expiração Automática & Gamificação:* Tutores criam rotinas com prazo e pontuação, mecanismo atômico de expiração automática de tarefas pendentes vencidas via query `@Modifying`, conclusão com atribuição de cuidador executor e cálculo de ranking/pontuação. |
| **5. Documentação, Vídeo (10 min) & Avaliação Oral** | **Obrigatório** | **Alto (PBI-15 a PBI-17)** | README detalhado, gravação com demonstração das rotas/segurança/fluxos e guia de estudo para defesa individual com foco em decisões de arquitetura e Clean Code. |

---

## 👑 2. Hierarquia Geral do Backlog no Azure Boards

```text
[EPIC] PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)
│
├── 🧹 [FEATURE 01] Refatoração Arquitetural, SOLID & Tratamento de Exceções
│   ├── [PBI-01] Arquitetura de Exceções de Domínio e Global Exception Handler (3 pts)
│   └── [PBI-02] Refatoração dos Serviços de Negócio com Princípios SOLID (SRP, DIP & Injeção) (5 pts)
│
├── 📑 [FEATURE 02] Validação de Contratos (Bean Validation) & Documentação OpenAPI
│   ├── [PBI-03] Padronização de DTOs e Bean Validation Avançado (@Valid) (3 pts)
│   └── [PBI-04] Configuração e Documentação Interativa com SpringDoc OpenAPI 3 / Swagger (3 pts)
│
├── 🔐 [FEATURE 03] Autenticação e Gestão de Credenciais com JWT (Spring Security Core)
│   ├── [PBI-05] Arquitetura de Autenticação, UserDetails, BCrypt & Serviço de Tokens JWT RSA (5 pts)
│   └── [PBI-06] Endpoints de Autenticação (/login) e Registro (/usuarios) com Emissão de JWT (3 pts)
│
├── 🛡️ [FEATURE 04] Autorização, Filtro JWT Stateless e Proteção de Endpoints (RBAC)
│   ├── [PBI-07] Configuração do SecurityFilterChain Stateless, CORS Dedicado e Proteção Centralizada (5 pts)
│   └── [PBI-08] Segurança Granular por Método (@EnableMethodSecurity) e Handlers 401/403 (3 pts)
│
├── 🏛️ [FEATURE 05] Versionamento de Banco de Dados & Governança de Esquema (Flyway)
│   ├── [PBI-09] Configuração do Flyway e Migração Inicial DDL Pet-Centric (V1) (5 pts)
│   └── [PBI-10] Migrações Incrementais de Domínio, Status e Seeds de Segurança (V2 e V3) (3 pts)
│
├── 🐾 [FEATURE 06] Fluxo 1: Gestão de Pets, Rede Familiar e Co-Cuidadores (N:N)
│   ├── [PBI-11] [Fluxo 1.1] Gestão de Vínculos N:N, Responsável Principal e Convite de Co-Cuidadores por E-mail (5 pts)
│   └── [PBI-12] [Fluxo 1.2] Visualização Agregada da Rede de Cuidado (RedeCuidadoMapper) e Histórico do Pet (5 pts)
│
├── 🎮 [FEATURE 07] Fluxo 2: Gestão da Rotina do Pet, Expiração Atômica & Gamificação
│   ├── [PBI-13] [Fluxo 2.1] Criação de Rotina Familiar e Mecanismo Atômico de Expiração Automática (5 pts)
│   └── [PBI-14] [Fluxo 2.2] Conclusão de Tarefas, Registro de Executor e Sistema de Pontuação (5 pts)
│
└── 📹 [FEATURE 08] Documentação Técnica, Demonstração em Vídeo & Preparação para Banca Oral
    ├── [PBI-15] Atualização Completa do README.md e Guia de Execução (Gradle) (3 pts)
    ├── [PBI-16] Roteiro e Gravação do Vídeo Demonstrativo da Aplicação (Máx. 10 min) (5 pts)
    └── [PBI-17] Matriz de Argumentação e Preparação para a Avaliação Oral Individual (3 pts)
```

---

## 📦 3. Detalhamento por Feature, PBIs e Child Tasks

---

### 🧹 FEATURE 01: Refatoração Arquitetural, SOLID & Tratamento de Exceções
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `SOLID`, `CleanCode`, `Refactor`, `Exceptions`
* **Start Date:** `2026-08-24`
* **Target Date:** `2026-08-25`
* **Descrição:** Refatoração da camada de serviços e tratamento de erros para cumprir os princípios de Clean Code, SRP e DIP, eliminando `ResponseStatusException` solta, unificando o tratamento de exceções de domínio no `GlobalExceptionHandler` e aplicando `@Transactional` seletivo.

#### 🔹 [PBI-01] Arquitetura de Exceções de Domínio e Global Exception Handler
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Refatoração Arquitetural, SOLID & Tratamento de Exceções`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `Exceptions`, `GlobalExceptionHandler`, `ControllerAdvice`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Desenvolvedor Backend,  
> **Eu quero** criar uma hierarquia de exceções de domínio expressivas e unificar sua captura em um `GlobalExceptionHandler` (`@RestControllerAdvice`),  
> **Para que** todas as falhas de negócio, validação e integridade retornem respostas HTTP estruturadas e previsíveis.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Exceção de domínio `ResourceNotFoundException` para recursos inexistentes (404 Not Found).
- [ ] DTO padrão `ApiErrorResponse` contendo `timestamp`, `status`, `error`, `message` e `path`.
- [ ] DTO `ValidationErrorDetail` com lista de erros de validação de formulários (`@Valid`).
- [ ] `GlobalExceptionHandler` intercepta `MethodArgumentNotValidException`, `ResourceNotFoundException`, `IllegalArgumentException`, `HttpMessageNotReadableException` e `DataIntegrityViolationException` sem expor stacktrace interno.

##### Tarefas Técnicas (Child Tasks)
* **Task 1.1:** Criar classes de exceção de domínio e DTOs de resposta padronizada de erro (`ApiErrorResponse`). *(Estimativa: 1.5h)*
* **Task 1.2:** Implementar `@RestControllerAdvice` no `GlobalExceptionHandler` mapeando todos os tipos de erro HTTP (400, 404, 500). *(Estimativa: 2.0h)*
* **Task 1.3:** Testar o tratamento de erros para garantir consistência de resposta em todos os endpoints. *(Estimativa: 1.0h)*

---

#### 🔹 [PBI-02] Refatoração dos Serviços de Negócio com Princípios SOLID (SRP, DIP & Injeção)
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Refatoração Arquitetural, SOLID & Tratamento de Exceções`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `SOLID`, `CleanCode`, `SRP`, `DIP`, `Lombok`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Desenvolvedor Backend,  
> **Eu quero** refatorar os Services para eliminar acoplamento indevido, métodos longos e anotações redundantes,  
> **Para que** o código respeite os princípios SOLID (especialmente SRP e DIP), injeção por construtor com Lombok e utilize transações de forma atômica e seletiva.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Injeção de dependências estritamente por construtor utilizando `@RequiredArgsConstructor` (Lombok) com atributos `private final` nos Services (`PetService`, `UsuarioService`, `UsuarioPetService`, `TarefaService`, `EnderecoService`, `StatusService`).
- [ ] Eliminação de indireções e métodos privados duplicados de busca (`findById` direto).
- [ ] Padronização de tipos primitivos `boolean` (`responsavelPrincipal`, `castrado`) para evitar verificações defensivas redundantes de `null`.
- [ ] Anotação `@Transactional` mantida estritamente em métodos de escrita e `@Transactional(readOnly = true)` em consultas.

##### Tarefas Técnicas (Child Tasks)
* **Task 2.1:** Refatorar `UsuarioService` e `PetService` aplicando `@RequiredArgsConstructor` e regras atômicas de transação. *(Estimativa: 2.5h)*
* **Task 2.2:** Refatorar `TarefaService` e `EnderecoService` aplicando modularização de regras, integração ViaCEP e `@Transactional` seletivo. *(Estimativa: 2.5h)*
* **Task 2.3:** Auditar o código-fonte eliminando queries não utilizadas, `if` redundantes e classes mortas. *(Estimativa: 1.5h)*

---

### 📑 FEATURE 02: Validação de Contratos (Bean Validation) & Documentação OpenAPI
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `BeanValidation`, `OpenAPI`, `Swagger`, `Contracts`
* **Start Date:** `2026-08-24`
* **Target Date:** `2026-08-25`
* **Descrição:** Padronização de DTOs de entrada e saída com Bean Validation avançado, isolamento das entidades JPA e documentação interativa completa da API utilizando SpringDoc OpenAPI 3 / Swagger UI.

#### 🔹 [PBI-03] Padronização de DTOs e Bean Validation Avançado (@Valid)
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Validação de Contratos (Bean Validation) & Documentação OpenAPI`
* **State:** `Approved`
* **Priority:** `2 - High`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `BeanValidation`, `DTO`, `Validation`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Desenvolvedor Mobile consumidor da API,  
> **Eu quero** que todas as requisições de entrada sejam estritamente validadas via Bean Validation e os Responses usem DTOs desacoplados,  
> **Para que** dados incorretos sejam rejeitados no Controller com mensagens amigáveis e não ocorra vazamento de entidades JPA.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] DTOs de Request anotados com constraints precisas (`@NotBlank`, `@NotNull`, `@Size`, `@Email`, `@FutureOrPresent`, `@Positive`, `@Pattern`).
- [ ] Validadores customizados: `@CepValidation`, `@DddValidation`, `@EnumValidation` e `@DiferentesUsuariosValidation`.
- [ ] Métodos de fábrica `toEntity()` embutidos nos DTOs de Request e `fromEntity()` nos DTOs de Response.
- [ ] DTOs de Response desacoplados das entidades (`UsuarioResponse`, `PetResponse`, `TarefaResponse`, `EnderecoResponse`, `RedeCuidadoResponse`, `CoCuidadorResponse`).
- [ ] Anotação `@Valid` aplicada em todos os parâmetros `@RequestBody` nos métodos dos Controllers.

##### Tarefas Técnicas (Child Tasks)
* **Task 3.1:** Revisar e aplicar Bean Validation completo em `PetRequest`, `UsuarioRequest`, `TarefaRequest`, `TarefaConclusaoRequest`, `CoCuidadorRequest` e `EnderecoRequest`. *(Estimativa: 2.0h)*
* **Task 3.2:** Garantir que todos os Responses utilizem DTOs Records imutáveis e testar respostas 400 Bad Request. *(Estimativa: 1.5h)*

---

#### 🔹 [PBI-04] Configuração e Documentação Interativa com SpringDoc OpenAPI 3 / Swagger
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Validação de Contratos (Bean Validation) & Documentação OpenAPI`
* **State:** `Approved`
* **Priority:** `2 - High`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `Swagger`, `OpenAPI`, `SpringDoc`, `Documentation`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Desenvolvedor e Integrador da API,  
> **Eu quero** acessar a documentação interativa Swagger UI da aplicação,  
> **Para que** eu possa testar todos os endpoints, parâmetros e validar os contratos de dados diretamente pelo navegador.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Dependência `org.springdoc:springdoc-openapi-starter-webmvc-ui` configurada no `build.gradle`.
- [ ] `SwaggerConfig` configurada com metadados do projeto PetGuardian.
- [ ] Controllers documentados com anotações `@Tag`, `@Operation` e `@ResponseStatus`.
- [ ] Swagger UI acessível e funcional em `/swagger-ui/index.html` e `/v3/api-docs`.

##### Tarefas Técnicas (Child Tasks)
* **Task 4.1:** Configurar metadados do OpenAPI em `SwaggerConfig`. *(Estimativa: 1.5h)*
* **Task 4.2:** Enriquecer `PetController`, `UsuarioController`, `UsuarioPetController`, `TarefaController` e `EnderecoController` com `@Tag` e `@Operation`. *(Estimativa: 2.0h)*
* **Task 4.3:** Validar documentação interativa e execução dos endpoints no Swagger UI. *(Estimativa: 1.0h)*

---

### 🔐 FEATURE 03: Autenticação e Gestão de Credenciais com JWT (Spring Security Core)
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `Security`, `Authentication`, `BCrypt`, `JWT`, `RSA2048`, `MobileIntegration`
* **Start Date:** `2026-08-25`
* **Target Date:** `2026-08-26`
* **Descrição:** Estruturação da arquitetura de autenticação com Spring Security e OAuth2 Resource Server com par de chaves assimétricas RSA 2048-bit, criptografia BCrypt, UserDetailsService e emissão de tokens JWT para consumo pelo aplicativo Mobile (React Native).

#### 🔹 [PBI-05] Arquitetura de Autenticação, UserDetails, BCrypt & Serviço de Tokens JWT RSA
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Autenticação e Gestão de Credenciais com JWT (Spring Security Core)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `SpringSecurity`, `BCrypt`, `UserDetails`, `JWT`, `RSA`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Arquiteto de Software Backend,  
> **Eu quero** configurar o Spring Security com `BCryptPasswordEncoder`, par de chaves RSA 2048-bit (`NimbusJwtEncoder`/`NimbusJwtDecoder`), `UserDetailsService` e um serviço emissor de tokens JWT (`TokenService`),  
> **Para que** as credenciais sejam salvas com hash seguro e a aplicação emita tokens JWT assinados digitalmente para autenticar o app Mobile.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Dependências `spring-boot-starter-security` e `spring-boot-starter-oauth2-resource-server` configuradas no `build.gradle`.
- [ ] Par de chaves RSA de 2048 bits (`private_key.pem`, `public_key.pem`) gerado em `src/main/resources/keys/` e injetado via `@ConfigurationProperties(prefix = "rsa")`.
- [ ] Bean `PasswordEncoder` configurado com `BCryptPasswordEncoder` e coluna de senha no `Usuario` dimensionada com segurança.
- [ ] `TokenService` implementado gerando tokens JWT assinados com tempo de expiração, claims (`role`, `sub: email`) e issuer `petguardian-api`.
- [ ] Serviço `AuthService` implementando `UserDetailsService` para busca de usuário por e-mail no banco de dados.

##### Tarefas Técnicas (Child Tasks)
* **Task 5.1:** Configurar Gradle com dependências de segurança e gerar par de chaves RSA 2048-bit. *(Estimativa: 1.5h)*
* **Task 5.2:** Implementar `AuthService` (`UserDetailsService`) e configurar `BCryptPasswordEncoder`. *(Estimativa: 2.0h)*
* **Task 5.3:** Desenvolver `TokenService` com `JwtEncoder` baseado em chaves assimétricas RSA. *(Estimativa: 2.0h)*

---

#### 🔹 [PBI-06] Endpoints de Autenticação (/login) e Registro (/usuarios) com Emissão de JWT
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Autenticação e Gestão de Credenciais com JWT (Spring Security Core)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `AuthEndpoints`, `Login`, `Register`, `JWT`, `MobileAPI`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Usuário do aplicativo Mobile (Tutor ou Cuidador),  
> **Eu quero** realizar login através do endpoint `/login` e cadastro via `POST /usuarios`,  
> **Para que** eu receba um token JWT válido e os dados do meu perfil (`user`) no payload para manter a sessão ativa no aplicativo.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Endpoint `POST /login` no `AuthController` recebendo `LoginRequest` (`email`, `senha`) com `@NotBlank` e `@Email`.
- [ ] `LoginResponse` contendo `token` e o objeto `user` (`UsuarioResponse`) para armazenamento direto no `AsyncStorage` do Mobile.
- [ ] `POST /usuarios` criptografa a senha com BCrypt via `UsuarioService` e persiste o usuário com seus dados de endereço e telefone.
- [ ] Retorno de status HTTP 401 para credenciais inválidas e HTTP 400 para erros de validação de payload.

##### Tarefas Técnicas (Child Tasks)
* **Task 6.1:** Desenvolver DTOs `LoginRequest` e `LoginResponse` no `AuthController`. *(Estimativa: 1.0h)*
* **Task 6.2:** Implementar autenticação via `AuthenticationManager` e delegar busca de dados do usuário para `UsuarioService`. *(Estimativa: 2.0h)*
* **Task 6.3:** Testar cenários de login válido, senha incorreta e persistência de senhas com hash BCrypt. *(Estimativa: 1.0h)*

---

### 🛡️ FEATURE 04: Autorização, Filtro JWT Stateless e Proteção de Endpoints (RBAC)
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `Security`, `RBAC`, `SecurityFilterChain`, `JWT`, `CorsConfig`
* **Start Date:** `2026-08-25`
* **Target Date:** `2026-08-26`
* **Descrição:** Configuração da cadeia de filtros `SecurityFilterChain` em modo STATELESS com decodificação automática de JWT via OAuth2 Resource Server, suporte a CORS isolado no `CorsConfig` para o aplicativo Mobile (React Native) e política de segurança centralizada *Secure by Default*.

#### 🔹 [PBI-07] Configuração do SecurityFilterChain Stateless, CORS Dedicado e Proteção Centralizada
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Autorização, Filtro JWT Stateless e Proteção de Endpoints (RBAC)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `SecurityFilterChain`, `Stateless`, `OAuth2ResourceServer`, `CORS`, `Mobile`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Arquiteto de Software e Administrador de Segurança,  
> **Eu quero** configurar o `SecurityFilterChain` em modo Stateless com decodificação JWT e classe `CorsConfig` dedicada,  
> **Para que** o aplicativo React Native consiga consumir a API enviando o header `Authorization: Bearer <token>` sem bloqueios de CORS ou sessão no servidor.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] `SecurityConfig` configurado com `SessionCreationPolicy.STATELESS`, CSRF desabilitado e `headers.frameOptions.sameOrigin` para o console H2.
- [ ] `CorsConfig` dedicado liberando métodos (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `OPTIONS`) e todos os headers.
- [ ] Configuração de Resource Server `.oauth2ResourceServer(oauth2 -> oauth2.jwt(...))` com conversão de claims para `ROLE_`.
- [ ] Whitelist de rotas públicas: `POST /login`, `POST /usuarios`, `/swagger-ui/**`, `/v3/api-docs/**`, `/h2-console/**`, `/actuator/**`.
- [ ] Todos os demais endpoints protegidos por padrão (`anyRequest().authenticated()`).

##### Tarefas Técnicas (Child Tasks)
* **Task 7.1:** Implementar classe `CorsConfig` no pacote `config`. *(Estimativa: 1.5h)*
* **Task 7.2:** Configurar `SecurityConfig` com `SecurityFilterChain` Stateless e OAuth2 Resource Server JWT. *(Estimativa: 2.5h)*
* **Task 7.3:** Validar requisições autenticadas com Bearer Token e bloqueio 401 para requisições não autenticadas. *(Estimativa: 1.0h)*

---

#### 🔹 [PBI-08] Segurança Granular por Método (@EnableMethodSecurity) e Handlers 401/403
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Autorização, Filtro JWT Stateless e Proteção de Endpoints (RBAC)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `MethodSecurity`, `EnableMethodSecurity`, `RBAC`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Desenvolvedor Backend,  
> **Eu quero** habilitar segurança por método com `@EnableMethodSecurity`,  
> **Para que** a aplicação suporte autorizações granulares e retorne payloads JSON claros quando requisições não autorizadas forem recebidas.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] `@EnableMethodSecurity` habilitado no `SecurityConfig`.
- [ ] `JwtAuthenticationConverter` mapeia o claim `role` para a autoridade `ROLE_USER` / `ROLE_ADMIN`.
- [ ] Tratamento padronizado de 401 Unauthorized e 403 Forbidden sem stacktrace exposto.

##### Tarefas Técnicas (Child Tasks)
* **Task 8.1:** Habilitar `@EnableMethodSecurity` e configurar `JwtAuthenticationConverter`. *(Estimativa: 2.0h)*
* **Task 8.2:** Validar respostas de erro 401 e 403 no Swagger e Postman. *(Estimativa: 1.5h)*

---

### 🏛️ FEATURE 05: Versionamento de Banco de Dados & Governança de Esquema (Flyway)
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `Flyway`, `Database`, `Governance`, `PetCentric`
* **Start Date:** `2026-08-26`
* **Target Date:** `2026-08-27`
* **Descrição:** Implementação de controle de versão de banco de dados relacional através do Flyway, estruturando tabelas Pet-Centric limpas (`usuario`, `pet`, `usuario_pet`, `tarefa`, `status`, `endereco`, `bairro`, `cidade`, `estado`, `telefone`).

#### 🔹 [PBI-09] Configuração do Flyway e Migração Inicial DDL Pet-Centric (V1)
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Versionamento de Banco de Dados & Governança de Esquema (Flyway)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `Spring-Boot`, `Flyway`, `DDL`, `PetCentric`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Desenvolvedor Backend da equipe PetGuardian,  
> **Eu quero** configurar o Flyway no ecossistema Spring Boot com banco H2 Database e criar o script DDL inicial versionado (`V1__criar_tabelas_iniciais_petcentric.sql`),  
> **Para que** todas as tabelas do sistema sejam criadas com integridade referencial, constraints limpas e console interativo `/h2-console` para testes com o app Mobile.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Dependências `org.flywaydb:flyway-core` e `com.h2database:h2` configuradas no `build.gradle`.
- [ ] Propriedades do Spring configuradas para habilitar o Flyway e H2 (`spring.datasource.url=jdbc:h2:mem:petguardian`, `spring.h2.console.enabled=true`, `spring.flyway.enabled=true`, `spring.jpa.hibernate.ddl-auto=validate`).
- [ ] Script `V1__criar_tabelas_iniciais_petcentric.sql` criado em `src/main/resources/db/migration/` contendo:
  - Tabela `estado`, `cidade`, `bairro`, `endereco`, `telefone`.
  - Tabela `usuario` e tabela de relacionamento `usuario_endereco`.
  - Tabela `raca` e tabela `pet` (`nome`, `idade`, `raca_id_raca`, `porte`, `sexo`, `castrado`).
  - Tabela `usuario_pet` com chave composta (`usuario_id_usuario`, `pet_id_pet`) e flag `respon_princ` (boolean).
  - Tabela `status` e tabela `tarefa` (`titulo`, `descricao`, `pontos_tarefa`, `criacao`, `prazo`, `conclusao`, `status_id_status`, `pet_id_pet`, `usuario_id_usuario`).
- [ ] Definição explícita de PKs, FKs, restrições `NOT NULL` e integridade referencial.
- [ ] Inicialização da aplicação executa a migração no H2 com sucesso e cria a tabela `flyway_schema_history`.
- [ ] Console do H2 acessível em `/h2-console` para inspeção visual dos dados.

##### Tarefas Técnicas (Child Tasks)
* **Task 9.1:** Configurar dependências do Flyway e H2 no `build.gradle` e `application.properties`. *(Estimativa: 1.5h)*
* **Task 9.2:** Elaborar script DDL completo `V1__criar_tabelas_iniciais_petcentric.sql` com todas as tabelas do domínio limpo. *(Estimativa: 3.5h)*
* **Task 9.3:** Testar a inicialização do Spring Boot validando a criação da tabela `flyway_schema_history` e acesso ao `/h2-console`. *(Estimativa: 1.0h)*

---

#### 🔹 [PBI-10] Migrações Incrementais de Domínio, Status e Seeds de Segurança (V2 e V3)
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Versionamento de Banco de Dados & Governança de Esquema (Flyway)`
* **State:** `Approved`
* **Priority:** `2 - High`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `Flyway`, `SeedData`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Desenvolvedor Backend e Administrador do Sistema,  
> **Eu quero** criar migrações incrementais do Flyway para inserir sementes de dados de domínio (status, raças, endereços e usuários de teste),  
> **Para que** a aplicação já inicie pronta para testes e demonstração imediata no Mobile.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Script `V2__popular_status_e_racas.sql` insere:
  - Status de tarefas (`PENDENTE`, `CONCLUIDO`, `EXPIRADO`).
  - Raças iniciais de cães e gatos.
- [ ] Script `V3__adicionar_perfis_e_usuarios_iniciais.sql` insere usuários de teste com senhas hash BCrypt, telefones, endereços, pets e vínculos familiares.
- [ ] Migrações são idempotentes e executam sequencialmente.

##### Tarefas Técnicas (Child Tasks)
* **Task 10.1:** Elaborar script `V2__popular_status_e_racas.sql` com constantes de status e raças. *(Estimativa: 1.5h)*
* **Task 10.2:** Elaborar script `V3__adicionar_perfis_e_usuarios_iniciais.sql` com seeds de usuários hash BCrypt e pets vinculados. *(Estimativa: 2.0h)*
* **Task 10.3:** Validar integridade referencial e execução sequencial no banco de dados. *(Estimativa: 1.0h)*

---

### 🐾 FEATURE 06: Fluxo 1: Gestão de Pets, Rede Familiar e Co-Cuidadores (N:N)
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `BusinessFlow`, `RedeCuidado`, `UsuarioPet`, `CoCuidadores`, `PetHistory`, `NonCRUD`
* **Start Date:** `2026-08-27`
* **Target Date:** `2026-08-28`
* **Descrição:** Implementação do primeiro fluxo de negócio complexo não-CRUD obrigatório: governança do Care Circle através do `UsuarioPetController` e `UsuarioPetService`, gestão de Responsável Principal, convite de co-cuidadores exclusivamente por e-mail, transferência de responsabilidade e agregação de alta performance da Rede de Cuidado com `RedeCuidadoMapper`.

#### 🔹 [PBI-11] [Fluxo 1.1] Gestão de Vínculos N:N, Responsável Principal e Convite de Co-Cuidadores por E-mail
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Fluxo 1: Gestão de Pets, Rede Familiar e Co-Cuidadores (N:N)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `UsuarioPetService`, `UsuarioPetController`, `CoCuidadores`, `ResponsavelPrincipal`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Tutor e Responsável Principal pelo Pet,  
> **Eu quero** cadastrar meus pets, definir quem é o responsável principal, convidar co-cuidadores por e-mail e transferir a titularidade,  
> **Para que** a rotina de cuidados do animal possa ser compartilhada de forma segura e colaborativa entre membros da família.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Na criação do pet (`POST /pets`), o tutor criador é automaticamente vinculado como `responsavelPrincipal = true`.
- [ ] Sub-recurso RESTful no `UsuarioPetController`:
  - `POST /pets/{petId}/cuidadores`: convida co-cuidador por e-mail (`CoCuidadorRequest`), validando se quem convida é o responsável principal.
  - `GET /pets/{petId}/cuidadores`: lista os co-cuidadores vinculados ao pet em `CoCuidadorResponse`.
  - `DELETE /pets/{petId}/cuidadores/{usuarioId}`: desvincula co-cuidador, impedindo desvinculação do responsável principal.
  - `PATCH /pets/{petId}/responsavel-principal`: transfere a titularidade principal com validação `@DiferentesUsuariosValidation`.
- [ ] Validações de domínio encapsuladas no `UsuarioPetValidator` sem poluir a camada de serviço com blocos condicionais.

##### Tarefas Técnicas (Child Tasks)
* **Task 11.1:** Implementar entidade `UsuarioPet` com chave composta `@EmbeddedId` `UsuarioPetId` e queries com `JOIN FETCH` no repositório. *(Estimativa: 2.0h)*
* **Task 11.2:** Desenvolver `UsuarioPetService` e `UsuarioPetValidator` com regras de convite por e-mail, titularidade e transferência. *(Estimativa: 2.5h)*
* **Task 11.3:** Criar endpoints no `UsuarioPetController` e testar operações de vínculo familiar. *(Estimativa: 1.5h)*

---

#### 🔹 [PBI-12] [Fluxo 1.2] Visualização Agregada da Rede de Cuidado (RedeCuidadoMapper) e Histórico do Pet
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Fluxo 1: Gestão de Pets, Rede Familiar e Co-Cuidadores (N:N)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `RedeCuidado`, `RedeCuidadoMapper`, `AntiNPlusOne`, `PetHistory`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Cuidador ou Tutor cadastrado,  
> **Eu quero** visualizar a rede completa de cuidado vinculada ao meu perfil e o histórico consolidado de tarefas cumpridas de cada pet,  
> **Para que** eu acompanhe todos os co-cuidadores, pets compartilhados, tarefas pendentes/concluídas e pontos acumulados de forma instantânea.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Endpoint `GET /usuarios/{id}/rede-cuidado` retorna DTO `RedeCuidadoResponse` com dados agregados de pets, co-cuidadores e métricas.
- [ ] Mapeamento e transformação desacoplados no componente `@Component` `RedeCuidadoMapper`.
- [ ] Eliminação de consultas N+1 de tarefas com consulta em lote no `TarefaRepository` (`findTarefaIdsByPetIdIn`) e agrupamento em memória.
- [ ] Endpoint `GET /pets/{id}/historico` retorna DTO `PetHistoryResponse` com a lista ordenada de tarefas concluídas do pet.

##### Tarefas Técnicas (Child Tasks)
* **Task 12.1:** Implementar `RedeCuidadoMapper` puro com conversão funcional de `PetResumo` e `CuidadorResumo`. *(Estimativa: 2.5h)*
* **Task 12.2:** Desenvolver query de carregamento em lote de tarefas no `TarefaRepository` e método `getConsolidatedHistory` no `PetService`. *(Estimativa: 2.0h)*
* **Task 12.3:** Validar performance e integridade dos dados da Rede de Cuidado. *(Estimativa: 1.5h)*

---

### 🎮 FEATURE 07: Fluxo 2: Gestão da Rotina do Pet, Expiração Atômica & Gamificação
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `BusinessFlow`, `Gamification`, `Tarefas`, `ExpiracaoAutomatica`, `Pontuacao`, `NonCRUD`
* **Start Date:** `2026-08-27`
* **Target Date:** `2026-08-28`
* **Descrição:** Implementação do segundo fluxo de negócio complexo não-CRUD obrigatório: gestão de tarefas da rotina do pet por cuidadores, mecanismo atômico de expiração automática de tarefas pendentes com prazo vencido, conclusão com atribuição de cuidador e cálculo de pontuação acumulada.

#### 🔹 [PBI-13] [Fluxo 2.1] Criação de Rotina Familiar e Mecanismo Atômico de Expiração Automática
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Fluxo 2: Gestão da Rotina do Pet, Expiração Atômica & Gamificação`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `Rotina`, `Tarefas`, `ExpiracaoAutomatica`, `ModifyingQuery`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Tutor do pet,  
> **Eu quero** criar tarefas da rotina do animal (alimentação, passeio, medicação, escovação) com prazo e pontuação, contando com a expiração automática de tarefas vencidas,  
> **Para que** a família mantenha os cuidados em dia e tarefas não realizadas dentro do prazo expirem sem intervenção manual.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Criação de tarefas via `POST /tarefas` exige `petId`, `titulo`, `descricao`, `prazo` (no presente/futuro) e `pontosTarefa` (> 0).
- [ ] Tarefas devem ser criadas inicialmente sem usuário executor associado (`usuarioId` nulo) e status `PENDENTE`.
- [ ] Query atômica `@Modifying` no `TarefaRepository` atualiza em lote tarefas pendentes cujo prazo seja menor que `LocalDateTime.now()` para o status `EXPIRADO`.
- [ ] Expiração automática é executada antes de consultas (`findAll`, `findById`, `findByUsuarioId`) e antes de tentativas de conclusão.
- [ ] Endpoint `GET /tarefas/by-usuario?usuarioId={id}` retorna com paginação e ordenação por prazo apenas tarefas pendentes válidas dos pets vinculados ao cuidador.

##### Tarefas Técnicas (Child Tasks)
* **Task 13.1:** Implementar endpoints de criação e listagem paginada no `TarefaController` e `TarefaService`. *(Estimativa: 2.0h)*
* **Task 13.2:** Desenvolver query atômica `@Modifying` de expiração no `TarefaRepository` e orquestrar chamada no `TarefaService`. *(Estimativa: 2.0h)*
* **Task 13.3:** Testar cenários de criação, consulta paginada e transição automática para `EXPIRADO`. *(Estimativa: 1.5h)*

---

#### 🔹 [PBI-14] [Fluxo 2.2] Conclusão de Tarefas, Registro de Executor e Sistema de Pontuação
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Fluxo 2: Gestão da Rotina do Pet, Expiração Atômica & Gamificação`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `Gamification`, `ConclusaoTarefa`, `Pontuacao`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Cuidador ou Tutor vinculado ao pet,  
> **Eu quero** concluir tarefas pendentes registrando meu ID como executor e acumulando pontos,  
> **Para que** o histórico de cuidados do pet registre quem realizou a atividade e meu total de pontos seja somado.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Endpoint `PATCH /tarefas/{id}/concluir` recebe `TarefaConclusaoRequest` com `concluinteId`.
- [ ] Valida se o usuário que está concluindo é vinculado ao pet da tarefa (lança erro caso não pertença à rede de cuidado).
- [ ] Impede conclusão de tarefas que não estejam com status `PENDENTE` (tarefas já concluídas ou expiradas lançam `IllegalArgumentException` 400 Bad Request).
- [ ] Ao concluir, atualiza status para `CONCLUIDO`, registra o cuidador executor e a data/hora de conclusão (`LocalDateTime.now()`).
- [ ] Endpoint `GET /tarefas/by-usuario/pontos?usuarioId={id}` calcula a soma acumulada de pontos obtidos pelo usuário em tarefas concluídas via query com `coalesce(sum(t.pontosTarefa), 0)`.

##### Tarefas Técnicas (Child Tasks)
* **Task 14.1:** Implementar método transacional `@Transactional` `concluir` no `TarefaService` com validações de vínculo e status. *(Estimativa: 2.0h)*
* **Task 14.2:** Implementar query agregadora de pontos `calcularPontosTotaisUsuario` no `TarefaRepository`. *(Estimativa: 1.5h)*
* **Task 14.3:** Criar testes de validação do ciclo completo de vida da tarefa (criação ➔ expiração / conclusão ➔ pontos). *(Estimativa: 2.0h)*

---

### 📹 FEATURE 08: Documentação Técnica, Demonstração em Vídeo & Preparação para Banca Oral
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `Documentation`, `Video`, `OralExam`, `Delivery`
* **Start Date:** `2026-08-28`
* **Target Date:** `2026-08-29`
* **Descrição:** Elaboração da documentação técnica no README, gravação do vídeo demonstrativo de até 10 minutos e consolidação da matriz de argumentação para a avaliação oral individual da disciplina.

#### 🔹 [PBI-15] Atualização Completa do README.md e Guia de Execução (Gradle)
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Documentação Técnica, Demonstração em Vídeo & Preparação para Banca Oral`
* **State:** `Approved`
* **Priority:** `2 - High`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `Documentation`, `README`, `Gradle`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Professor avaliador e Desenvolvedor da equipe,  
> **Eu quero** um README.md completo e organizado no repositório GitHub,  
> **Para que** qualquer pessoa consiga clonar, compilar via Gradle, rodar a aplicação, autenticar e testar os fluxos de rede de cuidado e rotina de tarefas.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Pré-requisitos documentados (JDK 21, Gradle 8.12+).
- [ ] Passo a passo de build e execução (`.\gradlew.bat bootRun` / `./gradlew bootRun` / `./gradlew compileJava`).
- [ ] Tabela com credenciais de teste e descrição de perfis.
- [ ] Documentação dos endpoints principais, URLs do Swagger UI, H2 Console e Actuator.
- [ ] Descrição arquitetural dos princípios SOLID, Clean Code, Spring Security com RSA 2048, Flyway e dos 2 fluxos complexos.

##### Tarefas Técnicas (Child Tasks)
* **Task 15.1:** Atualizar introdução, arquitetura, stack tecnológica Gradle e princípios SOLID no `README.md`. *(Estimativa: 1.5h)*
* **Task 15.2:** Documentar credenciais de teste, instruções de execução Gradle e links do Swagger/H2. *(Estimativa: 1.5h)*

---

#### 🔹 [PBI-16] Roteiro e Gravação do Vídeo Demonstrativo da Aplicação (Máx. 10 min)
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Documentação Técnica, Demonstração em Vídeo & Preparação para Banca Oral`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `Video`, `Demonstracao`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Equipe do Projeto PetGuardian,  
> **Eu quero** gravar e publicar um vídeo de até 10 minutos narrado demonstrando a aplicação em tempo real,  
> **Para que** o professor valide a execução de todos os requisitos obrigatórios da 3ª Sprint.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Vídeo com no máximo 10 minutos de duração, resolução nítida e áudio claro narrado pelos integrantes.
- [ ] Demonstração do Spring Security e autenticação JWT.
- [ ] Demonstração prática do Fluxo 1 (Pets, Co-Cuidadores e Rede de Cuidado).
- [ ] Demonstração prática do Fluxo 2 (Criação de Tarefas, Expiração Automática, Conclusão e Pontuação).
- [ ] Demonstração de validação de formulários (Bean Validation) e tratamento amigável de exceções no Global Exception Handler.
- [ ] Link público/não-listado do YouTube adicionado ao `README.md`.

##### Tarefas Técnicas (Child Tasks)
* **Task 16.1:** Criar roteiro técnico sequencial para gravação do vídeo (Timebox: 10 min). *(Estimativa: 1.5h)*
* **Task 16.2:** Gravar a demonstração prática da aplicação (Security + Fluxos + Validações + Swagger). *(Estimativa: 2.5h)*
* **Task 16.3:** Editar, realizar upload no YouTube e adicionar o link ao `README.md`. *(Estimativa: 1.5h)*

---

#### 🔹 [PBI-17] Matriz de Argumentação e Preparação para a Avaliação Oral Individual
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Documentação Técnica, Demonstração em Vídeo & Preparação para Banca Oral`
* **State:** `Approved`
* **Priority:** `2 - High`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `AvaliacaoOral`, `Estudo`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Integrante da equipe PetGuardian,  
> **Eu quero** revisar os trechos de código, decisões técnicas, Clean Code e justificativas arquiteturais,  
> **Para que** eu esteja plenamente preparado para responder individualmente às perguntas do professor na banca avaliativa.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Cada integrante domina a explicação de trechos específicos do código (SecurityFilterChain, Streams/groupingBy, RedeCuidadoMapper, GlobalExceptionHandler, Services transacionais, ViaCEP).
- [ ] Justificativas claras formuladas para as decisões de design (por que DTOs Records, por que RSA 2048-bit, por que injeção por construtor, por que `@Transactional` seletivo).
- [ ] Mapeamento das dificuldades encontradas e soluções implementadas.

##### Tarefas Técnicas (Child Tasks)
* **Task 17.1:** Elaborar documento de apoio com perguntas frequentes e justificativas de arquitetura do projeto. *(Estimativa: 2.0h)*
* **Task 17.2:** Realizar rodada interna de simulação da avaliação oral entre os membros do grupo. *(Estimativa: 1.5h)*

---

## 📊 4. Tabela de Visão Geral & Distribuição de Esforço

| ID do PBI | Título do PBI | Feature Pai | Story Points | Tasks (Horas) | Prioridade |
| :--- | :--- | :--- | :---: | :---: | :---: |
| **PBI-01** | Arquitetura de Exceções de Domínio e Global Exception Handler | FEAT-01 (SOLID & Qualidade) | **3 pts** | 4.5h | `1 - Critical` |
| **PBI-02** | Refatoração dos Serviços de Negócio com Princípios SOLID | FEAT-01 (SOLID & Qualidade) | **5 pts** | 6.5h | `1 - Critical` |
| **PBI-03** | Padronização de DTOs e Bean Validation Avançado (@Valid) | FEAT-02 (Validação & Swagger) | **3 pts** | 3.5h | `2 - High` |
| **PBI-04** | Documentação Interativa com SpringDoc OpenAPI 3 / Swagger | FEAT-02 (Validação & Swagger) | **3 pts** | 4.5h | `2 - High` |
| **PBI-05** | Arquitetura de Autenticação, UserDetails, BCrypt & JWT Service RSA | FEAT-03 (Security Core JWT) | **5 pts** | 5.5h | `1 - Critical` |
| **PBI-06** | Endpoints de Autenticação (/login) e Registro (/usuarios) com JWT | FEAT-03 (Security Core JWT) | **3 pts** | 4.0h | `1 - Critical` |
| **PBI-07** | SecurityFilterChain Stateless, CORS Dedicado e Proteção Centralizada | FEAT-04 (Autorização RBAC) | **5 pts** | 5.0h | `1 - Critical` |
| **PBI-08** | Segurança Granular por Método (@EnableMethodSecurity) e Handlers 401/403 | FEAT-04 (Autorização RBAC) | **3 pts** | 5.0h | `1 - Critical` |
| **PBI-09** | Configuração do Flyway e Migração Inicial DDL Pet-Centric (V1) | FEAT-05 (Flyway & Banco) | **5 pts** | 6.0h | `1 - Critical` |
| **PBI-10** | Migrações Incrementais, Status e Seeds de Segurança (V2/V3) | FEAT-05 (Flyway & Banco) | **3 pts** | 4.5h | `2 - High` |
| **PBI-11** | [Fluxo 1.1] Gestão Vínculos N:N, Responsável Principal e Convites por E-mail | FEAT-06 (Fluxo 1 Rede Cuidado) | **5 pts** | 6.0h | `1 - Critical` |
| **PBI-12** | [Fluxo 1.2] Rede de Cuidado Agregada (RedeCuidadoMapper) e Histórico do Pet | FEAT-06 (Fluxo 1 Rede Cuidado) | **5 pts** | 6.0h | `1 - Critical` |
| **PBI-13** | [Fluxo 2.1] Rotina Familiar e Expiração Automática de Tarefas | FEAT-07 (Fluxo 2 Gamificação) | **5 pts** | 5.5h | `1 - Critical` |
| **PBI-14** | [Fluxo 2.2] Conclusão de Tarefas, Registro de Executor e Pontos | FEAT-07 (Fluxo 2 Gamificação) | **5 pts** | 5.5h | `1 - Critical` |
| **PBI-15** | Atualização Completa do README.md e Guia de Execução (Gradle) | FEAT-08 (Documentação/Vídeo) | **3 pts** | 3.0h | `2 - High` |
| **PBI-16** | Roteiro e Gravação do Vídeo Demonstrativo (Máx. 10 min) | FEAT-08 (Documentação/Vídeo) | **5 pts** | 5.5h | `1 - Critical` |
| **PBI-17** | Matriz de Argumentação e Preparação para a Avaliação Oral | FEAT-08 (Documentação/Vídeo) | **3 pts** | 3.5h | `2 - High` |
| **TOTAL** | **17 PBIs / 43 Tasks** | **8 Features** | **63 pts** | **74.5h** | — |

---

## 🚀 5. Ordem Recomendada de Execução (Sprint Roadmap)

1. **Fase 1 — Refatoração de Qualidade e Exceções (SOLID & Clean Code):** Executar `PBI-01` e `PBI-02`. Criar Global Exception Handler, exceções de domínio e refatorar Services com `@RequiredArgsConstructor` (Lombok) e injeção de dependências limpa.
2. **Fase 2 — Validações de Contratos e Swagger/OpenAPI:** Executar `PBI-03` e `PBI-04`. Aplicar Bean Validation nos DTOs (`@Valid`), métodos de fábrica `toEntity()`/`fromEntity()` e documentar contratos no Swagger UI.
3. **Fase 3 — Segurança Core (Autenticação e Criptografia com JWT RSA):** Executar `PBI-05` e `PBI-06`. Configurar BCrypt, UserDetails, emissão de JWT no `TokenService` com RSA 2048-bit e rotas `/login` e `/usuarios`.
4. **Fase 4 — Segurança RBAC (Proteção de Rotas, Filtro JWT e CORS):** Executar `PBI-07` e `PBI-08`. Proteger controllers via `SecurityFilterChain` Stateless com `CorsConfig` dedicado e `@EnableMethodSecurity`.
5. **Fase 5 — Fundação e Governança de Banco (Flyway):** Executar `PBI-09` e `PBI-10`. Criar migrações versionadas DDL V1 e Seeds V2/V3 com a base de dados já madura e protegida.
6. **Fase 6 — Fluxos Especializados de Negócio:** Executar `PBI-11` e `PBI-12` (Fluxo 1 - Rede de Co-Cuidadores N:N com `UsuarioPetController` e `RedeCuidadoMapper` anti-N+1) e `PBI-13` e `PBI-14` (Fluxo 2 - Rotina Familiar, Expiração Atômica & Gamificação de Tarefas).
7. **Fase 7 — Documentação, Vídeo e Preparação da Banca:** Executar `PBI-15`, `PBI-16` e `PBI-17`.

---

## 📋 6. Guia para Criação no Azure Boards

1. Acesse sua organização no **Azure DevOps** (`dev.azure.com/{sua-organizacao}`).
2. Navegue até o projeto da Sprint 3 e abra a aba **Boards ➔ Backlogs**.
3. Selecione a visão de **Epics** e crie o Epic:
   * `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
4. Alterne para a visão de **Features** e crie as 8 Features mapeadas na Seção 2 na ordem recomendada.
5. Em cada Feature, adicione os respectivos **Product Backlog Items (PBIs)** copiando o título, descrição (User Story), critérios de aceite e Story Points.
6. Dentro de cada PBI, clique em **Add Task** e cadastre as **Child Tasks** com suas descrições e estimativas em horas (*Original Estimate* / *Remaining*).
7. Garanta que o professor esteja adicionado à organização com o nível de acesso **Basic** e permissões de administrador do projeto.
