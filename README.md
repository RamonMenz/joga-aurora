# Joga Aurora

## 1. Visão Geral do Projeto

Este repositório contém o **backend em Java** que apoia a operação do projeto Joga Aurora, oferecendo uma API para gestão de estudantes, turmas, presença e avaliações físicas.

Por meio deste sistema, o time do projeto Joga Aurora consegue organizar cadastros, acompanhar a participação dos atendidos nas atividades e monitorar a evolução física ao longo do tempo, gerando informações que ajudam na tomada de decisão pedagógica e na prestação de contas para parceiros e apoiadores.

### Problema que resolve

- Dificuldade em organizar e consolidar informações de alunos (dados pessoais, turmas, histórico).
- Falta de controle estruturado de **presença** e **avaliações físicas** (medidas corporais, testes de desempenho).
- Necessidade de **relatórios** e consultas consolidadas para análise de evolução e apoio à tomada de decisão.

### Principais funcionalidades

- **Gestão de usuários**: cadastro, atualização, remoção, ativação/inativação e controle de permissões administrativas para a equipe do projeto.
- **Autenticação e autorização** baseadas em sessão HTTP, com controle de acesso por roles (por exemplo, `ROLE_ADMIN`).
- **Gestão de turmas**: criação e manutenção de turmas e grupos de atendimento, refletindo a organização pedagógica das atividades do projeto social.
- **Gestão de estudantes**: cadastro de estudantes atendidos pelo projeto e associação com turmas.
- **Registros de medições corporais**: armazenamento de dados físicos dos participantes ao longo do tempo, para acompanhamento de evolução e indicadores de saúde.
- **Registros de testes físicos**: lançamento de resultados de testes de desempenho esportivo, apoio à avaliação pedagógica e esportiva.
- **Controle de presença**: registro e consulta de presenças dos participantes em atividades, permitindo acompanhar assiduidade e engajamento.
- **Consultas e relatórios**: endpoints para consultas específicas e geração de relatórios (incluindo exportação em planilhas via Apache POI) que podem ser usados em relatórios internos e externos do projeto social.
- **Coleção Postman** pronta para uso em `postman/joga_aurora.postman_collection.json`.

---

## 2. Arquitetura e Tecnologias Utilizadas

### 2.1 Arquitetura

A aplicação segue uma **arquitetura em camadas**, típica de aplicações Spring Boot:

- **Controller**: recebe as requisições HTTP, valida as entradas, orquestra chamadas aos serviços e devolve respostas (geralmente DTOs JSON).
- **Service**: concentra a **regra de negócio**, valida fluxos e integra múltiplos repositórios quando necessário.
- **Repository**: encapsula o acesso ao banco de dados usando **Spring Data JPA** e **Hibernate**.
- **Entity**: classes que representam as tabelas do banco (modelo de persistência).
- **DTOs e mapeadores**: objetos de entrada/saída da API, separados das entidades de persistência.
- **Configurações**: beans de infraestrutura, configuração de segurança, interceptors e logging.

Fluxo típico de uma requisição:

1. Cliente HTTP chama um endpoint REST (por exemplo, `/client/me`).
2. O `*Controller` correspondente recebe a requisição, faz a validação básica (`@Valid`, tipos, parâmetros) e chama o service.
3. O `*Service` aplica as regras de negócio e chama os `repository` necessários.
4. O `repository` usa JPA para interagir com o PostgreSQL.
5. O resultado é mapeado para DTOs de resposta e retornado em JSON.

A autenticação utiliza **Spring Security** com **HTTP Basic** e **sessão HTTP**, armazenando o contexto de segurança em sessão com cookies configurados para uso com front-ends (SameSite, Secure, HttpOnly).

### 2.2 Tecnologias principais

**Linguagem e runtime**
- **Java 21** – linguagem principal do projeto.

**Frameworks e bibliotecas**
- **Spring Boot 3.5.x** – framework principal para criação da aplicação.
- **Spring Web** (`spring-boot-starter-web`) – exposição dos endpoints REST.
- **Spring Data JPA** (`spring-boot-starter-data-jpa`) – acesso ao banco de dados via JPA/Hibernate.
- **Spring Security** (`spring-boot-starter-security`) – autenticação, autorização e configuração de sessão.
- **Spring Validation** (`spring-boot-starter-validation`) – validação de dados de entrada com Bean Validation (Jakarta).
- **Spring Boot Actuator** – endpoints de monitoramento (ex.: `/actuator/health`).
- **Flyway** (`flyway-core`, `flyway-database-postgresql`) – controle de versão do schema e dados do banco de dados.
- **PostgreSQL JDBC Driver** – driver para conexão com o banco PostgreSQL.
- **Lombok** – redução de boilerplate (getters/setters, construtores, builders) nas classes Java.
- **Apache POI (poi-ooxml)** – geração de relatórios/exportações em Excel.
- **Apache Commons Lang3** – utilitários de linguagem.
- **Logback** – logging configurado via `logback-spring.xml`.

**Build e gerenciamento de dependências**
- **Maven** – build tool e gerenciador de dependências (`pom.xml`).
- **Maven Wrapper** (`mvnw`, `mvnw.cmd`) – permite rodar Maven sem instalação prévia.

### 2.3 Banco de dados e migrações

- **Banco de dados**: **PostgreSQL**.
- **Migrações**: gerenciadas via **Flyway**, com scripts em:
  - `src/main/resources/db/migration/structure` – criação de tabelas (ex.: `V1.0.0__create_table_client.sql`, `V1.6.0__create_table_attendance.sql`).
  - `src/main/resources/db/migration/data` – inserção de dados iniciais (ex.: clientes e permissões padrões).
- Ao iniciar a aplicação, o Flyway executa as migrações automaticamente, garantindo que o schema esteja atualizado.

### 2.4 Segurança (Spring Security)

A configuração principal de segurança está em `br.feevale.security.config.SecurityConfig`.

Principais pontos:

- **Autenticação**:
  - Utiliza **HTTP Basic** com armazenamento do contexto de segurança em **sessão HTTP**.
  - Senhas armazenadas com **BCrypt** (`BCryptPasswordEncoder`).
- **Autorização**:
  - Endpoints públicos, por padrão:
    - `POST /client` – cadastro inicial de cliente.
    - `GET /actuator/health` – verificação de saúde da aplicação.
  - Demais endpoints exigem usuário autenticado.
  - Uso de **roles** (por exemplo, `ROLE_ADMIN`) com anotação `@Secured` em métodos sensíveis.
- **Sessão e cookies**:
  - Política de sessão `SessionCreationPolicy.ALWAYS`.
  - Cookies configurados em `application.properties` com:
    - `same-site=None`
    - `secure=true`
    - `http-only=true`
  - Pensado para integração com front-ends SPA em domínios diferentes.

### 2.5 Configurações de ambiente

As configurações principais estão em `src/main/resources/application.properties`.

- **Aplicação**:
  - `spring.application.name=joga-aurora`
  - Porta configurável via variável `PORT` (padrão `8080`).
- **Banco de dados** (PostgreSQL):
  - `spring.datasource.url=${DATASOURCE_URL}` – ex.: `jdbc:postgresql://localhost:5432/jogaauroradb`.
  - `spring.datasource.username=${DATASOURCE_USERNAME}` – ex.: `jogaaurora`.
  - `spring.datasource.password=${DATASOURCE_PASSWORD}` – ex.: `jogaaurora`.
- **JPA/Hibernate**:
  - `spring.jpa.open-in-view=true`.
  - `spring.jpa.show-sql=${SPRING_JPA_SHOW_SQL:false}`.
  - `spring.jpa.hibernate.ddl-auto=none` (controlado por Flyway).
- **Time zone**:
  - `APP_TIMEZONE` usado para `hibernate.jdbc.time_zone` e `spring.jackson.time-zone` (padrão: `America/Sao_Paulo`).
- **Flyway**:
  - `spring.flyway.enabled=true`.
  - `spring.flyway.validate-on-migrate=true`.
  - `spring.flyway.locations=classpath:db/migration/structure,classpath:db/migration/data`.
- **CORS**:
  - `app.cors.allowed-origins=${CORS_ALLOWED_ORIGINS:http://localhost:3000,http://127.0.0.1:3000}`.

---

## 3. Estrutura da Codebase

Abaixo, um resumo das pastas e sua finalidade.

### 3.1 Visão geral

- `src/main/java/br/feevale/App.java`
  - Classe principal da aplicação, anotada com `@SpringBootApplication`.
  - Também habilita suporte a paginação do Spring Data para serialização de páginas.

- `src/main/java/br/feevale/joga_aurora`
  - **Camada de domínio principal do sistema** (alunos, turmas, medições, testes físicos, presença e relatórios).

- `src/main/java/br/feevale/security`
  - **Módulo de segurança e gerenciamento de clientes** (autenticação, autorização, cadastro de usuários, permissões).

- `src/main/resources`
  - `application.properties`: configurações da aplicação, banco, segurança e CORS.
  - `logback-spring.xml`: configuração de logging.
  - `db/migration/structure`: scripts SQL de criação de tabelas.
  - `db/migration/data`: scripts SQL de inserção de dados iniciais.

- `src/test/java/br/feevale/joga_aurora`
  - `AppTests.java`: teste básico que verifica o carregamento do contexto Spring.

- `postman/joga_aurora.postman_collection.json`
  - Coleção de requisições para testar a API.

### 3.2 Pacote `br.feevale.joga_aurora`

Organização típica de uma aplicação Spring em camadas.

- `controller`
  - Contém os **endpoints REST** relacionados ao domínio principal:
    - `StudentController`: operações sobre alunos.
    - `ClassroomController`: operações sobre turmas/salas de aula.
    - `BodyMeasurementController`: operações sobre medições corporais.
    - `PhysicalTestController`: operações sobre testes físicos.
    - `AttendanceController`: operações sobre presenças.
    - `QueryController`: consultas específicas e composições de dados de domínio.
    - `ReportController`: geração de relatórios, possivelmente com exportação para Excel.

- `entity`
  - Entidades JPA mapeando as tabelas criadas pelos scripts Flyway: clientes, permissões, turmas, alunos, medições, testes físicos, presenças etc.

- `repository`
  - Interfaces `JpaRepository` para acesso às entidades.

- `service`
  - Serviços com regras de negócio e orquestração de repositórios.

- `enums`
  - Enumerações de domínio (ex.: tipos de teste físico, status, etc.).

- `model` / `mapper`
  - Classes de **request/response** (DTOs) e mapeadores entre entidades e DTOs.

- `filter`
  - Objetos de filtro para consultas mais complexas.

- `util`
  - Funções utilitárias (datas, conversões, formatações, helpers gerais).

- `config`
  - Configurações diversas (interceptadores, logging adicional, etc., quando aplicável).

### 3.3 Pacote `br.feevale.security`

- `config`
  - `SecurityConfig`: configuração da cadeia de filtros de segurança, regras de autorização, autenticação HTTP Basic, sessão e cookies.

- `controller`
  - `LoginController`: endpoint de login (`POST /login`), que retorna os dados do cliente autenticado.
  - `ClientController`: endpoints de cadastro e gerenciamento de clientes.
    - `POST /client`: criação de novo cliente (público).
    - `GET /client/me`: dados do cliente autenticado.
    - `PUT /client/me`: atualização do próprio cadastro.
    - `DELETE /client/me`: remoção da própria conta.
    - Endpoints administrativos anotados com `@Secured("ROLE_ADMIN")`, como:
      - Alterar status ativo/inativo.
      - Conceder/remover permissão de administrador.
      - Listagem paginada de clientes com filtros.

- `domain`
  - Entidades relacionadas a clientes/usuários e permissões/roles.

- `repository`
  - Repositórios JPA para clientes e permissões.

- `service`
  - Serviços de segurança, por exemplo: criação de cliente, atualização, remoção, busca, ativação e alteração de permissões.

- `mapper`, `model` (se presentes)
  - DTOs específicos de segurança (por exemplo, `ClientRequest`, `UpdateClientRequest`, `ClientNameRequest`, `ClientResponse`) e seus mapeamentos.

### 3.4 Migrações (Flyway)

- `src/main/resources/db/migration/structure`
  - `V1.0.0__create_table_client.sql`: cria a tabela de clientes.
  - `V1.1.0__create_table_permission.sql`: cria a tabela de permissões.
  - `V1.2.0__create_table_classroom.sql`: cria a tabela de turmas/salas de aula.
  - `V1.3.0__create_table_student.sql`: cria a tabela de estudantes.
  - `V1.4.0__create_table_body-measurement.sql`: cria a tabela de medições corporais.
  - `V1.5.0__create_table_physical-test.sql`: cria a tabela de testes físicos.
  - `V1.6.0__create_table_attendance.sql`: cria a tabela de presenças.

- `src/main/resources/db/migration/data`
  - `V1.0.1__insert_into_client.sql`: popula a tabela de clientes com registros iniciais.
  - `V1.1.1__insert_into_permission.sql`: popula a tabela de permissões (incluindo role de administrador).

---

## 4. Como Executar o Projeto

Abaixo, um guia completo para rodar o backend, tanto localmente quanto com Docker.

### 4.1 Pré-requisitos

Antes de começar, garanta que você possui:

- **Java 21** instalado e configurado no `PATH`.
- **PostgreSQL** acessível (por padrão, espera-se na porta `5432`).
- **Docker** e **Docker Compose** (opcional, mas recomendado para execução conteinerizada).

> O projeto já inclui o **Maven Wrapper** (`mvnw` / `mvnw.cmd`), então não é obrigatório ter Maven instalado globalmente.

### 4.2 Configuração do banco de dados (sem Docker)

1. Crie um banco de dados no PostgreSQL, por exemplo:
   - Nome: `jogaauroradb`
   - Usuário: `jogaaurora`
   - Senha: `jogaaurora`

2. Garanta que o banco está acessível, por padrão, em:
   - `jdbc:postgresql://localhost:5432/jogaauroradb`

3. Opcionalmente, ajuste as variáveis de ambiente para usar outros valores:

   - `DATASOURCE_URL`
   - `DATASOURCE_USERNAME`
   - `DATASOURCE_PASSWORD`


### 4.3 Variáveis de ambiente importantes

Antes de rodar a aplicação, configure (se necessário):

- `DATASOURCE_URL` – URL JDBC do banco, ex.:  
  `jdbc:postgresql://localhost:5432/jogaauroradb`
- `DATASOURCE_USERNAME` – usuário do banco, ex.: `jogaaurora`
- `DATASOURCE_PASSWORD` – senha do banco, ex.: `jogaaurora`
- `PORT` – porta HTTP em que a aplicação irá rodar (padrão `8080`).
- `APP_TIMEZONE` – fuso horário da aplicação, ex.: `America/Sao_Paulo`.
- `CORS_ALLOWED_ORIGINS` – origens permitidas para CORS, ex.: `http://localhost:3000`.
- `SPRING_JPA_SHOW_SQL` – `true`/`false`, para exibir as queries SQL geradas (útil em desenvolvimento).

### 4.4 Rodando a aplicação localmente (Maven)

No diretório raiz do projeto (`joga-aurora`), execute no PowerShell:

```powershell
# Limpa, baixa dependências e compila o projeto
./mvnw clean package

# Sobe a aplicação Spring Boot
./mvnw spring-boot:run
```

A aplicação ficará disponível em:

- `http://localhost:8080` (ou na porta configurada via `PORT`).

Na primeira execução, o **Flyway** aplicará automaticamente as migrações de banco (estrutura + dados iniciais).

### 4.5 Executando com Docker (somente aplicação)

O projeto contém um `Dockerfile` multi-stage que:

1. Usa uma imagem Maven com Java 21 para compilar o projeto.
2. Gera o JAR final e o copia para uma imagem `eclipse-temurin:21-jre`.

Passos para build e run:

```powershell
# Build da imagem (no diretório raiz do projeto)
docker build -t joga-aurora .

# Executa o container apontando para um banco PostgreSQL acessível
docker run `
  -p 8080:8080 `
  -e DATASOURCE_URL="jdbc:postgresql://host.docker.internal:5432/jogaauroradb" `
  -e DATASOURCE_USERNAME="jogaaurora" `
  -e DATASOURCE_PASSWORD="jogaaurora" `
  joga-aurora
```

> Observação: o container da aplicação **não sobe o banco de dados**; espera-se que o PostgreSQL esteja rodando externamente (por exemplo, na máquina host).

### 4.6 Executando com Docker Compose

O repositório inclui um `docker-compose.yml` que facilita a execução da aplicação (ainda esperando um PostgreSQL externo).

Passos:

1. Certifique-se de que o PostgreSQL está rodando em `localhost:5432` com o banco `jogaauroradb` e credenciais compatíveis.

2. No diretório raiz do projeto, construa a imagem da aplicação (caso ainda não exista):

   ```powershell
   docker build -t joga-aurora .
   ```

3. Suba o serviço via Docker Compose:

   ```powershell
   docker compose up
   ```

   ou, dependendo da sua instalação:

   ```powershell
   docker-compose up
   ```

4. A aplicação ficará acessível em:

   - `http://localhost:8080`

O `docker-compose.yml` normalmente define variáveis de ambiente como:

- `DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/jogaauroradb`
- `DATASOURCE_USERNAME=jogaaurora`
- `DATASOURCE_PASSWORD=jogaaurora`
- `APP_TIMEZONE=America/Sao_Paulo`
- `CORS_ALLOWED_ORIGINS=http://localhost:5173` (exemplo para front-end em Vite)

### 4.7 Acessando endpoints principais e autenticação

#### 4.7.1 Health check

- `GET /actuator/health` – endpoint público para verificar se a aplicação está no ar.

#### 4.7.2 Cadastro de cliente (público)

- `POST /client` – cadastro de um novo cliente.
  - Corpo esperado: dados básicos do cliente (nome, e-mail, senha, etc.).

Alguns clientes e permissões iniciais podem ser criados automaticamente pelos scripts SQL em `db/migration/data` (útil para testes e primeiro acesso).

#### 4.7.3 Login e sessão

- Autenticação via **HTTP Basic**.
- `POST /login` – retorna os dados do cliente autenticado (`ClientResponse`) e estabelece a sessão via cookie.

Fluxo típico:

1. O cliente (frontend ou ferramenta como Postman) envia as credenciais via **Basic Auth**.
2. Se autenticado com sucesso, o servidor cria uma sessão, associando o usuário autenticado.
3. As próximas requisições usam o cookie de sessão para manter o contexto de autenticação.

#### 4.7.4 Endpoints protegidos

- Exigem usuário autenticado e, em alguns casos, `ROLE_ADMIN`.
- Exemplos:
  - `GET /client/me` – obtém os dados do cliente autenticado.
  - `PUT /client/me` – atualiza os dados do cliente autenticado.
  - `DELETE /client/me` – remove o próprio cliente.
  - Endpoints administrativos de cliente, presença, alunos, turmas, etc.

### 4.8 Uso da coleção Postman

O arquivo `postman/joga_aurora.postman_collection.json` inclui exemplos práticos de:

- Requisições de cadastro e login de clientes.
- Chamadas autenticadas para endpoints protegidos.
- Consultas de alunos, turmas, presenças, medições e testes físicos.

Você pode importar esse arquivo diretamente no Postman para começar a interagir com a API rapidamente.
