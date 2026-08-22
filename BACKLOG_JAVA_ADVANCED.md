# 📋 Backlog Azure Boards — Sprint 3: Java Advanced (Spring Boot)
> **Projeto:** PetGuardian (Challenge FIAP 2026 - Clyvo Vet)  
> **Disciplina:** Advanced Business Development with Java (Java Advanced)  
> **Epic Principal:** `[EPIC] PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`  
> **Start Date:** `2026-08-24`  
> **Target Date:** `2026-08-29`  
> **Padrão:** Azure DevOps / Azure Boards (Scrum Process Template: *Epic ➔ Feature ➔ PBI ➔ Child Tasks*)  
> **Base de Requisitos:** Slides 20 a 22 da Apresentação Oficial FIAP & Diretrizes da Mentoria Clyvo (Arquitetura Pet-Centric)  
> **Sequência Estratégica:** 1º Refatoração & Qualidade ➔ 2º Segurança & JWT ➔ 3º Versionamento de Banco (Flyway) ➔ 4º Fluxos Complexos de Negócio ➔ 5º Documentação & Entrega

---

## 🎯 1. Resumo Executivo & Mapeamento de Requisitos da Sprint 3

| Requisito Oficial (Slides 20-22) | Peso / Pontos | Status no Backlog | Destaque Técnico & Mentoria Clyvo |
| :--- | :---: | :---: | :--- |
| **1. Refatoração SOLID, DRY & Clean Code** | **Penalidades (-10 a -15 pts)** | **Alto (PBI-01 a PBI-04)** | Injeção por construtor (`@RequiredArgsConstructor`), eliminação de `ResponseStatusException`, Global Exception Handler, Bean Validation e Swagger/OpenAPI. |
| **2. Spring Security & Controle de Acesso** | **30 pts** | **Crítico (PBI-05 a PBI-08)** | Mínimo 2 perfis de usuário (`ADMIN`, `TUTOR`, `VETERINARIO`), BCrypt, tokens JWT Stateless para o Mobile, proteção de rotas via `SecurityFilterChain` e `@PreAuthorize`. |
| **3. Flyway (Controle de Versão de BD)** | **20 pts** | **Crítico (PBI-09 e PBI-10)** | Migrações versionadas (`V1`, `V2`, `V3`) com tabelas Pet-Centric: `pet` (com score de bem-estar), `historico_peso`, `historico_saude`, `clinica` (flags 24h/emergência) e `treinamento`. |
| **4. Funcionalidades Completas (Fluxos Não-CRUD)** | **20 pts** | **Crítico (PBI-11 a PBI-14)** | **Dois fluxos ponta a ponta complexos:**<br>1) *Atendimento Clínico & Prontuário:* Busca de clínicas 24h, agendamento com validação de conflito de horários e registro no histórico clínico do pet.<br>2) *Rotina Familiar, Treinamento & Gamificação Pet-Centric:* Tutores gerenciam rotina, realizam treinos e concluem cuidados somando pontos ao Score do Pet, com expiração automática e Dicas de IA. |
| **5. Documentação, Vídeo (10 min) & Avaliação Oral** | **Obrigatório** | **Alto (PBI-15 a PBI-17)** | README detalhado, gravação com demonstração das rotas/segurança/fluxos e guia de estudo para defesa individual com foco em IA e decisões de arquitetura. |
| **6. Frontend / Camada de Visualização** | **30 pts** | **Incerto / Final (PBI-18 e PBI-19)** | ⚠️ *Posicionado como as últimas tarefas do backlog devido à priorização da integração com o app Mobile (React Native).* |

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
│   ├── [PBI-05] Arquitetura de Autenticação, UserDetails, BCrypt & Serviço de Tokens JWT (5 pts)
│   └── [PBI-06] Endpoints de Autenticação e Registro com Emissão de Token JWT (/auth/login e /auth/register) (3 pts)
│
├── 🛡️ [FEATURE 04] Autorização, Filtro JWT Stateless e Proteção de Endpoints (RBAC)
│   ├── [PBI-07] Configuração do SecurityFilterChain Stateless, Filtro JWT e CORS para Mobile (5 pts)
│   └── [PBI-08] Segurança Granular por Método (@PreAuthorize) e Handlers 401/403 (3 pts)
│
├── 🏛️ [FEATURE 05] Versionamento de Banco de Dados & Governança de Esquema (Flyway)
│   ├── [PBI-09] Configuração do Flyway e Migração Inicial DDL Pet-Centric (V1) (5 pts)
│   └── [PBI-10] Migrações Incrementais de Domínio, Clínicas 24h e Seeds de Segurança (V2 e V3) (3 pts)
│
├── 🩺 [FEATURE 06] Fluxo 1: Ciclo de Atendimento Veterinário, Clínicas 24h e Histórico de Saúde do Pet
│   ├── [PBI-11] [Fluxo 1.1] Busca de Clínicas 24h e Solicitação de Atendimento com Validação de Conflito (5 pts)
│   └── [PBI-12] [Fluxo 1.2] Triagem, Confirmação e Encerramento Clínico com Prontuário e Histórico Vacinal (5 pts)
│
├── 🎮 [FEATURE 07] Fluxo 2: Gestão da Rotina do Pet, Treinamento & Gamificação Pet-Centric
│   ├── [PBI-13] [Fluxo 2.1] Criação de Rotina Familiar de Cuidados e Mecanismo de Expiração Automática (5 pts)
│   └── [PBI-14] [Fluxo 2.2] Conclusão de Rotina, Módulos de Treinamento e Evolução do Score do Pet (5 pts)
│
├── 📹 [FEATURE 08] Documentação Técnica, Demonstração em Vídeo & Preparação para Banca Oral
│   ├── [PBI-15] Atualização Completa do README.md e Guia de Execução (3 pts)
│   ├── [PBI-16] Roteiro e Gravação do Vídeo Demonstrativo da Aplicação (Máx. 10 min) (5 pts)
│   └── [PBI-17] Matriz de Argumentação e Preparação para a Avaliação Oral Individual (3 pts)
│
└── 🌐 [FEATURE 09] Camada de Visualização Web (Thymeleaf / Interface de Apoio) ⚠️ [EM ESPERA / BAIXA PRIORIDADE]
    ├── [PBI-18] Interface Web Básica de Autenticação e Cadastro com Formulários Validados (5 pts)
    └── [PBI-19] Telas Web de Operação dos Fluxos de Atendimentos e Tarefas do Pet (5 pts)
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
* **Descrição:** Refatoração da camada de serviços e tratamento de erros para cumprir os princípios de Clean Code, SRP e DIP, eliminando ResponseStatusException solta e unificando o tratamento de exceções de domínio no Global Exception Handler.

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
> **Para que** todas as falhas de negócio, validação e segurança retornem respostas HTTP estruturadas e previsíveis.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Exceções personalizadas criadas no pacote `exception`: `ResourceNotFoundException`, `BusinessException`, `ConflictException`, `UnauthorizedOperationException`.
- [ ] DTO padrão `ErrorResponse` contendo `timestamp`, `status`, `error`, `message`, `path` e lista de `fieldErrors`.
- [ ] `GlobalExceptionHandler` intercepta todas as exceções da aplicação sem expor stacktrace interno para o cliente.

##### Tarefas Técnicas (Child Tasks)
* **Task 1.1:** Criar classes de exceção de domínio e DTO de resposta padronizada de erro. *(Estimativa: 1.5h)*
* **Task 1.2:** Implementar `@RestControllerAdvice` no `GlobalExceptionHandler` mapeando todos os tipos de erro. *(Estimativa: 2.0h)*
* **Task 1.3:** Testar o tratamento de erros para garantir consistência de resposta. *(Estimativa: 1.0h)*

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
> **Eu quero** refatorar os Services para eliminar acoplamento indevido, métodos longos e lançamentos de `ResponseStatusException`,  
> **Para que** o código respeite os princípios SOLID (especialmente SRP e DIP), injeção por construtor com Lombok e evite penalidades avaliativas.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Eliminação total de `ResponseStatusException` de dentro de todos os Services (`PetService`, `UsuarioService`, `AtendimentoService`, `TarefaService`, `ClinicaService`, `VeterinarioService`).
- [ ] Injeção de dependências estritamente por construtor utilizando `@RequiredArgsConstructor` (Lombok) com atributos `private final`.
- [ ] Quebra de métodos longos em métodos privados coesos com nomes autoexplicativos (Clean Code & DRY).
- [ ] Métodos transacionais anotados com `@Transactional`.

##### Tarefas Técnicas (Child Tasks)
* **Task 2.1:** Refatorar `UsuarioService`, `PetService`, `ClinicaService` e `VeterinarioService` aplicando `@RequiredArgsConstructor` e exceções de domínio. *(Estimativa: 2.5h)*
* **Task 2.2:** Refatorar `AtendimentoService` e `TarefaService` aplicando modularização de regras e anotações `@Transactional`. *(Estimativa: 2.5h)*
* **Task 2.3:** Auditar o código-fonte para assegurar ausência de duplicação (DRY) e conformidade com Clean Code. *(Estimativa: 1.5h)*

---

### 📑 FEATURE 02: Validação de Contratos (Bean Validation) & Documentação OpenAPI
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `BeanValidation`, `OpenAPI`, `Swagger`, `Contracts`
* **Start Date:** `2026-08-24`
* **Target Date:** `2026-08-25`
* **Descrição:** Padronização de DTOs de entrada e saída com Bean Validation avançado e documentação interativa completa da API utilizando SpringDoc OpenAPI 3 / Swagger UI com suporte a autenticação JWT.

#### 🔹 [PBI-03] Padronização de DTOs e Bean Validation Avançado (@Valid)
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Validação de Contratos (Bean Validation) & Documentação OpenAPI`
* **State:** `Approved`
* **Priority:** `2 - High`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `BeanValidation`, `DTO`, `Validation`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Desenvolvedor Mobile consumidor da API,  
> **Eu quero** que todas as requisições de entrada sejam estritamente validadas via Bean Validation,  
> **Para que** dados incorretos sejam rejeitados imediatamente no Controller com mensagens amigáveis em português.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Todos os DTOs de Request anotados com constraints precisas (`@NotBlank`, `@NotNull`, `@Size`, `@Email`, `@FutureOrPresent`, `@Positive`).
- [ ] Mensagens de validação customizadas em português em cada constraint.
- [ ] Anotação `@Valid` aplicada em todos os parâmetros `@RequestBody` nos métodos dos Controllers.

##### Tarefas Técnicas (Child Tasks)
* **Task 3.1:** Revisar e aplicar Bean Validation completo em todos os DTOs de Request (`PetRequest`, `UsuarioRequest`, `TarefaRequest`, `AtendimentoRequest`, `ClinicaRequest`). *(Estimativa: 2.0h)*
* **Task 3.2:** Garantir anotação `@Valid` em todos os Controllers e testar respostas de 400 Bad Request. *(Estimativa: 1.5h)*

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
> **Eu quero** acessar a documentação interativa Swagger UI da aplicação com esquema de segurança Bearer JWT configurado,  
> **Para que** eu possa testar todos os endpoints e validar os contratos de dados diretamente pelo navegador.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Dependência `springdoc-openapi-starter-webmvc-ui` adicionada ao `pom.xml`.
- [ ] `OpenApiConfig` configurada com metadados do projeto e `SecurityScheme` do tipo HTTP Bearer (JWT).
- [ ] Controllers documentados com anotações `@Tag`, `@Operation` e `@ApiResponse` (200, 201, 400, 401, 403, 404).
- [ ] Swagger UI acessível e 100% funcional em `/swagger-ui/index.html`.

##### Tarefas Técnicas (Child Tasks)
* **Task 4.1:** Criar classe de configuração `OpenApiConfig` com metadados e `SecurityScheme` Bearer JWT. *(Estimativa: 1.5h)*
* **Task 4.2:** Enriquecer os Controllers com `@Tag`, `@Operation` e `@ApiResponse`. *(Estimativa: 2.0h)*
* **Task 4.3:** Testar interatividade do Swagger executando requisições autenticadas. *(Estimativa: 1.0h)*

---

### 🔐 FEATURE 03: Autenticação e Gestão de Credenciais com JWT (Spring Security Core)
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `Security`, `Authentication`, `BCrypt`, `JWT`, `MobileIntegration`
* **Start Date:** `2026-08-25`
* **Target Date:** `2026-08-26`
* **Descrição:** Estruturação da arquitetura de autenticação com Spring Security e JWT (JSON Web Token), criptografia BCrypt, UserDetails e emissão de tokens de acesso para consumo pelo aplicativo Mobile (React Native).

#### 🔹 [PBI-05] Arquitetura de Autenticação, UserDetails, BCrypt & Serviço de Tokens JWT
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Autenticação e Gestão de Credenciais com JWT (Spring Security Core)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `SpringSecurity`, `BCrypt`, `UserDetails`, `JWT`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Arquiteto de Software Backend,  
> **Eu quero** configurar o Spring Security com `BCryptPasswordEncoder`, `UserDetails` na entidade `Usuario`, `UserDetailsService` e um serviço emissor de tokens JWT (`TokenService`),  
> **Para que** as credenciais sejam salvas com hash seguro e a aplicação emita tokens JWT assinados para autenticar o app Mobile.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Dependências `spring-boot-starter-security` e biblioteca JWT (`java-jwt` da Auth0 ou `jjwt`) adicionadas ao `pom.xml`.
- [ ] Criação do `enum PerfilUsuario` (`ROLE_ADMIN`, `ROLE_TUTOR`, `ROLE_VETERINARIO`) com mapeamento de authorities.
- [ ] Entidade `Usuario` implementa a interface `UserDetails` do Spring Security.
- [ ] Bean `PasswordEncoder` configurado com `BCryptPasswordEncoder`.
- [ ] `TokenService` implementado gerando tokens JWT assinados com tempo de expiração e claims (`id`, `email`, `role`, `nome`) e validação de tokens recebidos.
- [ ] Serviço `UserDetailsServiceImpl` busca usuário por email no banco de dados.

##### Tarefas Técnicas (Child Tasks)
* **Task 5.1:** Configurar Maven com dependências de segurança e criar enum `Perfil` / `Role`. *(Estimativa: 1.5h)*
* **Task 5.2:** Implementar `UserDetails` na entidade `Usuario` e criar `UserDetailsServiceImpl`. *(Estimativa: 2.0h)*
* **Task 5.3:** Desenvolver `TokenService` com geração e validação de tokens JWT assinados (HMAC-SHA256). *(Estimativa: 2.0h)*

---

#### 🔹 [PBI-06] Endpoints de Autenticação e Registro com Emissão de Token JWT (/auth/login e /auth/register)
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Autenticação e Gestão de Credenciais com JWT (Spring Security Core)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `AuthEndpoints`, `Login`, `Register`, `JWT`, `MobileAPI`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Usuário do aplicativo Mobile (Tutor, Veterinário ou Admin),  
> **Eu quero** realizar login e cadastro através de endpoints públicos dedicados (`/auth/login` e `/auth/register`),  
> **Para que** eu receba um token JWT válido e os dados do meu perfil para manter a sessão ativa no aplicativo.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Criação do `AuthController` com mapeamento explícito de `/auth/login` e `/auth/register`.
- [ ] DTOs específicos: `LoginRequest`, `LoginResponse` (contendo `token`, `tipo: "Bearer"`, `expiraEm`, `usuario`), `RegistroUsuarioRequest` e `UsuarioResponse`.
- [ ] `/auth/login` autentica credenciais via `AuthenticationManager`, gera token JWT e retorna o `LoginResponse`.
- [ ] `/auth/register` valida unicidade de email, criptografa a senha com BCrypt, persiste o usuário e retorna o `LoginResponse` com token JWT.
- [ ] Retorno de status HTTP 401 para credenciais inválidas e HTTP 409 para email duplicado com corpo JSON padronizado.

##### Tarefas Técnicas (Child Tasks)
* **Task 6.1:** Desenvolver DTOs de autenticação (`LoginRequest`, `LoginResponse`, `RegistroUsuarioRequest`). *(Estimativa: 1.0h)*
* **Task 6.2:** Implementar endpoints `/auth/login` e `/auth/register` no `AuthController` e `AuthService`. *(Estimativa: 2.0h)*
* **Task 6.3:** Testar cenários de login válido, senha incorreta e cadastro de email repetido. *(Estimativa: 1.0h)*

---

### 🛡️ FEATURE 04: Autorização, Filtro JWT Stateless e Proteção de Endpoints (RBAC)
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `Security`, `RBAC`, `SecurityFilterChain`, `JWTFilter`, `CORS`
* **Start Date:** `2026-08-25`
* **Target Date:** `2026-08-26`
* **Descrição:** Configuração da cadeia de filtros `SecurityFilterChain` em modo STATELESS com filtro de autenticação JWT (`JwtAuthenticationFilter`), suporte a CORS para o aplicativo Mobile (React Native), isolamento de rotas por perfis (RBAC) e proteção granular por método (`@PreAuthorize`).

#### 🔹 [PBI-07] Configuração do SecurityFilterChain Stateless, Filtro JWT e CORS para Mobile
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Autorização, Filtro JWT Stateless e Proteção de Endpoints (RBAC)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `SecurityFilterChain`, `Stateless`, `JwtFilter`, `CORS`, `Mobile`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Arquiteto de Software e Administrador de Segurança,  
> **Eu quero** configurar o `SecurityFilterChain` em modo Stateless com um filtro interceptor de JWT e liberação de CORS para o app Mobile,  
> **Para que** o aplicativo React Native consiga consumir a API enviando o header `Authorization: Bearer <token>` sem problemas de CORS ou sessão no servidor.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] `SecurityConfig` configurado com `SessionCreationPolicy.STATELESS` e CSRF desabilitado.
- [ ] Implementação de `JwtAuthenticationFilter` (`OncePerRequestFilter`) extraindo `Bearer <token>` e populando o `SecurityContextHolder`.
- [ ] Filtro registrado antes do `UsernamePasswordAuthenticationFilter`.
- [ ] Configuração de CORS (`CorsConfigurationSource`) liberando métodos e headers para o ambiente Mobile (Expo / localhost / IP de rede local).
- [ ] Rotas públicas liberadas (`/auth/**`, `/h2-console/**`, `/swagger-ui/**`, `/v3/api-docs/**`, `/actuator/health`).
- [ ] Rotas privadas protegidas por perfil (`ADMIN`, `VETERINARIO`, `TUTOR`).

##### Tarefas Técnicas (Child Tasks)
* **Task 7.1:** Implementar o filtro `JwtAuthenticationFilter` (`OncePerRequestFilter`). *(Estimativa: 2.0h)*
* **Task 7.2:** Configurar `SecurityConfig` com `SecurityFilterChain` Stateless, CORS e registro do filtro JWT. *(Estimativa: 2.0h)*
* **Task 7.3:** Validar requisições autenticadas com Bearer Token e rejeição de tokens expirados/inválidos. *(Estimativa: 1.0h)*

---

#### 🔹 [PBI-08] Segurança Granular por Método (@PreAuthorize) e Handlers 401/403
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Autorização, Filtro JWT Stateless e Proteção de Endpoints (RBAC)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `PreAuthorize`, `MethodSecurity`, `AccessDeniedHandler`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Desenvolvedor Backend,  
> **Eu quero** habilitar segurança por método com `@EnableMethodSecurity` e customizar os handlers de erro de segurança (401 e 403),  
> **Para que** a aplicação execute validações finas de autorização nos controllers e retorne payloads JSON claros quando o app Mobile enviar requisições não autorizadas.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] `@EnableMethodSecurity(prePostEnabled = true)` habilitado na configuração de segurança.
- [ ] Anotações `@PreAuthorize` aplicadas nos métodos dos Controllers.
- [ ] Implementação de `CustomAuthenticationEntryPoint` (401) e `CustomAccessDeniedHandler` (403) em JSON padronizado.

##### Tarefas Técnicas (Child Tasks)
* **Task 8.1:** Habilitar method security e aplicar `@PreAuthorize` nos Controllers. *(Estimativa: 2.0h)*
* **Task 8.2:** Criar handlers customizados de erro de segurança (`CustomAuthenticationEntryPoint` e `CustomAccessDeniedHandler`). *(Estimativa: 2.0h)*
* **Task 8.3:** Testar respostas de erro 401 e 403 garantindo a ausência de stacktrace exposto. *(Estimativa: 1.0h)*

---

### 🏛️ FEATURE 05: Versionamento de Banco de Dados & Governança de Esquema (Flyway)
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `Flyway`, `Database`, `Governance`, `PetCentric`
* **Start Date:** `2026-08-26`
* **Target Date:** `2026-08-27`
* **Descrição:** Implementação de controle de versão de banco de dados relacional através do Flyway, estruturando tabelas Pet-Centric (`pet`, `historico_peso`, `historico_saude`, `clinica` com flags 24h, `treinamento` e `tarefa_rotina`).

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
> **Para que** todas as tabelas do sistema sejam criadas em memória com integridade referencial, campos de pontuação no pet, histórico de saúde, suporte a clínicas 24h e console interativo `/h2-console` para testes com o app Mobile.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Dependências `flyway-core` e `com.h2database:h2` configuradas no `pom.xml`.
- [ ] Propriedades do Spring configuradas para habilitar o Flyway e H2 (`spring.datasource.url=jdbc:h2:mem:petguardiandb`, `spring.h2.console.enabled=true`, `spring.flyway.enabled=true`, `spring.jpa.hibernate.ddl-auto=validate`).
- [ ] Script `V1__criar_tabelas_iniciais_petcentric.sql` criado em `src/main/resources/db/migration/` contendo:
  - Tabela `usuario` e tabela de relacionamento `usuario_pet` (gestão familiar/cuidadores).
  - Tabela `pet` com campos `pontos_experiencia`, `score_bem_estar`, `nivel_saude`, `peso_atual`, `data_nascimento`.
  - Tabela `historico_peso` e `historico_saude` / `vacina` (registros vinculados ao pet).
  - Tabela `clinica` com `atendimento_24h` (boolean), `pronto_socorro` (boolean), `latitude`, `longitude`.
  - Tabela `veterinario`, `tipo_atendimento`, `status`, `atendimento` (prontuário clínico).
  - Tabela `tarefa` (rotina do pet criada por tutores com frequência diária/semanal) e `treinamento_pet`.
- [ ] Definição explícita de PKs, FKs, restrições `NOT NULL` e constraints `UNIQUE`.
- [ ] Inicialização da aplicação executa a migração no H2 com sucesso e cria a tabela `flyway_schema_history`.
- [ ] Console do H2 acessível em `/h2-console` para inspeção visual dos dados.

##### Tarefas Técnicas (Child Tasks)
* **Task 9.1:** Configurar dependências do Flyway e H2 no `pom.xml` e `application.properties`. *(Estimativa: 1.5h)*
* **Task 9.2:** Elaborar script DDL completo `V1__criar_tabelas_iniciais_petcentric.sql` com todas as tabelas Pet-Centric. *(Estimativa: 3.5h)*
* **Task 9.3:** Testar a inicialização do Spring Boot validando a criação da tabela `flyway_schema_history` e acesso ao `/h2-console`. *(Estimativa: 1.0h)*

---

#### 🔹 [PBI-10] Migrações Incrementais de Domínio, Clínicas 24h e Seeds de Segurança (V2 e V3)
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Versionamento de Banco de Dados & Governança de Esquema (Flyway)`
* **State:** `Approved`
* **Priority:** `2 - High`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `Flyway`, `SeedData`, `Clinicas24h`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Desenvolvedor Backend e Administrador do Sistema,  
> **Eu quero** criar migrações incrementais do Flyway para inserir sementes de dados de domínio (status, módulos de treinamento, clínicas 24h e usuários administradores padrão),  
> **Para que** a aplicação já inicie pronta para testes e demonstração imediata no Mobile.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Script `V2__popular_tabelas_dominio_e_treinamentos.sql` insere:
  - Status de atendimento e tarefas (`PENDENTE`, `CONFIRMADO`, `CONCLUIDO`, `CANCELADO`, `EXPIRADO`).
  - Tipos de atendimento (`CONSULTA`, `VACINACAO`, `EXAME`, `CIRURGIA`, `EMERGENCIA_24H`).
  - Módulos padrão de Treinamento e Educação (`treinamento_pet`: "Comandos Básicos", "Higiene e Escovação", "Adaptação").
  - Seeds de clínicas veterinárias com indicação de atendimento 24h e pronto-socorro.
- [ ] Script `V3__adicionar_perfis_e_usuarios_iniciais.sql` insere usuários de teste com senhas hash BCrypt (`ROLE_ADMIN`, `ROLE_TUTOR`, `ROLE_VETERINARIO`) e pets vinculados.
- [ ] Migrações são idempotentes e executam sequencialmente.

##### Tarefas Técnicas (Child Tasks)
* **Task 10.1:** Elaborar script `V2__popular_tabelas_dominio_e_treinamentos.sql` com constantes e módulos de treino. *(Estimativa: 1.5h)*
* **Task 10.2:** Elaborar script `V3__adicionar_perfis_e_usuarios_iniciais.sql` com seeds de usuários hash BCrypt e pets vinculados. *(Estimativa: 2.0h)*
* **Task 10.3:** Validar integridade referencial e execução sequencial no banco de dados. *(Estimativa: 1.0h)*

---

### 🩺 FEATURE 06: Fluxo 1: Ciclo de Atendimento Veterinário, Clínicas 24h e Histórico de Saúde do Pet
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `BusinessFlow`, `Atendimento`, `Clinica24h`, `HistoricoSaude`, `NonCRUD`
* **Start Date:** `2026-08-27`
* **Target Date:** `2026-08-28`
* **Descrição:** Implementação do primeiro fluxo de negócio complexo não-CRUD obrigatório: busca de clínicas (com filtros 24h e pronto-socorro), agendamento com validação de conflito de horários e conclusão com gravação no prontuário e histórico de saúde do Pet.

#### 🔹 [PBI-11] [Fluxo 1.1] Busca de Clínicas 24h e Solicitação de Atendimento com Validação de Conflito
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Fluxo 1: Ciclo de Atendimento Veterinário, Clínicas 24h e Histórico de Saúde do Pet`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `Atendimento`, `Clinica24h`, `Agendamento`, `AgendaConflict`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Tutor cadastrado no PetGuardian,  
> **Eu quero** buscar clínicas veterinárias mais próximas (com opção de atendimento 24h / pronto-socorro) e solicitar agendamento de consulta/exame para o meu pet,  
> **Para que** o sistema valide a disponibilidade de agenda do veterinário e crie a solicitação com status inicial `PENDENTE`.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Endpoint `GET /clinicas` com filtros por `atendimento24h`, `prontoSocorro` e cidade.
- [ ] Validação de Vínculo: O sistema valida se o tutor autenticado possui vínculo ativo com o pet na tabela `usuario_pet`.
- [ ] Validação de Data Futura: Agendamento permitido exclusivamente para datas e horários futuros.
- [ ] Validação de Conflito de Horário: Query especializada no `AtendimentoRepository` verifica sobreposição de horários do veterinário.
- [ ] Caso haja conflito, lança `ConflictException` (409 Conflict) com mensagem explicativa.
- [ ] Atendimento é persistido com status `PENDENTE`.

##### Tarefas Técnicas (Child Tasks)
* **Task 11.1:** Criar métodos de busca de clínicas com filtros 24h e queries de conflito de agenda no repositório. *(Estimativa: 2.0h)*
* **Task 11.2:** Implementar regra de negócio de solicitação e validações de conflito no `AtendimentoService`. *(Estimativa: 2.5h)*
* **Task 11.3:** Criar endpoints no `AtendimentoController` e testar agendamentos válidos e duplicados. *(Estimativa: 1.5h)*

---

#### 🔹 [PBI-12] [Fluxo 1.2] Triagem, Confirmação e Encerramento Clínico com Prontuário e Histórico Vacinal
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Fluxo 1: Ciclo de Atendimento Veterinário, Clínicas 24h e Histórico de Saúde do Pet`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `Triagem`, `Confirmacao`, `Prontuario`, `HistoricoVacinal`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Veterinário ou Administrador de Clínica,  
> **Eu quero** realizar a triagem (confirmar ou cancelar) e concluir o atendimento preenchendo diagnóstico, prescrições e vacinas aplicadas,  
> **Para que** o histórico de saúde e vacinal do pet seja registrado com status `CONCLUIDO` e fique visível na tela dedicada do Pet.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] **Confirmação:** Endpoint `/atendimentos/{id}/confirmar` altera status para `CONFIRMADO` (`ROLE_VETERINARIO` e `ROLE_ADMIN`).
- [ ] **Cancelamento:** Endpoint `/atendimentos/{id}/cancelar` altera status para `CANCELADO` com justificativa textual obrigatória.
- [ ] **Conclusão Clínica:** Endpoint `/atendimentos/{id}/finalizar` recebe DTO `FinalizacaoAtendimentoRequest` (diagnóstico, laudo, prescrição, vacinas aplicadas), atualiza status para `CONCLUIDO` e grava automaticamente na tabela `historico_saude` do Pet.
- [ ] Endpoint `GET /pets/{id}/historico-saude` retorna a linha do tempo clínica e vacinal completa do pet para exibição no Mobile.

##### Tarefas Técnicas (Child Tasks)
* **Task 12.1:** Implementar DTOs de triagem e finalização clínica (`ConfirmacaoRequest`, `CancelamentoRequest`, `FinalizacaoAtendimentoRequest`). *(Estimativa: 1.5h)*
* **Task 12.2:** Desenvolver métodos de transição de status e persistência do histórico clínico/vacinal do pet no `AtendimentoService`. *(Estimativa: 2.5h)*
* **Task 12.3:** Criar endpoint `GET /pets/{id}/historico-saude` e testar o fluxo de ponta a ponta. *(Estimativa: 2.0h)*

---

### 🎮 FEATURE 07: Fluxo 2: Gestão da Rotina do Pet, Treinamento & Gamificação Pet-Centric
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `BusinessFlow`, `Gamification`, `PetScore`, `RotinaFamiliar`, `Treinamento`, `AiAssistant`, `NonCRUD`
* **Start Date:** `2026-08-27`
* **Target Date:** `2026-08-28`
* **Descrição:** Implementação do segundo fluxo de negócio complexo não-CRUD obrigatório: gestão de tarefas da rotina do pet por tutores, realização de trilhas de treinamento/educação, evolução do Score de Bem-Estar do Pet e Dicas personalizadas via Assistente de IA.

#### 🔹 [PBI-13] [Fluxo 2.1] Criação de Rotina Familiar de Cuidados e Mecanismo de Expiração Automática
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Fluxo 2: Gestão da Rotina do Pet, Treinamento & Gamificação Pet-Centric`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `Rotina`, `TarefasFamiliares`, `ExpiracaoAutomatica`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Tutor e membro da família do pet,  
> **Eu quero** criar tarefas da rotina diária e periódica do animal (alimentação, passeio, medicação, hidratação) e contar com expiração automática de tarefas vencidas,  
> **Para que** os cuidados familiares fiquem organizados e tarefas pendentes não cumpridas no dia expirem automaticamente.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Qualquer tutor ou co-cuidador vinculado ao pet (`usuario_pet`) pode criar tarefas de rotina com tipo de frequência (`DIARIA`, `SEMANAL`, `PONTUAL`), horário previsto e pontuação (`pontos_tarefa`).
- [ ] Mecanismo atômico no `TarefaRepository` / `TarefaService` atualiza tarefas pendentes com prazo ultrapassado para o status `EXPIRADO`.
- [ ] Expiração automática é disparada antes de listagens e tentativas de conclusão.
- [ ] Endpoint `GET /pets/{id}/tarefas/hoje` retorna apenas tarefas ativas e válidas para a data atual.

##### Tarefas Técnicas (Child Tasks)
* **Task 13.1:** Implementar endpoint de cadastro de tarefas de rotina familiar no `TarefaController` e `TarefaService`. *(Estimativa: 2.0h)*
* **Task 13.2:** Desenvolver mecanismo de expiração automática no `TarefaRepository` e `TarefaService`. *(Estimativa: 2.0h)*
* **Task 13.3:** Testar cenários de criação de rotina e expiração de tarefas vencidas. *(Estimativa: 1.5h)*

---

#### 🔹 [PBI-14] [Fluxo 2.2] Conclusão de Rotina, Módulos de Treinamento e Evolução do Score do Pet
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Fluxo 2: Gestão da Rotina do Pet, Treinamento & Gamificação Pet-Centric`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `Gamification`, `PetScore`, `Treinamento`, `AiAssistant`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Tutor e cuidador do pet,  
> **Eu quero** marcar tarefas da rotina como concluídas, realizar módulos de treinamento e consultar o score de bem-estar do pet e dicas preventivas da IA,  
> **Para que** o animal evolua seu nível de saúde e nós recebamos orientações assertivas sobre os seus cuidados.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Conclusão de tarefa de rotina via `PATCH /tarefas/{id}/concluir` credita os pontos diretamente ao `Pet` (`pontos_experiencia`) e recalcula o `score_bem_estar` do animal.
- [ ] Conclusão de módulo de treino via `POST /pets/{id}/treinamentos/{moduloId}/concluir` registra a conclusão e soma pontuação extra ao Pet.
- [ ] Tentativa de concluir tarefa já expirada ou já concluída lança `BusinessException` (400 Bad Request).
- [ ] Endpoint `GET /pets/{id}/score-bem-estar` retorna: total de pontos, nível de saúde, tarefas concluídas no mês e percentual de rotina cumprida.
- [ ] Endpoint `GET /pets/{id}/assistente-ia/dicas` analisa raça, idade, peso e histórico de saúde do pet e retorna dicas preventivas e conselhos formatados em JSON.

##### Tarefas Técnicas (Child Tasks)
* **Task 14.1:** Implementar método transacional de conclusão de tarefa com crédito de pontos no `Pet` e recálculo de score. *(Estimativa: 2.0h)*
* **Task 14.2:** Implementar serviço de módulos de treinamento e conclusão no `TreinamentoService`. *(Estimativa: 2.0h)*
* **Task 14.3:** Criar serviço `AiAssistantService` com endpoint `GET /pets/{id}/assistente-ia/dicas` gerando conselhos clínicos/comportamentais com base no histórico do pet. *(Estimativa: 2.5h)*

---

### 📹 FEATURE 08: Documentação Técnica, Demonstração em Vídeo & Preparação para Banca Oral
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `Documentation`, `Video`, `OralExam`, `Delivery`
* **Start Date:** `2026-08-28`
* **Target Date:** `2026-08-29`
* **Descrição:** Elaboração da documentação técnica no README, gravação do vídeo demonstrativo de até 10 minutos e consolidação da matriz de argumentação para a avaliação oral individual da disciplina.

#### 🔹 [PBI-15] Atualização Completa do README.md e Guia de Execução
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Documentação Técnica, Demonstração em Vídeo & Preparação para Banca Oral`
* **State:** `Approved`
* **Priority:** `2 - High`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `Documentation`, `README`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Professor avaliador e Desenvolvedor da equipe,  
> **Eu quero** um README.md completo e organizado no repositório GitHub,  
> **Para que** qualquer pessoa consiga clonar, rodar as migrações do Flyway, autenticar com as credenciais de teste e testar os fluxos da aplicação.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Pré-requisitos documentados (Java 17, Maven 3.8+).
- [ ] Passo a passo de build e execução (`mvn clean spring-boot:run`).
- [ ] Tabela com credenciais de teste (`admin@clyvo.com`, `vet@clyvo.com`, `tutor@clyvo.com`).
- [ ] Documentação dos endpoints principais, URLs do Swagger UI, H2 Console e Actuator Health.
- [ ] Descrição arquitetural das migrações Flyway, Spring Security, SOLID e dos 2 fluxos complexos.

##### Tarefas Técnicas (Child Tasks)
* **Task 15.1:** Atualizar introdução, arquitetura, stack tecnológica e princípios SOLID no `README.md`. *(Estimativa: 1.5h)*
* **Task 15.2:** Documentar credenciais de teste, instruções de execução e links do Swagger/H2. *(Estimativa: 1.5h)*

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
- [ ] Demonstração das migrações do Flyway executando na inicialização do Spring Boot.
- [ ] Demonstração do Spring Security: tentativa de acesso não autenticado (401/403) e login com perfis `ADMIN`, `VETERINARIO` e `TUTOR`.
- [ ] Demonstração prática dos dois fluxos de negócio complexos em funcionamento ponta a ponta.
- [ ] Demonstração de validação de formulários (Bean Validation) e tratamento amigável de exceções.
- [ ] Link público/não-listado do YouTube adicionado ao `README.md`.

##### Tarefas Técnicas (Child Tasks)
* **Task 16.1:** Criar roteiro técnico sequencial para gravação do vídeo (Timebox: 10 min). *(Estimativa: 1.5h)*
* **Task 16.2:** Gravar a demonstração prática da aplicação (Flyway + Security + Fluxos + Validações). *(Estimativa: 2.5h)*
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
> **Eu quero** revisar os trechos de código, decisões técnicas, uso de IA e justificativas arquiteturais,  
> **Para que** eu esteja plenamente preparado para responder individualmente às perguntas do professor na banca avaliativa em sala de aula (Slide 22).

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Cada integrante domina a explicação de trechos específicos do código (SecurityFilterChain, UserDetailsService, Migrations Flyway, Services transacionais).
- [ ] Justificativas claras formuladas para as decisões de design (por que Flyway, por que BCrypt, por que arquitetura em camadas, separação de DTOs).
- [ ] Mapeamento das dificuldades encontradas e soluções implementadas.
- [ ] Relatório claro e transparente sobre o uso de ferramentas de IA generativa no desenvolvimento e refatoração.

##### Tarefas Técnicas (Child Tasks)
* **Task 17.1:** Elaborar documento de apoio com perguntas frequentes e justificativas de arquitetura do projeto. *(Estimativa: 2.0h)*
* **Task 17.2:** Realizar rodada interna de simulação da avaliação oral entre os membros do grupo. *(Estimativa: 1.5h)*

---

### 🌐 FEATURE 09: Camada de Visualização Web (Thymeleaf / Interface de Apoio) ⚠️ [EM ESPERA / BAIXA PRIORIDADE]
* **Work Item Type:** `Feature`
* **Parent Epic:** `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
* **Tags:** `JavaAdvanced`, `Frontend`, `Thymeleaf`, `UI`, `Optional`
* **Start Date:** `2026-08-28`
* **Target Date:** `2026-08-29`
* **Descrição:** Desenvolvimento opcional de interface visual web com Thymeleaf caso a equipe opte por disponibilizar visualização web além do aplicativo mobile React Native.

#### 🔹 [PBI-18] Interface Web Básica de Autenticação e Cadastro com Formulários Validados
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Camada de Visualização Web (Thymeleaf / Interface de Apoio)`
* **State:** `New`
* **Priority:** `4 - Low`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `Frontend`, `Thymeleaf`, `UI`, `Sprint3`, `Opcional`

##### Descrição (História de Usuário)
> **Como** Usuário do PetGuardian,  
> **Eu quero** acessar páginas web de login e cadastro com formulários validados,  
> **Para que** eu consiga me autenticar e cadastrar diretamente pelo navegador.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Dependência `spring-boot-starter-thymeleaf` e layout base com fragments (header, footer, alerts).
- [ ] Página de login com feedback visual de credenciais inválidas.
- [ ] Formulário de cadastro com exibição de mensagens de erro vindas do Bean Validation.

##### Tarefas Técnicas (Child Tasks)
* **Task 18.1:** Configurar Thymeleaf e layout base com fragments. *(Estimativa: 2.0h)*
* **Task 18.2:** Desenvolver controllers web (`@Controller`) e páginas de Login e Cadastro. *(Estimativa: 2.5h)*

---

#### 🔹 [PBI-19] Telas Web de Operação dos Fluxos de Atendimentos e Tarefas do Pet
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Camada de Visualização Web (Thymeleaf / Interface de Apoio)`
* **State:** `New`
* **Priority:** `4 - Low`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `Frontend`, `Thymeleaf`, `Fluxos`, `Sprint3`, `Opcional`

##### Descrição (História de Usuário)
> **Como** Tutor e Veterinário,  
> **Eu quero** visualizar a lista de atendimentos e tarefas com botões de ação para concluir cuidados ou confirmar consultas,  
> **Para que** os dois fluxos de negócio possam ser operados visualmente via navegador.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Tela de listagem e agendamento de atendimentos com filtros por status.
- [ ] Tela de gerenciamento de tarefas do pet com botão para concluir e feedback de pontuação.

##### Tarefas Técnicas (Child Tasks)
* **Task 19.1:** Desenvolver tela de listagem e agendamento de atendimentos em Thymeleaf. *(Estimativa: 2.5h)*
* **Task 19.2:** Desenvolver tela de tarefas do pet com ação de conclusão e visualização de pontos. *(Estimativa: 2.5h)*

---

## 📊 4. Tabela de Visão Geral & Distribuição de Esforço

| ID do PBI | Título do PBI | Feature Pai | Story Points | Tasks (Horas) | Prioridade |
| :--- | :--- | :--- | :---: | :---: | :---: |
| **PBI-01** | Arquitetura de Exceções de Domínio e Global Exception Handler | FEAT-01 (SOLID & Qualidade) | **3 pts** | 4.5h | `1 - Critical` |
| **PBI-02** | Refatoração dos Serviços de Negócio com Princípios SOLID | FEAT-01 (SOLID & Qualidade) | **5 pts** | 6.5h | `1 - Critical` |
| **PBI-03** | Padronização de DTOs e Bean Validation Avançado (@Valid) | FEAT-02 (Validação & Swagger) | **3 pts** | 3.5h | `2 - High` |
| **PBI-04** | Documentação Interativa com SpringDoc OpenAPI 3 / Swagger | FEAT-02 (Validação & Swagger) | **3 pts** | 4.5h | `2 - High` |
| **PBI-05** | Arquitetura de Autenticação, UserDetails, BCrypt & JWT Service | FEAT-03 (Security Core JWT) | **5 pts** | 5.5h | `1 - Critical` |
| **PBI-06** | Endpoints de Autenticação e Registro com Emissão de JWT | FEAT-03 (Security Core JWT) | **3 pts** | 4.0h | `1 - Critical` |
| **PBI-07** | SecurityFilterChain Stateless, Filtro JWT e CORS para Mobile | FEAT-04 (Autorização RBAC) | **5 pts** | 5.0h | `1 - Critical` |
| **PBI-08** | Segurança Granular por Método (@PreAuthorize) e Handlers 401/403 | FEAT-04 (Autorização RBAC) | **3 pts** | 5.0h | `1 - Critical` |
| **PBI-09** | Configuração do Flyway e Migração Inicial DDL Pet-Centric (V1) | FEAT-05 (Flyway & Banco) | **5 pts** | 6.0h | `1 - Critical` |
| **PBI-10** | Migrações Incrementais, Clínicas 24h e Seeds de Segurança (V2/V3) | FEAT-05 (Flyway & Banco) | **3 pts** | 4.5h | `2 - High` |
| **PBI-11** | [Fluxo 1.1] Busca Clínicas 24h e Agendamento c/ Conflito Horário | FEAT-06 (Fluxo 1 Atendimentos) | **5 pts** | 6.0h | `1 - Critical` |
| **PBI-12** | [Fluxo 1.2] Triagem, Finalização Clínica e Histórico Vacinal do Pet | FEAT-06 (Fluxo 1 Atendimentos) | **5 pts** | 6.0h | `1 - Critical` |
| **PBI-13** | [Fluxo 2.1] Criação de Rotina Familiar e Expiração Automática | FEAT-07 (Fluxo 2 Gamificação) | **5 pts** | 5.5h | `1 - Critical` |
| **PBI-14** | [Fluxo 2.2] Conclusão de Rotina, Treinamentos, Score e Dicas IA | FEAT-07 (Fluxo 2 Gamificação) | **5 pts** | 6.5h | `1 - Critical` |
| **PBI-15** | Atualização Completa do README.md e Guia de Execução | FEAT-08 (Documentação/Vídeo) | **3 pts** | 3.0h | `2 - High` |
| **PBI-16** | Roteiro e Gravação do Vídeo Demonstrativo (Máx. 10 min) | FEAT-08 (Documentação/Vídeo) | **5 pts** | 5.5h | `1 - Critical` |
| **PBI-17** | Matriz de Argumentação e Preparação para a Avaliação Oral | FEAT-08 (Documentação/Vídeo) | **3 pts** | 3.5h | `2 - High` |
| ⚠️ **PBI-18** | Interface Web Básica de Autenticação e Cadastro *(Opcional)* | FEAT-09 (Frontend Web) | **5 pts** | 4.5h | `4 - Low` |
| ⚠️ **PBI-19** | Telas Web de Operação de Atendimentos e Tarefas *(Opcional)* | FEAT-09 (Frontend Web) | **5 pts** | 5.0h | `4 - Low` |
| **TOTAL** | **19 PBIs (17 Core + 2 Frontend Condicional) / 47 Tasks** | **9 Features** | **73 pts** | **85.0h** | — |

> 💡 **Nota de Planejamento de Capacidade:**  
> O escopo **Core da API (PBI-01 ao PBI-17)** totaliza **63 Story Points** (~75.5 horas de desenvolvimento e testes), distribuído de forma equilibrada entre os integrantes do grupo.

---

## 🚀 5. Ordem Recomendada de Execução (Sprint Roadmap)

1. **Fase 1 — Refatoração de Qualidade e Exceções (SOLID & Clean Code):** Executar `PBI-01` e `PBI-02`. Criar Global Exception Handler, exceções de domínio e refatorar Services com `@RequiredArgsConstructor` (Lombok) e atributos `private final`.
2. **Fase 2 — Validações de Contratos e Swagger/OpenAPI:** Executar `PBI-03` e `PBI-04`. Aplicar Bean Validation nos DTOs (`@Valid`) e documentar contratos no Swagger UI.
3. **Fase 3 — Segurança Core (Autenticação e Criptografia com JWT):** Executar `PBI-05` e `PBI-06`. Configurar BCrypt, UserDetails, emissão de JWT no `TokenService` e rotas de login/registro.
4. **Fase 4 — Segurança RBAC (Proteção de Rotas, Filtro JWT e CORS):** Executar `PBI-07` e `PBI-08`. Proteger controllers via `SecurityFilterChain` Stateless e `@PreAuthorize`, tratando 401/403 e liberando CORS para o app Mobile.
5. **Fase 5 — Fundação e Governança de Banco (Flyway):** Executar `PBI-09` e `PBI-10`. Criar migrações versionadas DDL V1 e Seeds V2/V3 com a base de dados já madura e protegida.
6. **Fase 6 — Fluxos Especializados de Negócio:** Executar `PBI-11` e `PBI-12` (Fluxo 1 - Clínicas 24h & Atendimentos) e `PBI-13` e `PBI-14` (Fluxo 2 - Rotina Familiar, Treinamento & Gamificação Pet-Centric).
7. **Fase 7 — Documentação, Vídeo e Preparação da Banca:** Executar `PBI-15`, `PBI-16` e `PBI-17`.
8. **Fase 8 — Camada de Visualização / Frontend Web (Se aprovado pelo time):** Executar `PBI-18` e `PBI-19` caso decidam entregar views web além do aplicativo Mobile.

---

## 📋 6. Guia para Criação no Azure Boards

1. Acesse sua organização no **Azure DevOps** (`dev.azure.com/{sua-organizacao}`).
2. Navegue até o projeto da Sprint 3 e abra a aba **Boards ➔ Backlogs**.
3. Selecione a visão de **Epics** e crie o Epic:
   * `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança Pet-Centric (Sprint 3)`
4. Alterne para a visão de **Features** e crie as 9 Features mapeadas na Seção 2 na ordem recomendada.
5. Em cada Feature, adicione os respectivos **Product Backlog Items (PBIs)** copiando o título, descrição (User Story), critérios de aceite e Story Points.
6. Dentro de cada PBI, clique em **Add Task** e cadastre as **Child Tasks** com suas descrições e estimativas em horas (*Original Estimate* / *Remaining*).
7. Garanta que o professor esteja adicionado à organização com o nível de acesso **Basic** e permissões de administrador do projeto.
