# 🤖 Regras de IA e Diretrizes de Engenharia - PetGuardian (Java-Advanced)

Este documento contém todas as instruções mandatórias, padrões arquiteturais e convenções de código que qualquer Agente de IA deve seguir rigorosamente ao trabalhar no projeto **Java-Advanced**.

---

## ⛔ 1. Restrições Estritas de Execução
- **NÃO RODAR O PROJETO NO TERMINAL:** Apenas analise, verifique e edite o código estaticamente. Nunca execute comandos no terminal para iniciar a JVM, build Gradle em daemon ou subir o servidor (`./gradlew bootRun`, etc.).
- **NÃO UTILIZAR `Locale.ROOT`:** Use `.toUpperCase()` ou `.toLowerCase()` padrão em qualquer normalização de texto.
- **NÃO FAZER VERIFICAÇÕES DE NULL REDUNDANTES:** Não adicionar checagens manuais de `null` ou ternários defensivos em atributos que possuem valor padrão (ex: `role = COMUM`) ou anotações `@NotNull`/`@NotBlank`.

---

## 📦 2. Padrões Obrigatórios para DTOs (Data Transfer Objects)
1. **Records do Java:** Todos os DTOs devem ser declarados como `public record NomeRequest(...)` ou `public record NomeResponse(...)`.
2. **DTOs Limpos e sem Poluição:**
   - **PROIBIDO `@Schema`:** Nunca utilize anotações `@Schema` do OpenAPI dentro dos records de DTO.
   - **PROIBIDO `message = "..."` em Validações Padrão:** Não passe mensagens customizadas dentro de `@NotBlank`, `@NotNull`, `@Email`, `@PastOrPresent`, `@FutureOrPresent`, `@Positive`, `@Size`. Mantenha apenas a anotação pura (ex: `@NotNull`, `@NotBlank`, `@Size(max = 50)`).
3. **Método `toEntity()` no Request DTO:**
   - Todo Request DTO que instancia uma entidade deve conter o método `public Entidade toEntity(...)` utilizando o padrão `Builder` do Lombok da entidade.
4. **Método `fromEntity()` no Response DTO:**
   - Todo Response DTO deve conter um método estático `public static NomeResponse fromEntity(Entidade e)` mapeando os atributos diretamente da entidade.

---

## 🧠 3. Padrões para a Camada de Serviços (`Service`)
1. **Atualizações Limpas (`update`):**
   - **PROIBIDO encadear múltiplos `.setX(...)`**: Utilize sempre o DTO existente para criar a entidade com `request.toEntity(...)`, atribua apenas o ID com `entidade.setId(id)` e salve com `repository.save(entidade)`.
   ```java
   // ✅ PADRÃO CORRETO
   @Transactional
   public Aula update(Long id, AulaRequest request) {
       findById(id);
       Modulo modulo = findModuloById(request.moduloId());
       Aula aula = request.toEntity(modulo);
       aula.setId(id);
       return aulaRepository.save(aula);
   }
   ```
2. **Tratamento de Recursos Não Encontrados:**
   - Todas as buscas por ID ou chave única em repositórios devem utilizar `.orElseThrow(() -> new ResourceNotFoundException("..."))`.
   - **NUNCA retornar `null`** a partir de um método de busca de entidade.
3. **Transações Declarativas:**
   - Métodos de escrita (`create`, `update`, `delete`) devem ser anotados com `@Transactional`.
   - Métodos de leitura com agregações complexas devem ser anotados com `@Transactional(readOnly = true)`.

---

## 🏛️ 4. Padrões para Entidades JPA & Mapeamento de FKs
1. **Campos Booleanos:**
   - Utilizar o tipo primitivo `boolean` (nunca wrapper `Boolean`).
   - Inicializar sempre com `@Builder.Default private boolean campo = false;`.
2. **Coleções (`Set` / `List`):**
   - Relacionamentos `@OneToMany` e `@ManyToMany` com coleções `Set` devem ser sempre inicializados com `@Builder.Default private Set<Entidade> nome = new HashSet<>();` para garantir *null-safety*.
3. **Nomenclatura Padrão de Chaves Estrangeiras (`@JoinColumn`):**
   - As colunas de FK devem seguir a paridade exata com o Oracle Data Modeler:
     - `Aula.modulo` $\rightarrow$ `@JoinColumn(name = "modulo_id_modulo", nullable = false)`
     - `Modulo.trilha` $\rightarrow$ `@JoinColumn(name = "trilha_id_trilha", nullable = false)`
     - `Historico.pet` $\rightarrow$ `@JoinColumn(name = "pet_id_pet", nullable = false)`
     - `Trilha.pet` $\rightarrow$ `@JoinColumn(name = "pet_id_pet", nullable = false)`
     - `Pet.raca` $\rightarrow$ `@JoinColumn(name = "raca_id_raca", nullable = false)`
     - `Tarefa.usuario` $\rightarrow$ `@JoinColumn(name = "usuario_id_usuario", nullable = false)`
     - `Tarefa.pet` $\rightarrow$ `@JoinColumn(name = "pet_id_pet", nullable = false)`
     - `Tarefa.status` $\rightarrow$ `@JoinColumn(name = "status_id_status", nullable = false)`
     - `Usuario.telefone` $\rightarrow$ `@JoinColumn(name = "telefone_id_telefone", nullable = false)`
4. **Obrigatoriedade de Usuário na Tarefa:**
   - Toda tarefa deve nascer vinculada a um usuário cuidador do pet (`usuario_id_usuario NOT NULL`), validado pelo `TarefaValidator`.
5. **Anotações Lombok Mandatórias:**
   - Todas as entidades JPA devem conter: `@Entity`, `@Table(...)`, `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`.
6. **Entidade `Telefone`:**
   - É tratada como dependência do agregado de `Usuario` (1:1 com `telefone_id_telefone UNIQUE`). Não criar Controller nem Service isolados para Telefone.

---

## 👥 5. Regras de Negócio do `UsuarioPet` (Care Circle)
1. **Chave Composta:** Utiliza `@EmbeddedId UsuarioPetId id` com mapeamento `@MapsId("usuarioId")` e `@MapsId("petId")`.
2. **Titularidade Única:** Cada pet deve possuir apenas 1 responsável principal ativo (`responsavelPrincipal = true`).
3. **Permissões (`UsuarioPetValidator`):**
   - Apenas o `responsavelPrincipal` pode convidar co-cuidadores ou transferir a titularidade.
   - O `responsavelPrincipal` não pode ser desvinculado sem antes transferir sua titularidade para outro membro.

---

## 🎮 6. Gamificação & Consultas JPQL Agregadas
1. **Cálculo de Pontos (Tarefas + Aulas):**
   - O cálculo da pontuação consolidada do Pet é realizado diretamente no banco via queries agregadas JPQL com `COALESCE(SUM(...), 0)` no `TarefaRepository` e `AulaRepository`.
   - Nunca carregar entidades em memória para somar pontos via loops Java.

---

## 🔒 7. Segurança, RBAC & Configuração OAuth2 RSA JWT
1. **Perfis de Usuário (`UsuarioRole`):**
   - O sistema possui dois perfis: `COMUM` (gratuito) e `PREMIUM` (assinante).
   - O token JWT inclui a claim `role` normalizada, convertida para `ROLE_COMUM` ou `ROLE_PREMIUM`.
2. **Proteção de Rotas Premium:**
   - Rotas de Trilhas (`/trilhas/**`), Módulos (`/modulos/**`) e Aulas (`/aulas/**`) são restritas exclusivamente a usuários `PREMIUM` via `.hasRole("PREMIUM")` e `@PreAuthorize("hasRole('PREMIUM')")`.
3. **Anotações Mandatórias do `SecurityConfig`:**
   - **NUNCA REMOVER** as seguintes anotações da classe `SecurityConfig`:
     - `@ConfigurationPropertiesScan`
     - `@EnableWebSecurity`
     - `@EnableMethodSecurity`
     - `@EnableConfigurationProperties(SecurityConfig.RsaKeyProperties.class)`
4. **Rotas Públicas Permitidas:**
   - `POST /login`
   - `POST /usuarios`
   - `/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html`
   - `/h2-console/**`

---

## 🗄️ 8. Migrações de Banco de Dados (Flyway)
- Toda alteração estrutural no schema do banco deve ser refletida nos scripts em `src/main/resources/db/migration/` (`V1__criar_tabelas.sql`, etc.).
- A configuração `spring.jpa.hibernate.ddl-auto=validate` deve ser sempre preservada.
