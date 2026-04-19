# MavenProjectJenkins — API de Livros

API REST de catálogo de livros construída com Java + Spring Boot, integrada ao Jenkins para build automatizado a partir do SCM. O Jenkins puxa o código, compila com Maven, executa os testes e gera o artefato JAR ao final do pipeline.

## Tecnologias

- **Java 17**
- **Spring Boot 3.2**
- **Maven 3.9**
- **Jenkins** (pipeline declarativo)
- **JUnit 5** + **Mockito**
- **JaCoCo** (cobertura de testes)

## Endpoints da API

| Método | Endpoint            | Descrição               |
|--------|---------------------|-------------------------|
| GET    | `/api/livros`       | Lista todos os livros   |
| GET    | `/api/livros/{id}`  | Busca livro por ID      |
| POST   | `/api/livros`       | Cadastra novo livro     |
| PUT    | `/api/livros/{id}`  | Atualiza livro          |
| DELETE | `/api/livros/{id}`  | Remove livro            |
| GET    | `/actuator/health`  | Health check            |

### Exemplo de payload

```json
{
  "titulo": "Clean Code",
  "autor": "Robert C. Martin",
  "anoPublicacao": 2008,
  "isbn": "978-0132350884"
}
```

## Como executar localmente

```bash
# Compilar
mvn clean compile

# Executar testes
mvn test

# Iniciar a aplicação
mvn spring-boot:run

# Gerar JAR
mvn clean package
java -jar target/app-livros.jar
```

A API sobe em `http://localhost:8080`.

## Pipeline Jenkins

### Pré-requisitos no Jenkins

1. Plugin **Maven Integration**
2. Plugin **JaCoCo**
3. Configurar em *Manage Jenkins → Tools*:
   - JDK 17 com nome `JDK 17`
   - Maven 3.9 com nome `Maven 3.9`

### Stages do pipeline

```
Checkout → Compilar → Testes → Empacotar → Arquivar Artefato
```

| Stage | Comando Maven | Descrição |
|-------|--------------|-----------|
| Checkout | — | Puxa o código do SCM |
| Compilar | `mvn clean compile` | Compila o projeto |
| Testes | `mvn test` | Executa testes + relatório JaCoCo |
| Empacotar | `mvn package` | Gera o JAR em `target/` |
| Arquivar Artefato | — | Salva o JAR no Jenkins com fingerprint |

### Criar job no Jenkins

1. **Novo item** → **Pipeline**
2. Em *Pipeline Definition* selecione **Pipeline script from SCM**
3. SCM: **Git**, URL do repositório
4. Script Path: `Jenkinsfile`
5. Salvar e executar

## Estrutura do projeto

```
MavenProjectJenkins/
├── src/
│   ├── main/
│   │   ├── java/com/leandroninja/livros/
│   │   │   ├── LivrosApplication.java
│   │   │   ├── controller/
│   │   │   │   └── LivroController.java
│   │   │   ├── model/
│   │   │   │   └── Livro.java
│   │   │   └── service/
│   │   │       └── LivroService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/leandroninja/livros/
│           └── controller/
│               └── LivroControllerTest.java
├── Jenkinsfile
├── pom.xml
└── README.md
```
