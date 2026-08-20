# 📋 Backlog Azure Boards — Sprint 3: Java Advanced (Spring Boot)
> **Projeto:** PetGuardian (Challenge FIAP 2026 - Clyvo Vet)  
> **Disciplina:** Advanced Business Development with Java (Java Advanced)  
> **Padrão:** Azure DevOps / Azure Boards (Scrum Process Template: *Epic ➔ Feature ➔ PBI ➔ Child Tasks*)  
> **Base de Requisitos:** Slides 20 a 22 da Apresentação Oficial FIAP (Sprint 3)

---

## 🎯 1. Resumo Executivo & Mapeamento de Requisitos da Sprint 3

| Requisito Oficial (Slides 20-22) | Peso / Pontos | Status no Backlog | Destaque Técnico / Observação |
| :--- | :---: | :---: | :--- |
| **1. Spring Security & Controle de Acesso** | **30 pts** | **Crítico (PBI-03 e PBI-04)** | Mínimo 2 perfis de usuário (`ADMIN`, `TUTOR`, `VETERINARIO`), criptografia BCrypt, proteção de rotas via `SecurityFilterChain` e `@PreAuthorize`. |
| **2. Flyway (Controle de Versão de BD)** | **20 pts** | **Crítico (PBI-01 e PBI-02)** | Migrações versionadas (`V1__...`, `V2__...`), DDL estruturado, carga de dados e histórico de migrações. |
| **3. Funcionalidades Completas (Fluxos Não-CRUD)** | **20 pts** | **Crítico (PBI-07 e PBI-08)** | Dois fluxos ponta a ponta complexos (Agendamento & Triagem de Atendimentos; Rede de Cuidado & Gamificação de Tarefas com expiração automática). |
| **4. Refatoração SOLID, DRY & Clean Code** | **Penalidades (-10 a -15 pts)** | **Alto (PBI-05 e PBI-06)** | Injeção por construtor, eliminação de `ResponseStatusException` solta nos services, Bean Validation em DTOs e Global Exception Handler. |
| **5. Documentação, Vídeo (10 min) & Avaliação Oral** | **Obrigatório** | **Alto (PBI-09, PBI-10 e PBI-11)** | README detalhado, gravação com demonstração das rotas/segurança/fluxos e guia de estudo para defesa individual com foco em IA e decisões de arquitetura. |
| **6. Frontend / Camada de Visualização** | **30 pts** | **Incerto / Final (PBI-12 e PBI-13)** | ⚠️ *Posicionado como as últimas tarefas do backlog devido à incerteza da equipe nesta sprint (priorização da API + Mobile).* |

---

## 👑 2. Hierarquia Geral do Backlog no Azure Boards

```
[EPIC] PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança (Sprint 3)
│
├── [FEATURE 01] Versionamento de Banco de Dados & Governança de Esquema (Flyway)
│   ├── [PBI-01] Configuração do Flyway e Migração Inicial DDL (V1)
│   └── [PBI-02] Migrações Incrementais de Dados Iniciais e Estrutura de Segurança (V2 e V3)
│
├── [FEATURE 02] Autenticação, Autorização & Controle de Acesso (Spring Security)
│   ├── [PBI-03] Arquitetura de Autenticação, Criptografia e Perfis de Usuário (RBAC)
│   └── [PBI-04] Proteção de Endpoints, Filtros de Segurança e Controle de Acesso Granular
│
├── [FEATURE 03] Refatoração Arquitetural, SOLID & Padrões de Qualidade (Clean Code & DRY)
│   ├── [PBI-05] Refatoração dos Serviços de Negócio e Tratamento Global de Exceções
│   └── [PBI-06] Padronização de DTOs, Bean Validation Avançado e Documentação Swagger/OpenAPI
│
├── [FEATURE 04] Implementação de Fluxos de Negócio Complexos (Regras Especializadas Não-CRUD)
│   ├── [PBI-07] [Fluxo 1] Ciclo de Agendamento, Triagem e Confirmação de Atendimento Veterinário
│   └── [PBI-08] [Fluxo 2] Gestão de Cuidados Colaborativos do Pet com Gamificação e Expiração Automática
│
├── [FEATURE 05] Documentação Técnica, Demonstração em Vídeo & Preparação para Avaliação Oral
│   ├── [PBI-09] Atualização Completa do README.md e Guia de Execução
│   ├── [PBI-10] Roteiro e Gravação do Vídeo Demonstrativo da Aplicação (Máx. 10 min)
│   └── [PBI-11] Matriz de Argumentação e Preparação para a Avaliação Oral Individual
│
└── [FEATURE 06] Camada de Visualização / Frontend Web (Thymeleaf / Interface de Apoio) ⚠️ [EM ESPERA / ÚLTIMA PRIORIDADE]
    ├── [PBI-12] Interface Web Básica de Autenticação e Navegação de Perfis (Formulários Validados)
    └── [PBI-13] Telas de Visualização dos Fluxos de Atendimentos e Tarefas do Pet
```

---

## 📦 3. Detalhamento dos Product Backlog Items (PBIs) e Child Tasks

---

### 🔹 FEATURE 01: Versionamento de Banco de Dados & Governança de Esquema (Flyway)

#### 🔹 [PBI-01] Configuração do Flyway e Migração Inicial DDL (V1)
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Versionamento de Banco de Dados & Governança de Esquema (Flyway)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `Spring-Boot`, `Flyway`, `Database`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Desenvolvedor Backend da equipe PetGuardian,  
> **Eu quero** configurar o Flyway no ecossistema Spring Boot e criar o script DDL inicial versionado (`V1__criar_tabelas_iniciais.sql`),  
> **Para que** todas as tabelas do sistema sejam criadas de forma automatizada, rastreável e sem depender do `ddl-auto=create-drop` do Hibernate em produção.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Dependência `flyway-core` (e suporte ao driver correspondente) adicionada corretamente ao `pom.xml`.
- [ ] Propriedades do Spring configuradas para habilitar o Flyway (`spring.flyway.enabled=true`, `spring.jpa.hibernate.ddl-auto=validate`).
- [ ] Script `V1__criar_tabelas_iniciais.sql` criado em `src/main/resources/db/migration/` com criação de todas as tabelas: `usuario`, `pet`, `usuario_pet`, `clinica`, `veterinario`, `tipo_atendimento`, `status`, `atendimento`, `tarefa`, `endereco`, `telefone`, `usuario_endereco`.
- [ ] Definição explícita de chaves primárias, chaves estrangeiras, restrições `NOT NULL` e `UNIQUE` consistentes com o modelo de domínio.
- [ ] A inicialização da aplicação executa as migrações com sucesso e a tabela `flyway_schema_history` é gerada sem falhas.

##### Tarefas Técnicas (Child Tasks)
* **Task 1.1:** Configurar dependências do Flyway no `pom.xml` e ajustar `application.properties` para `ddl-auto=validate`. *(Estimativa: 1.5h)*
  * *Descrição:* Adicionar dependências no Maven e configurar propriedades de execução do Flyway para validar o schema contra as entidades JPA.
* **Task 1.2:** Mapear e escrever o script DDL completo `V1__criar_tabelas_iniciais.sql`. *(Estimativa: 3.5h)*
  * *Descrição:* Criar todas as tabelas relacionais do PetGuardian com tipos de dados adequados, constraints e integridade referencial.
* **Task 1.3:** Testar a inicialização do Spring Boot validando a criação da tabela `flyway_schema_history`. *(Estimativa: 1.0h)*
  * *Descrição:* Subir a aplicação e inspecionar logs para garantir que a migração V1 foi aplicada com baseline limpo.

---

#### 🔹 [PBI-02] Migrações Incrementais de Dados Iniciais e Estrutura de Segurança (V2 e V3)
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Versionamento de Banco de Dados & Governança de Esquema (Flyway)`
* **State:** `Approved`
* **Priority:** `2 - High`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `Flyway`, `SeedData`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Desenvolvedor Backend e Administrador do Sistema,  
> **Eu quero** criar migrações incrementais do Flyway para inserir sementes de dados essenciais (status, tipos de atendimento, perfis e usuários administradores padrão),  
> **Para que** a aplicação já inicie pronta para testes e demonstração imediata de autenticação e fluxos.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Script `V2__popular_tabelas_dominio.sql` insere registros iniciais nas tabelas de domínio (`status`: `PENDENTE`, `CONCLUIDO`, `EXPIRADO`, `CANCELADO`, `CONFIRMADO`; `tipo_atendimento`: `CONSULTA`, `VACINACAO`, `EXAME`, `CIRURGIA`).
- [ ] Script `V3__adicionar_colunas_perfil_e_usuarios_iniciais.sql` adiciona a coluna `perfil`/`role` na tabela `usuario` e insere usuários de teste com senhas criptografadas via BCrypt (ao menos 1 Admin, 1 Veterinário e 1 Tutor/Cuidador).
- [ ] Migrações são idempotentes e executam sequencialmente sem quebrar o versionamento do banco.

##### Tarefas Técnicas (Child Tasks)
* **Task 2.1:** Elaborar script `V2__popular_tabelas_dominio.sql` com inserts das constantes do sistema. *(Estimativa: 1.5h)*
  * *Descrição:* Inserir dados prévios de status, raças e tipos de atendimento.
* **Task 2.2:** Elaborar script `V3__adicionar_colunas_perfil_e_usuarios_iniciais.sql` com suporte a perfis e seeds de usuários com senhas hash BCrypt. *(Estimativa: 2.0h)*
  * *Descrição:* Inserir usuários com diferentes roles (`ROLE_ADMIN`, `ROLE_TUTOR`, `ROLE_VETERINARIO`) para permitir testes de segurança.
* **Task 2.3:** Validar integridade referencial e execução sequencial no banco de dados. *(Estimativa: 1.0h)*
  * *Descrição:* Executar os testes de inicialização e verificar os dados inseridos nas tabelas.

---

### 🔹 FEATURE 02: Autenticação, Autorização & Controle de Acesso (Spring Security)

#### 🔹 [PBI-03] Arquitetura de Autenticação, Criptografia e Perfis de Usuário (RBAC)
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Autenticação, Autorização & Controle de Acesso (Spring Security)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `8`
* **Tags:** `JavaAdvanced`, `SpringSecurity`, `BCrypt`, `RBAC`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Usuário do PetGuardian (Tutor, Veterinário ou Administrador),  
> **Eu quero** me autenticar de forma segura através de credenciais criptografadas e possuir um perfil de acesso bem definido,  
> **Para que** minhas informações fiquem protegidas e eu acesse apenas os recursos condizentes com meu perfil no sistema.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Dependência `spring-boot-starter-security` adicionada ao `pom.xml`.
- [ ] Criação do `enum PerfilUsuario` (ou `Role`: `ADMIN`, `TUTOR`, `VETERINARIO`) e associação na entidade `Usuario` implementando `UserDetails`.
- [ ] Bean de `PasswordEncoder` configurado utilizando `BCryptPasswordEncoder` (eliminação total de senhas em texto puro).
- [ ] Implementação de `UserDetailsService` customizado para buscar o usuário por email no banco de dados com suas devidas `GrantedAuthority`.
- [ ] Endpoint de registro (`/auth/register`) e login (`/auth/login`) ou suporte a autenticação básica/JWT/Session configurado e funcional.

##### Tarefas Técnicas (Child Tasks)
* **Task 3.1:** Adicionar dependência do Spring Security e estruturar o enum `Perfil` / `Role`. *(Estimativa: 1.5h)*
  * *Descrição:* Atualizar `pom.xml`, entidade `Usuario` e DTOs de cadastro/resposta.
* **Task 3.2:** Implementar `UserDetails` na entidade `Usuario` e criar o serviço `UserDetailsServiceImpl`. *(Estimativa: 2.5h)*
  * *Descrição:* Implementar os métodos de authorities, enabled, accountNonExpired e busca por email no `UsuarioRepository`.
* **Task 3.3:** Criar classe de configuração `SecurityConfig` com bean de `BCryptPasswordEncoder` e `AuthenticationManager`. *(Estimativa: 2.0h)*
  * *Descrição:* Configurar os beans fundamentais de criptografia e gerenciamento de autenticação.
* **Task 3.4:** Refatorar `UsuarioService` para realizar hash BCrypt na criação e atualização de senhas. *(Estimativa: 2.0h)*
  * *Descrição:* Garantir que nenhuma senha seja persistida sem hash criptográfico.

---

#### 🔹 [PBI-04] Proteção de Endpoints, Filtros de Segurança e Controle de Acesso Granular
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Autenticação, Autorização & Controle de Acesso (Spring Security)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `8`
* **Tags:** `JavaAdvanced`, `SpringSecurity`, `Authorization`, `SecurityFilterChain`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Administrador do Sistema e Arquiteto de Software,  
> **Eu quero** proteger as rotas da aplicação através do `SecurityFilterChain` e anotações `@PreAuthorize`,  
> **Para que** usuários não autenticados ou com perfil não autorizado recebam status HTTP 401 Unauthorized ou 403 Forbidden ao tentar acessar recursos restritos.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Rotas públicas explicitamente liberadas (`/auth/**`, `/h2-console/**`, `/swagger-ui/**`, `/v3/api-docs/**`, `/actuator/health`).
- [ ] Rotas administrativas restritas exclusivamente a `ADMIN` (ex: gerenciamento de clínicas, exclusão de usuários e relatórios globais).
- [ ] Rotas de veterinários restritas a `VETERINARIO` ou `ADMIN` (ex: prescrição de tarefas clínicas, atualização de diagnósticos e laudos).
- [ ] Rotas de tutores restritas a `TUTOR` ou `ADMIN` (ex: conclusão de tarefas diárias, agendamento de consultas para seus pets).
- [ ] Uso de `@EnableMethodSecurity` e validação com `@PreAuthorize("hasRole('ADMIN')")` ou regras na `SecurityFilterChain`.
- [ ] Tratamento customizado de `AuthenticationEntryPoint` (401) e `AccessDeniedHandler` (403) retornando JSON padronizado.

##### Tarefas Técnicas (Child Tasks)
* **Task 4.1:** Configurar a cadeia de filtros `SecurityFilterChain` em `SecurityConfig` com mapeamento de rotas e permissões por perfil. *(Estimativa: 3.0h)*
  * *Descrição:* Declarar regras de `authorizeHttpRequests` distinguindo rotas públicas e privadas por role.
* **Task 4.2:** Habilitar segurança por método com `@EnableMethodSecurity` e anotar controllers com `@PreAuthorize`. *(Estimativa: 2.5h)*
  * *Descrição:* Aplicar `@PreAuthorize` nos controllers `ClinicaController`, `VeterinarioController`, `TarefaController`, `AtendimentoController` e `UsuarioController`.
* **Task 4.3:** Criar handlers customizados de erro de segurança (401 Unauthorized e 403 Forbidden). *(Estimativa: 2.0h)*
  * *Descrição:* Implementar `CustomAuthenticationEntryPoint` e `CustomAccessDeniedHandler` integrados ao DTO de erro global.

---

### 🔹 FEATURE 03: Refatoração Arquitetural, SOLID & Padrões de Qualidade (Clean Code & DRY)

#### 🔹 [PBI-05] Refatoração dos Serviços de Negócio e Tratamento Global de Exceções
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Refatoração Arquitetural, SOLID & Padrões de Qualidade (Clean Code & DRY)`
* **State:** `Approved`
* **Priority:** `2 - High`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `SOLID`, `CleanCode`, `DRY`, `GlobalExceptionHandler`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Desenvolvedor Backend,  
> **Eu quero** refatorar os Services para eliminar acoplamento indevido, métodos com múltiplas responsabilidades e lançamentos diretos de `ResponseStatusException`,  
> **Para que** o código respeite os princípios SOLID (especialmente SRP e DIP), DRY e evite penalidades de até -15 pontos na avaliação.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Eliminação de `ResponseStatusException` de dentro da camada de serviço (`AtendimentoService`, `PetService`, etc.), substituindo por exceções de domínio expressivas (`ResourceNotFoundException`, `BusinessException`, `ConflictException`).
- [ ] `GlobalExceptionHandler` estendido para capturar e mapear todas as exceções de domínio com status HTTP e corpo JSON padronizado.
- [ ] Injeção de dependências estritamente por construtor com `@RequiredArgsConstructor` (Lombok) e atributos `private final`.
- [ ] Métodos de serviços modularizados com nomes autoexplicativos, sem duplicação de lógica de validação (DRY) e sem comentários desnecessários substituindo código limpo.

##### Tarefas Técnicas (Child Tasks)
* **Task 5.1:** Criar classes de exceção de domínio personalizadas (`BusinessException`, `ConflictException`, `UnauthorizedOperationException`). *(Estimativa: 1.5h)*
  * *Descrição:* Criar exceções não checadas específicas para regras de negócio do PetGuardian.
* **Task 5.2:** Refatorar `AtendimentoService` e `TarefaService` removendo `ResponseStatusException` e aplicando SRP. *(Estimativa: 2.5h)*
  * *Descrição:* Substituir tratamento HTTP embutido por exceções de domínio e extrair métodos privados auxiliares claros.
* **Task 5.3:** Atualizar `GlobalExceptionHandler` com captura de `AccessDeniedException`, `BadCredentialsException`, `BusinessException` e `ConflictException`. *(Estimativa: 2.0h)*
  * *Descrição:* Centralizar e padronizar o payload JSON de retorno de erro para o frontend/mobile.

---

#### 🔹 [PBI-06] Padronização de DTOs, Bean Validation Avançado e Documentação Swagger/OpenAPI
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Refatoração Arquitetural, SOLID & Padrões de Qualidade (Clean Code & DRY)`
* **State:** `Approved`
* **Priority:** `2 - High`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `Validation`, `OpenAPI`, `Swagger`, `DTO`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Desenvolvedor e Consumidor da API (Mobile/Frontend),  
> **Eu quero** que todas as requisições de entrada sejam estritamente validadas via Bean Validation e documentadas no Swagger/OpenAPI,  
> **Para que** entradas inválidas sejam rejeitadas com mensagens claras (400 Bad Request) e a API possua contrato público consistente.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Todos os DTOs de Request anotados com validações precisas (`@NotBlank`, `@NotNull`, `@Size`, `@Email`, `@FutureOrPresent`, `@Positive`).
- [ ] Controllers com anotação `@Valid` em todos os parâmetros `@RequestBody`.
- [ ] Documentação do Swagger atualizada com descrições das operações, códigos de resposta (200, 201, 400, 401, 403, 404), esquemas de segurança (Bearer Auth / Basic Auth) e exemplos de payload.
- [ ] Ausência de schemas nulos ou rotas quebradas no Swagger UI (`/swagger-ui/index.html`).

##### Tarefas Técnicas (Child Tasks)
* **Task 6.1:** Revisar e aplicar Bean Validation completo em todos os DTOs (`TarefaRequest`, `AtendimentoRequest`, `UsuarioRequest`, `PetRequest`, `ClinicaRequest`). *(Estimativa: 2.5h)*
  * *Descrição:* Adicionar constraints de validação e mensagens customizadas em português.
* **Task 6.2:** Configurar documentação OpenAPI 3 em `OpenApiConfig` com esquema de segurança (SecurityScheme / JWT ou Basic). *(Estimativa: 2.0h)*
  * *Descrição:* Adicionar metadados do projeto, contato da equipe e configuração do botão "Authorize" no Swagger.
* **Task 6.3:** Enriquecer os Controllers com anotações `@Operation`, `@ApiResponse` e `@Tag` do SpringDoc. *(Estimativa: 2.0h)*
  * *Descrição:* Documentar status HTTP e modelos de dados para facilitar a integração com a disciplina Mobile.

---

### 🔹 FEATURE 04: Implementação de Fluxos de Negócio Complexos (Regras Especializadas Não-CRUD)

#### 🔹 [PBI-07] [Fluxo 1] Ciclo de Agendamento, Triagem e Confirmação de Atendimento Veterinário
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Implementação de Fluxos de Negócio Complexos (Regras Especializadas Não-CRUD)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `8`
* **Tags:** `JavaAdvanced`, `BusinessFlow`, `Atendimento`, `RegraDeNegocio`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Tutor de um pet e Veterinário da clínica Clyvo,  
> **Eu quero** executar o fluxo completo de solicitação, validação de conflito de agenda, confirmação e finalização de um atendimento com laudo e diagnóstico,  
> **Para que** o sistema gerencie o ciclo completo de saúde clínica do pet (atendendo ao requisito obrigatório de fluxo não-CRUD).

##### Critérios de Aceite (Acceptance Criteria)
- [ ] **Passo 1 (Solicitação):** Tutor solicita agendamento de atendimento informando pet, clínica, veterinário, tipo de atendimento e data/hora futura.
- [ ] **Validação de Conflito de Horário:** O sistema valida se o veterinário já possui outro atendimento agendado no mesmo intervalo de horário (não permite agendamento duplicado).
- [ ] **Validação de Vínculo:** O sistema verifica se o tutor solicitante possui vínculo ativo com o pet na tabela `usuario_pet`.
- [ ] **Passo 2 (Confirmação/Triagem):** Veterinário ou Clínica altera o status de `PENDENTE` para `CONFIRMADO` ou `CANCELADO` (com justificativa).
- [ ] **Passo 3 (Finalização/Prontuário):** Veterinário conclui o atendimento inserindo diagnóstico/observações clínicas, atualizando o status para `CONCLUIDO` e data de encerramento.
- [ ] Transição de status inválida lança `BusinessException` (ex: não é possível concluir um atendimento cancelado ou no futuro).

##### Tarefas Técnicas (Child Tasks)
* **Task 7.1:** Implementar métodos de verificação de disponibilidade no `AtendimentoRepository` (query para conflito de agenda por veterinário e data/hora). *(Estimativa: 2.0h)*
  * *Descrição:* Criar consultas customizadas para validar sobreposição de horários de atendimento.
* **Task 7.2:** Criar método de negócio `agendarAtendimento` com validações de permissão do tutor e disponibilidade do veterinário. *(Estimativa: 3.0h)*
  * *Descrição:* Implementar regra transacional no `AtendimentoService` com checagem de regras de negócio.
* **Task 7.3:** Implementar endpoints especializados de transição de status (`/atendimentos/{id}/confirmar`, `/atendimentos/{id}/cancelar`, `/atendimentos/{id}/finalizar`). *(Estimativa: 2.5h)*
  * *Descrição:* Criar endpoints no `AtendimentoController` com DTOs específicos de justificativa e laudo clínico.

---

#### 🔹 [PBI-08] [Fluxo 2] Gestão de Cuidados Colaborativos do Pet com Gamificação e Expiração Automática
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Implementação de Fluxos de Negócio Complexos (Regras Especializadas Não-CRUD)`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `8`
* **Tags:** `JavaAdvanced`, `BusinessFlow`, `Gamificacao`, `Tarefas`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Veterinário e Tutor/Cuidador do PetGuardian,  
> **Eu quero** prescrever tarefas diárias de cuidados, permitir que os co-cuidadores concluam as tarefas ganhando pontos de gamificação e expirar automaticamente tarefas atrasadas,  
> **Para que** haja engajamento na rede de cuidados colaborativos e rastreamento fidedigno da saúde diária do pet.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] **Prescrição:** Apenas veterinários autorizados podem prescrever tarefas clínicas associadas a um pet, definindo prazo, descrição e pontuação (`pontos_tarefa`).
- [ ] **Execução / Conclusão:** Apenas usuários vinculados ao pet (`usuario_pet`) podem marcar a tarefa como `CONCLUIDA`.
- [ ] **Gamificação:** Ao concluir a tarefa, os pontos são creditados ao usuário executor e o total acumulado pode ser consultado via endpoint especializado (`/usuarios/{id}/pontos-totais` ou `/usuarios/{id}/rede-cuidado`).
- [ ] **Expiração Automática:** Tarefas pendentes com prazo ultrapassado mudam automaticamente para o status `EXPIRADO` antes de qualquer listagem ou tentativa de conclusão.
- [ ] Tentativa de conclusão de tarefa já expirada ou já concluída lança erro de negócio explicativo (400 Bad Request).

##### Tarefas Técnicas (Child Tasks)
* **Task 8.1:** Aprimorar o mecanismo de expiração de tarefas atrasadas no `TarefaService` e `TarefaRepository`. *(Estimativa: 2.0h)*
  * *Descrição:* Garantir que a rotina de expiração seja atômica e eficiente via query de update condicional.
* **Task 8.2:** Implementar fluxo transacional de conclusão de tarefa (`concluirTarefa`) com atribuição de pontos e registro de data de conclusão. *(Estimativa: 2.5h)*
  * *Descrição:* Validar se o usuário executor faz parte da rede de cuidadores do pet e aplicar a pontuação.
* **Task 8.3:** Criar endpoint agregado `/usuarios/{id}/rede-cuidado` e ranking de pontuação por pet. *(Estimativa: 2.5h)*
  * *Descrição:* Desenvolver DTO consolidado trazendo co-cuidadores, pets compartilhados, tarefas pendentes e histórico de pontos.

---

### 🔹 FEATURE 05: Documentação Técnica, Demonstração em Vídeo & Preparação para Avaliação Oral

#### 🔹 [PBI-09] Atualização Completa do README.md e Guia de Execução
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Documentação Técnica, Demonstração em Vídeo & Preparação para Avaliação Oral`
* **State:** `Approved`
* **Priority:** `2 - High`
* **Effort (Story Points):** `3`
* **Tags:** `JavaAdvanced`, `Documentation`, `README`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Professor avaliador e Desenvolvedor da equipe,  
> **Eu quero** um README.md completo e organizado no repositório GitHub,  
> **Para que** qualquer pessoa consiga clonar, configurar variáveis de ambiente, executar as migrações do Flyway, autenticar com as credenciais de teste e testar os fluxos da aplicação.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Instruções claras de pré-requisitos (Java 17, Maven 3.8+).
- [ ] Guia passo a passo de como compilar e rodar a aplicação (`./mvnw spring-boot:run` ou `mvn clean install`).
- [ ] Tabela com os usuários e senhas de teste pré-cadastrados (Admin, Veterinário, Tutor).
- [ ] Documentação dos endpoints principais, URLs do Swagger UI, H2 Console e Actuator Health.
- [ ] Explicação detalhada da arquitetura adotada, princípios SOLID aplicados e fluxos complexos implementados.

##### Tarefas Técnicas (Child Tasks)
* **Task 9.1:** Atualizar a seção de introdução, tecnologias e arquitetura do `README.md`. *(Estimativa: 1.5h)*
  * *Descrição:* Incluir detalhes das novas camadas de segurança, migrações e entidades da Sprint 3.
* **Task 9.2:** Documentar credenciais de acesso para testes, endpoints de autenticação e links do Swagger/Actuator. *(Estimativa: 1.5h)*
  * *Descrição:* Detalhar comandos de inicialização e roteiro de testes via Swagger/Insomnia.

---

#### 🔹 [PBI-10] Roteiro e Gravação do Vídeo Demonstrativo da Aplicação (Máx. 10 min)
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Documentação Técnica, Demonstração em Vídeo & Preparação para Avaliação Oral`
* **State:** `Approved`
* **Priority:** `1 - Critical`
* **Effort (Story Points):** `5`
* **Tags:** `JavaAdvanced`, `Video`, `Demonstracao`, `Sprint3`

##### Descrição (História de Usuário)
> **Como** Equipe do Projeto PetGuardian,  
> **Eu quero** gravar e publicar um vídeo de até 10 minutos com áudio claro demonstrando o funcionamento da aplicação,  
> **Para que** o professor valide a execução em tempo de execução de todos os requisitos obrigatórios da 3ª Sprint.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Duração do vídeo de no máximo 10 minutos, com gravação em alta resolução e áudio explicativo falado.
- [ ] Demonstração das migrações do Flyway executando na inicialização do Spring Boot.
- [ ] Demonstração do Spring Security: tentativa de acesso não autenticado (401/403) e login com diferentes perfis (`ADMIN`, `VETERINARIO`, `TUTOR`).
- [ ] Demonstração prática dos dois fluxos de negócio complexos em funcionamento ponta a ponta.
- [ ] Demonstração de tratamento de exceções amigável (validações de formulário e erros de regra de negócio).
- [ ] Link público ou não-listado do YouTube inserido no README do repositório.

##### Tarefas Técnicas (Child Tasks)
* **Task 10.1:** Criar roteiro técnico sequencial para a gravação do vídeo (Timebox: 10 min). *(Estimativa: 1.5h)*
  * *Descrição:* Mapear a ordem exata de telas/endpoints e falas dos integrantes da equipe.
* **Task 10.2:** Gravar a demonstração prática da aplicação (Flyway + Security + Fluxos + Validações). *(Estimativa: 2.5h)*
  * *Descrição:* Executar os cenários de teste demonstrando o funcionamento da API e retorno das respostas.
* **Task 10.3:** Editar, realizar upload no YouTube e adicionar o link ao `README.md`. *(Estimativa: 1.5h)*
  * *Descrição:* Conferir qualidade de áudio/vídeo e publicar link acessível.

---

#### 🔹 [PBI-11] Matriz de Argumentação e Preparação para a Avaliação Oral Individual
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Documentação Técnica, Demonstração em Vídeo & Preparação para Avaliação Oral`
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
- [ ] Relatório claro e transparente sobre se e como ferramentas de IA generativa foram utilizadas no desenvolvimento e refatoração.

##### Tarefas Técnicas (Child Tasks)
* **Task 11.1:** Elaborar documento de apoio com perguntas frequentes e justificativas de arquitetura do projeto. *(Estimativa: 2.0h)*
  * *Descrição:* Sintetizar os conceitos de Spring Security, Flyway, SOLID e transações para consulta rápida.
* **Task 11.2:** Realizar rodada interna de simulação da avaliação oral entre os membros do grupo. *(Estimativa: 1.5h)*
  * *Descrição:* Treinar respostas sobre trechos do código e decisões tomadas no backend.

---

### 🔹 FEATURE 06: Camada de Visualização / Frontend Web (Thymeleaf / Interface de Apoio) ⚠️ [EM ESPERA / ÚLTIMA PRIORIDADE]

> ⚠️ **Nota de Planejamento da Sprint:**  
> A implementação da camada de visualização Web (frontend) está listada nos requisitos gerais (Slide 20 - 30 pts), porém o time ainda está avaliando se fará telas web completas ou se focará a experiência de interface no aplicativo Mobile (React Native) integrado à API Spring Boot. Por essa razão, os PBIs abaixo foram configurados com prioridade mais baixa e posicionados ao final da fila de implementação.

#### 🔹 [PBI-12] Interface Web Básica de Autenticação e Navegação de Perfis (Formulários Validados)
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Camada de Visualização / Frontend Web (Thymeleaf / Interface de Apoio)`
* **State:** `New`
* **Priority:** `4 - Low`
* **Effort (Story Points):** `8`
* **Tags:** `JavaAdvanced`, `Frontend`, `Thymeleaf`, `UI`, `Sprint3`, `Opcional`

##### Descrição (História de Usuário)
> **Como** Usuário do PetGuardian,  
> **Eu quero** acessar páginas web de login, cadastro e dashboard inicial com formulários validados,  
> **Para que** eu consiga interagir com o sistema diretamente pelo navegador caso o frontend web seja adotado nesta sprint.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Adição da dependência `spring-boot-starter-thymeleaf` e `thymeleaf-extras-springsecurity6` (se aplicável).
- [ ] Página de login com feedback visual de credenciais inválidas.
- [ ] Formulário de cadastro de usuário e pet com exibição de erros de validação vindos do Bean Validation.
- [ ] Header/Navbar dinâmico que altera opções com base no perfil do usuário logado (`sec:authorize`).

##### Tarefas Técnicas (Child Tasks)
* **Task 12.1:** Configurar Thymeleaf e layout base com fragments (header, footer, alerts). *(Estimativa: 2.5h)*
  * *Descrição:* Criar estrutura HTML/CSS de templates reutilizáveis.
* **Task 12.2:** Desenvolver controllers web (`@Controller`) e páginas de Login e Cadastro de Usuário. *(Estimativa: 3.0h)*
  * *Descrição:* Criar rotas web para renderização das views com binding de formulário e validação de erros.

---

#### 🔹 [PBI-13] Telas de Visualização dos Fluxos de Atendimentos e Tarefas do Pet
* **Work Item Type:** `Product Backlog Item`
* **Parent Feature:** `Camada de Visualização / Frontend Web (Thymeleaf / Interface de Apoio)`
* **State:** `New`
* **Priority:** `4 - Low`
* **Effort (Story Points):** `8`
* **Tags:** `JavaAdvanced`, `Frontend`, `Thymeleaf`, `Fluxos`, `Sprint3`, `Opcional`

##### Descrição (História de Usuário)
> **Como** Tutor e Veterinário,  
> **Eu quero** visualizar a lista de atendimentos e a lista de tarefas com botões de ação para concluir cuidados ou confirmar consultas,  
> **Para que** os dois fluxos de negócio principais possam ser operados visualmente via navegador.

##### Critérios de Aceite (Acceptance Criteria)
- [ ] Tela de listagem e agendamento de atendimentos com filtros por status e seleção de veterinário.
- [ ] Tela de gerenciamento de tarefas do pet com botão para concluir e feedback de pontuação acumulada.
- [ ] Validação visual e bloqueio de ações não permitidas para o perfil logado.

##### Tarefas Técnicas (Child Tasks)
* **Task 13.1:** Desenvolver tela de listagem e agendamento de atendimentos em Thymeleaf. *(Estimativa: 3.0h)*
  * *Descrição:* Criar página com formulário de agendamento e tabela de atendimentos.
* **Task 13.2:** Desenvolver tela de tarefas do pet com ação de conclusão e visualização de pontos. *(Estimativa: 3.0h)*
  * *Descrição:* Criar visualização de tarefas pendentes/concluídas com interação de usuário.

---

## 📊 4. Tabela de Visão Geral & Distribuição de Esforço

| ID do PBI | Título do PBI | Área / Feature | Story Points | Estimativa Tasks | Prioridade |
| :--- | :--- | :--- | :---: | :---: | :---: |
| **PBI-01** | Configuração do Flyway e Migração Inicial DDL (V1) | Banco de Dados / Flyway | **5 pts** | 6.0h | `1 - Critical` |
| **PBI-02** | Migrações Incrementais de Dados Iniciais e Segurança (V2 e V3) | Banco de Dados / Flyway | **3 pts** | 4.5h | `2 - High` |
| **PBI-03** | Arquitetura de Autenticação, Criptografia e Perfis (RBAC) | Segurança / Spring Security | **8 pts** | 8.0h | `1 - Critical` |
| **PBI-04** | Proteção de Endpoints, Filtros e Controle Granular | Segurança / Spring Security | **8 pts** | 7.5h | `1 - Critical` |
| **PBI-05** | Refatoração de Serviços e Tratamento Global de Exceções | Arquitetura / SOLID & Clean Code | **5 pts** | 6.0h | `2 - High` |
| **PBI-06** | Padronização de DTOs, Bean Validation e Swagger/OpenAPI | Validação & Documentação | **5 pts** | 6.5h | `2 - High` |
| **PBI-07** | [Fluxo 1] Agendamento, Triagem e Confirmação de Atendimento | Regra de Negócio Não-CRUD | **8 pts** | 7.5h | `1 - Critical` |
| **PBI-08** | [Fluxo 2] Cuidados Colaborativos, Gamificação e Expiração | Regra de Negócio Não-CRUD | **8 pts** | 7.0h | `1 - Critical` |
| **PBI-09** | Atualização Completa do README.md e Guia de Execução | Documentação / Repositório | **3 pts** | 3.0h | `2 - High` |
| **PBI-10** | Roteiro e Gravação do Vídeo Demonstrativo (Máx. 10 min) | Avaliação / Demonstração | **5 pts** | 5.5h | `1 - Critical` |
| **PBI-11** | Matriz de Argumentação e Preparação para Avaliação Oral | Avaliação / Defesa Técnica | **3 pts** | 3.5h | `2 - High` |
| ⚠️ **PBI-12** | Interface Web de Autenticação e Perfis *(Incerto)* | Frontend / Thymeleaf | **8 pts** | 5.5h | `4 - Low` |
| ⚠️ **PBI-13** | Telas de Atendimentos e Tarefas do Pet *(Incerto)* | Frontend / Thymeleaf | **8 pts** | 6.0h | `4 - Low` |
| **TOTAL** | **13 PBIs (11 Core + 2 Frontend Condicional) / 33 Tasks** | — | **77 pts** | **76.5h** | — |

> 💡 **Nota de Planejamento de Capacidade:**  
> Sem o Frontend Web (foco em API + Segurança + Flyway + Fluxos + Mobile Integration), o esforço do Core é de **61 Story Points** (~65 horas de desenvolvimento e testes), distribuídos de forma equilibrada entre os integrantes do grupo.

---

## 🚀 5. Ordem Recomendada de Execução (Sprint Roadmap)

1. **Fase 1 — Fundação e Governança de Dados (Flyway):** Executar `PBI-01` e `PBI-02`. O banco de dados passa a ser gerenciado e versionado de forma estável.
2. **Fase 2 — Segurança e Controle de Acesso (Spring Security):** Executar `PBI-03` e `PBI-04`. Proteger as rotas, cadastrar perfis (`ADMIN`, `VETERINARIO`, `TUTOR`) e criptografar senhas com BCrypt.
3. **Fase 3 — Refatoração de Qualidade e Boas Práticas (SOLID / Clean Code):** Executar `PBI-05` e `PBI-06`. Eliminar `ResponseStatusException` solta, organizar DTOs com validações `@Valid` e atualizar o Swagger.
4. **Fase 4 — Fluxos Especializados de Negócio:** Executar `PBI-07` (Atendimentos & Conflitos de Agenda) e `PBI-08` (Cuidados Colaborativos & Gamificação de Tarefas).
5. **Fase 5 — Documentação, Vídeo e Preparação da Banca:** Executar `PBI-09`, `PBI-10` e `PBI-11`.
6. **Fase 6 — Camada de Visualização / Frontend Web (Se aprovado pelo time):** Executar `PBI-12` e `PBI-13` caso decidam entregar views web além do aplicativo Mobile.

---

## 📋 6. Guia para Criação no Azure Boards

1. Acesse sua organização no **Azure DevOps** (`dev.azure.com/{sua-organizacao}`).
2. Navegue até o projeto da Sprint 3 e abra a aba **Boards ➔ Backlogs**.
3. Selecione a visão de **Epics** e crie o Epic:
   * `PetGuardian - Evolução da Arquitetura Spring Boot, Segurança e Governança (Sprint 3)`
4. Alterne para a visão de **Features** e crie as 6 Features mapeadas na Seção 2.
5. Em cada Feature, adicione os respectivos **Product Backlog Items (PBIs)** copiando o título, descrição (User Story), critérios de aceite e Story Points.
6. Dentro de cada PBI, clique em **Add Task** e cadastre as **Child Tasks** com suas descrições e estimativas em horas (*Original Estimate* / *Remaining*).
7. Garanta que o professor esteja adicionado à organização com o nível de acesso **Basic** e permissões de administrador do projeto.
