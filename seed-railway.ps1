# ==============================================================================
# PetGuardian API — Script de Seed e Validação E2E no Railway
# ==============================================================================
# Executa autenticação JWT, cadastro de usuários com ViaCEP, pets, care circle,
# tarefas futuras, histórico de saúde, trilhas, módulos e aulas.
# Idempotente: pode ser executado repetidas vezes sem falhas.
# ==============================================================================

param(
    [string]$BaseUrl = "https://java-advanced-production-35ab.up.railway.app"
)

$ErrorActionPreference = "Stop"
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

function Log-Step {
    param([string]$Title)
    Write-Host "`n========================================================" -ForegroundColor Cyan
    Write-Host ">> $Title" -ForegroundColor Cyan
    Write-Host "========================================================" -ForegroundColor Cyan
}

function Log-Success {
    param([string]$Message)
    Write-Host "  [OK] $Message" -ForegroundColor Green
}

function Log-Info {
    param([string]$Message)
    Write-Host "  [INFO] $Message" -ForegroundColor Yellow
}

function Invoke-Api {
    param(
        [string]$Uri,
        [string]$Method = "Get",
        [hashtable]$Headers = @{},
        [string]$Body = $null
    )
    try {
        $params = @{
            Uri         = $Uri
            Method      = $Method
            ContentType = "application/json; charset=utf-8"
        }
        if ($Headers.Count -gt 0) { $params.Headers = $Headers }
        if ($Body) { $params.Body = $Body }
        
        return Invoke-RestMethod @params
    } catch {
        $responseBody = ""
        if ($_.Exception.Response) {
            $stream = $_.Exception.Response.GetResponseStream()
            if ($stream) {
                $reader = New-Object System.IO.StreamReader($stream)
                $responseBody = $reader.ReadToEnd()
            }
        }
        $msg = if ($responseBody) { $responseBody } else { $_.Exception.Message }
        throw [System.Exception]::new("API Error [$Method $Uri]: $msg")
    }
}

# ------------------------------------------------------------------------------
# 1. Healthcheck da API
# ------------------------------------------------------------------------------
Log-Step "1. Verificando Saúde da API no Railway ($BaseUrl)"
$health = Invoke-Api -Uri "$BaseUrl/actuator/health"
Log-Success "API Online! Status: $($health.status)"

# ------------------------------------------------------------------------------
# 2. Cadastro dos 2 Usuários (com CEPs reais validados via ViaCEP)
# ------------------------------------------------------------------------------
Log-Step "2. Criando Usuários com Endereços ViaCEP"

$user1Body = @{
    nome           = "Enzo Administrador"
    email          = "enzo.admin@petguardian.com"
    senha          = "Admin@123456"
    ddd            = "11"
    numeroTelefone = "987654321"
    role           = "ADMIN"
    endereco       = @{
        cep    = "07700-100"
        numero = "100"
    }
} | ConvertTo-Json -Depth 5

$user2Body = @{
    nome           = "Carolina Cuidadora"
    email          = "carolina.cuidadora@petguardian.com"
    senha          = "User@123456"
    ddd            = "11"
    numeroTelefone = "912345678"
    role           = "PREMIUM"
    endereco       = @{
        cep    = "01310-000"
        numero = "200"
    }
} | ConvertTo-Json -Depth 5

try {
    $createdUser1 = Invoke-Api -Uri "$BaseUrl/usuarios" -Method Post -Body $user1Body
    Log-Success "Usuario 1 criado: $($createdUser1.nome) (ID: $($createdUser1.id))"
} catch {
    Log-Info "Usuario 1 ja cadastrado. Prosseguindo."
}

try {
    $createdUser2 = Invoke-Api -Uri "$BaseUrl/usuarios" -Method Post -Body $user2Body
    Log-Success "Usuario 2 criado: $($createdUser2.nome) (ID: $($createdUser2.id))"
} catch {
    Log-Info "Usuario 2 ja cadastrado. Prosseguindo."
}

# ------------------------------------------------------------------------------
# 3. Autenticação JWT (/login)
# ------------------------------------------------------------------------------
Log-Step "3. Autenticando Usuários e Gerando Token JWT"

$login1Body = @{
    email = "enzo.admin@petguardian.com"
    senha = "Admin@123456"
} | ConvertTo-Json

$login1Response = Invoke-Api -Uri "$BaseUrl/login" -Method Post -Body $login1Body
$tokenAdmin = $login1Response.token
$user1Id = $login1Response.user.id
Log-Success "Usuario 1 logado! ID: $user1Id | Perfil: $($login1Response.user.role)"

$headersAdmin = @{
    "Authorization" = "Bearer $tokenAdmin"
    "Content-Type"  = "application/json"
}

$login2Body = @{
    email = "carolina.cuidadora@petguardian.com"
    senha = "User@123456"
} | ConvertTo-Json

$login2Response = Invoke-Api -Uri "$BaseUrl/login" -Method Post -Body $login2Body
$tokenUser2 = $login2Response.token
$user2Id = $login2Response.user.id
Log-Success "Usuario 2 logado! ID: $user2Id | Perfil: $($login2Response.user.role)"

$headersUser2 = @{
    "Authorization" = "Bearer $tokenUser2"
    "Content-Type"  = "application/json"
}

# ------------------------------------------------------------------------------
# 4. Cadastro / Obtenção de 3 Pets
# ------------------------------------------------------------------------------
Log-Step "4. Cadastrando e Mapeando 3 Pets"

$existingPetsPage = Invoke-Api -Uri "$BaseUrl/pets?size=50" -Headers $headersAdmin
$existingPets = $existingPetsPage.content

function Get-OrCreate-Pet {
    param(
        [string]$Nome,
        [string]$DataNasc,
        [string]$Raca,
        [string]$Porte,
        [string]$Sexo,
        [bool]$Castrado,
        [long]$TutorId,
        [hashtable]$Headers
    )
    $found = $existingPets | Where-Object { $_.nome -eq $Nome }
    if ($found) {
        Log-Info "Pet '$Nome' ja existe no banco (ID: $($found.id)). Reutilizando."
        return $found
    }

    $petBody = @{
        nome      = $Nome
        dataNasc  = $DataNasc
        raca      = $Raca
        porte     = $Porte
        sexo      = $Sexo
        castrado  = $Castrado
        usuarioId = $TutorId
    } | ConvertTo-Json

    $novoPet = Invoke-Api -Uri "$BaseUrl/pets" -Method Post -Headers $Headers -Body $petBody
    Log-Success "Pet criado: $($novoPet.nome) (ID: $($novoPet.id), Raca: $($novoPet.raca.nome), Tutor ID: $TutorId)"
    return $novoPet
}

$pet1 = Get-OrCreate-Pet -Nome "Thor" -DataNasc "2021-03-10" -Raca "Golden Retriever" -Porte "GRANDE" -Sexo "M" -Castrado $true -TutorId $user1Id -Headers $headersAdmin
$pet2 = Get-OrCreate-Pet -Nome "Luna" -DataNasc "2022-07-22" -Raca "Poodle" -Porte "PEQUENO" -Sexo "F" -Castrado $true -TutorId $user1Id -Headers $headersAdmin
$pet3 = Get-OrCreate-Pet -Nome "Pipoca" -DataNasc "2023-01-15" -Raca "Vira-lata" -Porte "MEDIO" -Sexo "F" -Castrado $false -TutorId $user2Id -Headers $headersUser2

# ------------------------------------------------------------------------------
# 5. Relacionar Pets com Usuários (Care Circle / Co-cuidadores)
# ------------------------------------------------------------------------------
Log-Step "5. Relacionando Pets com Co-cuidadores (Care Circle)"

function Add-Caregiver-If-Not-Exists {
    param(
        [long]$PetId,
        [long]$ResponsavelId,
        [string]$EmailConvidado,
        [hashtable]$Headers
    )
    try {
        $cuidadores = Invoke-Api -Uri "$BaseUrl/pets/$PetId/cuidadores" -Headers $Headers
        $jaExiste = $cuidadores | Where-Object { $_.email -eq $EmailConvidado }
        if ($jaExiste) {
            Log-Info "Cuidador $EmailConvidado ja vinculado ao Pet ID $PetId."
            return
        }
        $body = @{
            responsavelPrincipalId = $ResponsavelId
            email                  = $EmailConvidado
        } | ConvertTo-Json
        $res = Invoke-Api -Uri "$BaseUrl/pets/$PetId/cuidadores" -Method Post -Headers $Headers -Body $body
        Log-Success "$EmailConvidado vinculado como co-cuidador ao Pet ID $PetId!"
    } catch {
        Log-Info "Vinculo cuidador Pet ${PetId}: $($_.Exception.Message)"
    }
}

# Enzo convida Carolina para Thor e Luna
Add-Caregiver-If-Not-Exists -PetId $pet1.id -ResponsavelId $user1Id -EmailConvidado "carolina.cuidadora@petguardian.com" -Headers $headersAdmin
Add-Caregiver-If-Not-Exists -PetId $pet2.id -ResponsavelId $user1Id -EmailConvidado "carolina.cuidadora@petguardian.com" -Headers $headersAdmin

# Carolina convida Enzo para Pipoca
Add-Caregiver-If-Not-Exists -PetId $pet3.id -ResponsavelId $user2Id -EmailConvidado "enzo.admin@petguardian.com" -Headers $headersUser2

# ------------------------------------------------------------------------------
# 6. Criar Tarefas Futuras (Datas posteriores a 10/09/2026)
# ------------------------------------------------------------------------------
Log-Step "6. Criando Tarefas com Prazos Futuros (> 10/09/2026)"

# Nota: tarefa.titulo tem tamanho maximo de 30 caracteres
$tarefa1Body = @{
    titulo       = "Vacinacao Antirrabica Anual"
    pontosTarefa = 50
    descricao    = "Levar o Thor na clinica veterinaria para aplicacao da vacina antirrabica e polivalente."
    prazo        = "2026-09-18T10:00:00"
    usuarioId    = $user1Id
    petId        = $pet1.id
    status       = "PENDENTE"
} | ConvertTo-Json

$tarefa2Body = @{
    titulo       = "Dar Vermifugo Semestral"
    pontosTarefa = 30
    descricao    = "Dar 1 comprimido mastigavel de vermifugo junto ao alimento matinal."
    prazo        = "2026-09-22T08:30:00"
    usuarioId    = $user2Id
    petId        = $pet2.id
    status       = "PENDENTE"
} | ConvertTo-Json

$tarefa3Body = @{
    titulo       = "Banho e Tosa Higienica"
    pontosTarefa = 40
    descricao    = "Banho completo com xampu medicamentoso prescrito pelo veterinario."
    prazo        = "2026-09-26T14:00:00"
    usuarioId    = $user2Id
    petId        = $pet3.id
    status       = "PENDENTE"
} | ConvertTo-Json

$tarefa1 = Invoke-Api -Uri "$BaseUrl/tarefas" -Method Post -Headers $headersAdmin -Body $tarefa1Body
Log-Success "Tarefa 1 criada: $($tarefa1.titulo) (Prazo: $($tarefa1.prazo), Pontos: $($tarefa1.pontosTarefa))"

$tarefa2 = Invoke-Api -Uri "$BaseUrl/tarefas" -Method Post -Headers $headersAdmin -Body $tarefa2Body
Log-Success "Tarefa 2 criada: $($tarefa2.titulo) (Prazo: $($tarefa2.prazo), Pontos: $($tarefa2.pontosTarefa))"

$tarefa3 = Invoke-Api -Uri "$BaseUrl/tarefas" -Method Post -Headers $headersUser2 -Body $tarefa3Body
Log-Success "Tarefa 3 criada: $($tarefa3.titulo) (Prazo: $($tarefa3.prazo), Pontos: $($tarefa3.pontosTarefa))"

# Conclui a Tarefa 1 para testar PATCH /concluir e pontuação do Pet
$concluirBody = @{
    concluinteId = $user1Id
} | ConvertTo-Json

$tarefaConcluida = Invoke-Api -Uri "$BaseUrl/tarefas/$($tarefa1.id)/concluir" -Method Patch -Headers $headersAdmin -Body $concluirBody
Log-Success "Tarefa 1 concluida! Status atual: $($tarefaConcluida.status)"

# ------------------------------------------------------------------------------
# 7. Criar Histórico de Saúde do Pet (limite VARCHAR 30)
# ------------------------------------------------------------------------------
Log-Step "7. Registrando Históricos Clínicos dos Pets"

# Nota: historico.tipo_hist tem tamanho maximo de 30 caracteres
$hist1Body = @{
    tipoHist = "Check-up Cardiologico Anual"
    dataHist = "2026-09-08T10:30:00"
    petId    = $pet1.id
} | ConvertTo-Json

$hist1 = Invoke-Api -Uri "$BaseUrl/historicos" -Method Post -Headers $headersAdmin -Body $hist1Body
Log-Success "Historico Pet 1 registrado: $($hist1.tipoHist) (Data: $($hist1.dataHist))"

$hist2Body = @{
    tipoHist = "Antiparasitario Simparic"
    dataHist = "2026-09-05T16:00:00"
    petId    = $pet2.id
} | ConvertTo-Json

$hist2 = Invoke-Api -Uri "$BaseUrl/historicos" -Method Post -Headers $headersAdmin -Body $hist2Body
Log-Success "Historico Pet 2 registrado: $($hist2.tipoHist) (Data: $($hist2.dataHist))"

$hist3Body = @{
    tipoHist = "Consulta de Rotina Geral"
    dataHist = "2026-09-01T11:15:00"
    petId    = $pet3.id
} | ConvertTo-Json

$hist3 = Invoke-Api -Uri "$BaseUrl/historicos" -Method Post -Headers $headersUser2 -Body $hist3Body
Log-Success "Historico Pet 3 registrado: $($hist3.tipoHist) (Data: $($hist3.dataHist))"

# ------------------------------------------------------------------------------
# 8. Criar Trilhas, Módulos e Aulas Educativas (Role ADMIN)
# ------------------------------------------------------------------------------
Log-Step "8. Criando Trilhas, Módulos e Aulas"

# Trilha 1 para Thor
$trilha1Body = @{
    nome      = "Adestramento e Foco"
    descricao = "Trilha completa de comandos basicos e tecnicas de caminhada para caes de grande porte."
    petId     = $pet1.id
} | ConvertTo-Json

$trilha1 = Invoke-Api -Uri "$BaseUrl/trilhas" -Method Post -Headers $headersAdmin -Body $trilha1Body
Log-Success "Trilha 1 criada: $($trilha1.nome) (ID: $($trilha1.id), Pet: $($pet1.nome))"

# Modulo 1 na Trilha 1
$modulo1Body = @{
    nome           = "Comandos Essenciais de Foco"
    tempoConclusao = "1h"
    descricao      = "Ensinar o pet a prestar atencao e responder a comandos de obediencia basica."
    trilhaId       = $trilha1.id
} | ConvertTo-Json

$modulo1 = Invoke-Api -Uri "$BaseUrl/modulos" -Method Post -Headers $headersAdmin -Body $modulo1Body
Log-Success "Modulo 1 criado: $($modulo1.nome) (ID: $($modulo1.id), Tempo: $($modulo1.tempoConclusao))"

# Aula 1 no Modulo 1
$aula1Body = @{
    nome        = "Comando Sentar com Recompensa"
    descricao   = "Como usar reforco positivo para induzir o pet a sentar de forma natural."
    pontosAula  = 20
    dificuldade = "Facil"
    conteudo    = "Segure o petisco na ponta dos dedos e mova-o suavemente sobre a cabeca do pet. Quando ele sentar, entregue o petisco e faca carinho."
    concluida   = $false
    moduloId    = $modulo1.id
} | ConvertTo-Json

$aula1 = Invoke-Api -Uri "$BaseUrl/aulas" -Method Post -Headers $headersAdmin -Body $aula1Body
Log-Success "Aula 1 criada: $($aula1.nome) (ID: $($aula1.id), Pontos: $($aula1.pontosAula))"

# Aula 2 no Modulo 1
$aula2Body = @{
    nome        = "Caminhada Sem Puxar a Guia"
    descricao   = "Tecnica para manter o pet ao lado durante o passeio."
    pontosAula  = 25
    dificuldade = "Intermediario"
    conteudo    = "Quando a guia esticar, pare e espere o pet virar para voce. Recompense assim que ele retornar ao seu lado."
    concluida   = $false
    moduloId    = $modulo1.id
} | ConvertTo-Json

$aula2 = Invoke-Api -Uri "$BaseUrl/aulas" -Method Post -Headers $headersAdmin -Body $aula2Body
Log-Success "Aula 2 criada: $($aula2.nome) (ID: $($aula2.id), Pontos: $($aula2.pontosAula))"

# Conclui Aula 1 (PATCH /aulas/{id}/concluir)
$aulaConcluida = Invoke-Api -Uri "$BaseUrl/aulas/$($aula1.id)/concluir" -Method Patch -Headers $headersAdmin
Log-Success "Aula 1 concluida com sucesso! Concluida: $($aulaConcluida.concluida)"

# Trilha 2 para Luna
$trilha2Body = @{
    nome      = "Higiene e Escovacao"
    descricao = "Rotina de higiene bucal, pelagem e corte de unhas sem estresse."
    petId     = $pet2.id
} | ConvertTo-Json

$trilha2 = Invoke-Api -Uri "$BaseUrl/trilhas" -Method Post -Headers $headersAdmin -Body $trilha2Body
Log-Success "Trilha 2 criada: $($trilha2.nome) (ID: $($trilha2.id), Pet: $($pet2.nome))"

# Modulo 2 na Trilha 2
$modulo2Body = @{
    nome           = "Cuidados com Pelagem Fina"
    tempoConclusao = "40min"
    descricao      = "Tecnicas de desembaraco e banho a seco para pelos cacheados."
    trilhaId       = $trilha2.id
} | ConvertTo-Json

$modulo2 = Invoke-Api -Uri "$BaseUrl/modulos" -Method Post -Headers $headersAdmin -Body $modulo2Body
Log-Success "Modulo 2 criado: $($modulo2.nome) (ID: $($modulo2.id), Tempo: $($modulo2.tempoConclusao))"

# Aula 3 no Modulo 2
$aula3Body = @{
    nome        = "Dessensibilizacao a Escova"
    descricao   = "Como acostumar o pet com o som e textura da rasqueadeira."
    pontosAula  = 15
    dificuldade = "Facil"
    conteudo    = "Apresente a rasqueadeira sem tocar. Recompense. Depois passe suavemente pelo dorso."
    concluida   = $false
    moduloId    = $modulo2.id
} | ConvertTo-Json

$aula3 = Invoke-Api -Uri "$BaseUrl/aulas" -Method Post -Headers $headersAdmin -Body $aula3Body
Log-Success "Aula 3 criada: $($aula3.nome) (ID: $($aula3.id), Pontos: $($aula3.pontosAula))"

# ------------------------------------------------------------------------------
# 9. Verificação e Auditoria de Todos os Endpoints (GETs)
# ------------------------------------------------------------------------------
Log-Step "9. Executando Auditoria Geral dos Endpoints (GETs)"

# 9.1 Usuários
$usuarios = Invoke-Api -Uri "$BaseUrl/usuarios" -Headers $headersAdmin
$usuariosCount = if ($usuarios.content) { $usuarios.content.Count } else { $usuarios.Count }
Log-Success "GET /usuarios -> $usuariosCount usuario(s) cadastrado(s)"

$user1Detail = Invoke-Api -Uri "$BaseUrl/usuarios/$user1Id" -Headers $headersAdmin
$end1 = $user1Detail.enderecos[0]
Log-Success "GET /usuarios/$user1Id -> $($user1Detail.nome) ($($user1Detail.email)) | Endereco: $($end1.rua), Nº $($end1.numero) - $($end1.bairro), $($end1.cidade)/$($end1.estado) (CEP: $($end1.cep))"

$user2Detail = Invoke-Api -Uri "$BaseUrl/usuarios/$user2Id" -Headers $headersAdmin
$end2 = $user2Detail.enderecos[0]
Log-Success "GET /usuarios/$user2Id -> $($user2Detail.nome) ($($user2Detail.email)) | Endereco: $($end2.rua), Nº $($end2.numero) - $($end2.bairro), $($end2.cidade)/$($end2.estado) (CEP: $($end2.cep))"

$redeCuidadoUser1 = Invoke-Api -Uri "$BaseUrl/usuarios/$user1Id/rede-cuidado" -Headers $headersAdmin
Log-Success "GET /usuarios/$user1Id/rede-cuidado -> $($redeCuidadoUser1.pets.Count) pet(s) vinculado(s), $($redeCuidadoUser1.coCuidadores.Count) co-cuidador(es)"

# 9.2 Pets e Pontos
$petsList = Invoke-Api -Uri "$BaseUrl/pets" -Headers $headersAdmin
$petsCount = if ($petsList.content) { $petsList.content.Count } else { $petsList.Count }
Log-Success "GET /pets -> $petsCount pet(s) cadastrado(s)"

$pontosThor = Invoke-Api -Uri "$BaseUrl/pets/$($pet1.id)/pontos" -Headers $headersAdmin
Log-Success "GET /pets/$($pet1.id)/pontos -> Total: $($pontosThor.pontosTotais) pts (Tarefas: $($pontosThor.pontosTarefas), Aulas: $($pontosThor.pontosAulas))"

$cuidadoresThor = Invoke-Api -Uri "$BaseUrl/pets/$($pet1.id)/cuidadores" -Headers $headersAdmin
Log-Success "GET /pets/$($pet1.id)/cuidadores -> $($cuidadoresThor.Count) cuidador(es) no Care Circle"

$historicoConsolidadoThor = Invoke-Api -Uri "$BaseUrl/pets/$($pet1.id)/historico" -Headers $headersAdmin
Log-Success "GET /pets/$($pet1.id)/historico -> $($historicoConsolidadoThor.tarefasConcluidas.Count) tarefa(s) concluida(s)"

# 9.3 Históricos Clínicos
$historicosThor = Invoke-Api -Uri "$BaseUrl/historicos/pet/$($pet1.id)" -Headers $headersAdmin
Log-Success "GET /historicos/pet/$($pet1.id) -> $($historicosThor.Count) registro(s) de saude"

# 9.4 Tarefas
$tarefasThor = Invoke-Api -Uri "$BaseUrl/tarefas/by-pet/$($pet1.id)" -Headers $headersAdmin
$tarefasThorCount = if ($tarefasThor.content) { $tarefasThor.content.Count } else { $tarefasThor.Count }
Log-Success "GET /tarefas/by-pet/$($pet1.id) -> $tarefasThorCount tarefa(s) encontrada(s)"

$pontosUser1 = Invoke-Api -Uri "$BaseUrl/tarefas/by-usuario/pontos?usuarioId=$user1Id" -Headers $headersAdmin
Log-Success "GET /tarefas/by-usuario/pontos?usuarioId=$user1Id -> $pontosUser1 pontos acumulados pelo cuidador"

# 9.5 Trilhas, Módulos e Aulas
$trilhasThor = Invoke-Api -Uri "$BaseUrl/trilhas/pet/$($pet1.id)" -Headers $headersAdmin
Log-Success "GET /trilhas/pet/$($pet1.id) -> $($trilhasThor.Count) trilha(s)"

$modulosTrilha1 = Invoke-Api -Uri "$BaseUrl/modulos/trilha/$($trilha1.id)" -Headers $headersAdmin
Log-Success "GET /modulos/trilha/$($trilha1.id) -> $($modulosTrilha1.Count) modulo(s)"

$aulasModulo1 = Invoke-Api -Uri "$BaseUrl/aulas/modulo/$($modulo1.id)" -Headers $headersAdmin
Log-Success "GET /aulas/modulo/$($modulo1.id) -> $($aulasModulo1.Count) aula(s)"

Write-Host "`n========================================================" -ForegroundColor Green
Write-Host ">> SEED E TESTES E2E CONCLUÍDOS COM SUCESSO TOTAL! <<" -ForegroundColor Green
Write-Host "========================================================`n" -ForegroundColor Green
