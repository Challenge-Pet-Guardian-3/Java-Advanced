# 🐾 PetGuardian

> Aplicação colaborativa de cuidado de pets com gamificação, integrando duas entregas acadêmicas FIAP (2TDSPG):
> **Java Advanced** (aplicação Spring Boot completa) e **Disruptive Architectures: IoT, IoB & Generative IA** (módulo de IA — Challenge Clyvo Vet).

---

## 👥 Integrantes

| Nome | RM | Turma | GitHub | LinkedIn |
| :--- | :---: | :---: | :--- | :--- |
| **Enzo Okuizumi** | **561432** | 2TDSPG | [EnzoOkuizumiFiap](https://github.com/EnzoOkuizumiFiap) | [Enzo Okuizumi](https://www.linkedin.com/in/enzo-okuizumi-b60292256/) |
| **Gustavo Okada** | **563428** | 2TDSPG | [Gdev3356](https://github.com/Gdev3356) | [Gustavo Okada](https://www.linkedin.com/in/gustavo-okada-53a3b8359/) |
| **Lucas Barros Gouveia** | **566422** | 2TDSPG | [LuzBGouveia](https://github.com/LuzBGouveia) | [Lucas Barros Gouveia](https://www.linkedin.com/in/lucas-barros-gouveia-09b147355/) |
| **Luna de Carvalho Guimarães** | **562290** | 2TDSPG | [lunaguima](https://github.com/lunaguima) | [Luna Guimarães](https://www.linkedin.com/in/luna-guimar%C3%A3es-b0ba82309/) |
| **Milton Marcelino** | **564836** | 2TDSPG | [MiltonMarcelino](https://github.com/MiltonMarcelino) | [Milton Marcelino](http://linkedin.com/in/milton-marcelino-250298142) |

---

## 🔗 Repositório & Vídeos

* **Repositório GitHub:** [https://github.com/Challenge-Pet-Guardian-3/Disruptive-Architectures-IoT-IoB-IA](https://github.com/Challenge-Pet-Guardian-3/Disruptive-Architectures-IoT-IoB-IA)
* **Vídeo demonstração — Java Advanced (máx. 10 min):** *(adicionar link após a gravação)*
* **Vídeo pitch — Disruptive Architectures (máx. 5 min):** *(adicionar link após a gravação)*

---

## 💡 Visão Geral do Projeto

O PetGuardian é um aplicativo colaborativo de cuidado de pets: famílias inteiras (múltiplos cuidadores) gerenciam tarefas, vacinas, trilhas de aprendizado e agora contam com um assistente de orientação inteligente. O backend é uma aplicação **Spring Boot** completa, com frontend em **React Native**, banco versionado via **Flyway**, autenticação via **Spring Security + JWT**, e um módulo de **IA baseada em motor de regras** para orientações preventivas.

---

## 🛠️ Tecnologias Utilizadas

- **Backend:** Java 21, Spring Boot, Spring Data JPA, Spring Security, JWT
- **Banco de Dados:** H2 (file-based), Flyway para versionamento de schema
- **Documentação de API:** Swagger / OpenAPI
- **Frontend:** React Native + Expo, TypeScript, TanStack Query
- **Módulo de IA:** motor de regras em Java (sem dependências externas)

---

## 🔐 Perfis de Usuário e Controle de Acesso (Spring Security)

A aplicação implementa **dois perfis de usuário** com permissões diferentes, controlados via `SecurityConfig`:

| Perfil | Permissões |
| :--- | :--- |
| **`ROLE_DONO_FAMILIA`** (Responsável Principal) | Além das permissões de um cuidador comum, pode remover membros da família (`DELETE /familia/membros/**`) e renomear a família (`PUT /familia`) |
| **Cuidador comum (usuário autenticado)** | Acesso às demais rotas da aplicação (pets, tarefas, trilhas, IA, recados), mas sem permissão para gerenciar a estrutura da família |

**Rotas públicas (sem autenticação):** login, cadastro de usuário (`POST /usuarios`), listagem de clínicas veterinárias (`GET /clinicas`), Swagger e console H2.

**Rotas protegidas:** todas as demais exigem token JWT válido; as rotas de IA (`/ia/chat`, `/ia/insights/*`, `/ia/historico/*`) exigem usuário autenticado.

**Proteção por dono do recurso:** além do controle por perfil, ações sensíveis (editar/excluir o próprio cadastro, editar/excluir um pet, concluir/reabrir/excluir uma tarefa) validam que o solicitante é o usuário autenticado dono do recurso — nunca um ID informado livremente na requisição. Isso é resolvido a partir do token JWT (`Authentication`), não de parâmetros do cliente.

---

## ⚙️ Funcionalidades Completas Implementadas

Além de operações de CRUD simples, dois fluxos completos de negócio foram implementados ponta a ponta:

1. **Família:** criação de uma família com geração de código de convite único, entrada de novos cuidadores via código, e gestão de membros (com controle de acesso por perfil).
2. **Tarefas:** criação de tarefas vinculadas a um pet e conclusão pelo cuidador autenticado (identificado via token, sem depender de ID enviado pelo cliente), com pontuação de XP creditada automaticamente ao membro da família.

---

## 🚀 Como Executar Localmente

### Pré-requisitos:
* Java 21+
* Maven

```bash
# 1. Clonar o repositório
git clone https://github.com/Challenge-Pet-Guardian-3/Disruptive-Architectures-IoT-IoB-IA.git
cd Disruptive-Architectures-IoT-IoB-IA

# 2. Rodar a aplicação
./mvnw spring-boot:run
```

* **Documentação Interativa (Swagger):** `http://localhost:8080/swagger-ui.html`
* **Banco H2 (dev):** `jdbc:h2:file:./data/petguardian`

O banco é criado e populado automaticamente pelo Flyway na primeira execução (migrations em `src/main/resources/db/migration`).

---

## 🤖 Módulo de IA — Assistente Clyvo (Disruptive Architectures: IoT, IoB & Generative IA)

### Visão Geral

O módulo **Clyvo AI** é um assistente inteligente de orientação preventiva, integrado diretamente ao backend Java já existente. Ele resolve dúvidas frequentes do tutor sobre cuidados básicos (alimentação, vacinação, higiene, comportamento e riscos de intoxicação) que não justificam uma consulta veterinária imediata, mas exigem resposta rápida e confiável.

### Abordagem de IA Escolhida e Justificativa Técnica

**Abordagem:** Motor de Regras Inteligente (Rule-Based System) — uma das abordagens de IA previstas no enunciado da disciplina.

**Por que motor de regras em vez de LLM/RAG generativo:**

- **Confiabilidade em contexto de saúde animal:** orientações sobre alimentação, vacinação e intoxicação exigem respostas consistentes, sem risco de alucinação.
- **Previsibilidade e auditabilidade:** cada resposta é rastreável a uma regra específica no código.
- **Aderência ao stack já existente:** roda dentro do mesmo backend Spring Boot, sem exigir infraestrutura adicional (servidor Python, banco vetorial, chave de API de LLM).
- **Caminho de evolução natural:** a arquitetura atual (mensagem → regra → resposta → histórico persistido) já deixa a base pronta para, futuramente, substituir a lógica de `gerarResposta()` por uma chamada a um LLM, mantendo o restante do fluxo inalterado.

### Dados Utilizados pela IA

| Dado | Origem | Uso |
| :--- | :--- | :--- |
| Pergunta do tutor (texto livre) | Input do usuário via app | Normalizada e comparada às palavras-chave das regras |
| Perfil do Pet (`Pet`) | Banco de dados (tabela `pet`) | Personaliza recomendações com o nome do pet em `gerarInsights()` |
| Histórico de mensagens (`IaMensagem`) | Banco de dados (tabela `ia_mensagem`) | Registra pergunta, resposta e data/hora por usuário e por pet |

### Arquitetura do Módulo de IA

```text
📱 App Mobile (React Native)
        │
        ▼
[HTTP POST /ia/chat]
┌───────────────────────────────────────────┐
│         ☕ Backend Spring Boot (Java)       │
│                                             │
│  1. IaController recebe a pergunta         │
│  2. IaService normaliza o texto            │
│  3. Motor de regras identifica o tema      │
│     (ração, vacina, ansiedade, banho,      │
│      toxicidade, higiene bucal)            │
│  4. Resposta é gerada e retornada          │
│  5. Pergunta + resposta são persistidas    │
│     em IaMensagem                          │
└───────────────────────────────────────────┘
        │                         │
        ▼                         ▼
🗄️ Banco de Dados            📋 Histórico de conversas
(Pet, Usuario)                (ia_mensagem)
```

### Endpoints REST da IA

**`POST /ia/chat`** — envia a pergunta e recebe a orientação.
```json
{
  "usuarioId": 1,
  "petId": 1,
  "pergunta": "Meu cachorro pode comer chocolate?"
}
```
Resposta:
```json
{
  "resposta": "Chocolate, cebola, alho, uvas/passas, cafeína e adoçantes com xilitol são tóxicos para cães e gatos. Em caso de ingestão, procure atendimento veterinário imediatamente."
}
```

**`GET /ia/insights/{petId}`** — recomendações contextuais para o pet.

**`GET /ia/historico/{usuarioId}`** — histórico de perguntas e respostas do usuário.

---

## 📌 Resultados Parciais

- ✅ Backend Spring Boot completo com Flyway e Spring Security (2 perfis de usuário)
- ✅ Fluxos completos de Família e Tarefas implementados
- ✅ Motor de regras da IA funcional para 6 categorias de perguntas frequentes
- ✅ Persistência de histórico de conversas e geração de insights por pet
- ✅ Endpoints documentados e testáveis via Swagger
- ⏳ Diagrama arquitetural em alta resolução (em elaboração)
- ⏳ Vídeos de demonstração e pitch (em gravação)