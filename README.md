# 🤖 AI Code Reviewer

An **AI-powered code review platform** built with **Java, Spring Boot, Ollama, and Qwen2.5-Coder**.

The project starts as a lightweight local LLM-based code reviewer and is designed to evolve into a **production-grade, multi-agent code review platform** integrated with GitLab CI/CD, SonarQube, security scanners, automated testing, repository context, scoring, and merge gates.

---

## 🚀 Project Vision

The long-term goal is to build an intelligent code review system that can automatically analyze code changes and provide actionable engineering feedback.

### Planned review capabilities

- 🐛 Bug detection
- 🔐 Security vulnerability detection
- ⚡ Performance analysis
- 🧹 Code quality review
- 🧪 Test coverage and missing test-case detection
- 🎯 Edge-case analysis
- 🏗️ Architecture review
- 🔧 Maintainability analysis
- 📦 Dependency analysis
- 📚 Documentation review
- 🤖 AI-generated recommendations
- 📊 Quality scoring
- 🚦 Merge/quality gates
- 💬 GitLab Merge Request comments

---

# 🏗️ Current Architecture

The current version intentionally keeps the architecture simple.

```text
                 ┌─────────────────────┐
                 │      Developer      │
                 └──────────┬──────────┘
                            │
                            │ HTTP POST
                            ▼
                 ┌─────────────────────┐
                 │  ReviewController   │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │ CodeReviewService   │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │    LLMProvider      │
                 │     Interface       │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │ OllamaLLMProvider   │
                 └──────────┬──────────┘
                            │
                            │ HTTP
                            ▼
                 ┌─────────────────────┐
                 │       Ollama        │
                 │                     │
                 │ Qwen2.5-Coder 7B    │
                 └─────────────────────┘
```

### Current flow

```text
Source Code
    ↓
REST API
    ↓
CodeReviewService
    ↓
Review Prompt
    ↓
LLMProvider
    ↓
Ollama
    ↓
Qwen2.5-Coder
    ↓
AI Review
    ↓
API Response
```

---

# 🧰 Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Backend development |
| Spring Boot | REST API and application framework |
| Maven | Build and dependency management |
| Spring RestClient | Communication with Ollama |
| Ollama | Local LLM runtime |
| Qwen2.5-Coder 7B | Code analysis LLM |
| Git | Version control |
| GitHub/GitLab | Planned source repository integration |

### Planned technologies

| Technology | Planned Usage |
|---|---|
| GitLab | Repository and Merge Request integration |
| SonarQube | Static code analysis |
| SAST tools | Security analysis |
| Kafka | Event-driven processing |
| Redis | Caching and distributed state |
| PostgreSQL | Review history and findings |
| Docker | Containerization |
| Kubernetes | Deployment |
| AWS | Cloud deployment |
| OpenTelemetry | Observability |
| Prometheus/Grafana | Metrics and dashboards |

---

# 💻 Local Development

## Prerequisites

Install the following:

- Java 21+
- Maven
- Git
- Ollama

Verify Java:

```bash
java -version
```

Verify Git:

```bash
git --version
```

Verify Ollama:

```bash
ollama --version
```

---

# 🤖 Ollama Setup

Pull the local code model:

```bash
ollama pull qwen2.5-coder:7b
```

Verify the model:

```bash
ollama list
```

You should see:

```text
qwen2.5-coder:7b
```

You can also test the model directly:

```bash
ollama run qwen2.5-coder:7b
```

---

# ⚙️ Configuration

The application uses `application.properties`.

```properties
server.port=8080

llm.ollama.base-url=http://localhost:11434
llm.ollama.model=qwen2.5-coder:7b
```

The LLM provider is abstracted behind an interface so that the implementation can later be replaced with another local or cloud model.

---

# 📁 Project Structure

```text
codereviewer/
│
├── .mvn/
│   └── wrapper/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── vijay/
│   │   │           └── codereviewer/
│   │   │               │
│   │   │               ├── controller/
│   │   │               │   └── ReviewController.java
│   │   │               │
│   │   │               ├── service/
│   │   │               │   └── CodeReviewService.java
│   │   │               │
│   │   │               ├── llm/
│   │   │               │   ├── LLMProvider.java
│   │   │               │   └── OllamaLLMProvider.java
│   │   │               │
│   │   │               └── CodereviewerApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│
├── .gitignore
├── .gitattributes
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

# 🔌 API

## Review Source Code

### Endpoint

```http
POST /api/review
```

### Full URL

```text
http://localhost:8080/api/review
```

### Content-Type

```text
text/plain
```

### Example Request

```bash
curl -X POST http://localhost:8080/api/review \
-H "Content-Type: text/plain" \
--data-binary 'public class Test {

    public int divide(int a, int b) {
        return a / b;
    }
}'
```

---

# 🧪 Example Review

### Input

```java
public class Test {

    public int divide(int a, int b) {
        return a / b;
    }
}
```

The AI reviewer should identify that:

- Division by zero is possible.
- The method does not explicitly handle invalid input.
- A test case for `b = 0` is required.
- The API/consumer should define expected behavior for invalid input.

Example conceptual response:

```text
Severity: HIGH

Issue:
The divide method does not handle division by zero.

Impact:
Calling divide(a, 0) will throw ArithmeticException.

Recommendation:
Validate the divisor before performing the division or define
an explicit error-handling strategy.

Suggested test:
divide(10, 0)
```

> The actual response is generated dynamically by the selected LLM.

---

# 🧠 Current Review Prompt

The current reviewer asks the LLM to analyze:

1. Bugs
2. Security vulnerabilities
3. Performance problems
4. Code quality
5. Edge cases
6. Error handling
7. Maintainability
8. Input validation
9. Missing test cases

The reviewer is instructed to:

- Avoid inventing issues.
- Report only issues supported by the source code.
- Explain why an issue matters.
- Provide recommendations.
- Assign severity.

Severity levels:

```text
LOW
MEDIUM
HIGH
CRITICAL
```

---

# 🧩 LLM Abstraction

The application does not directly couple the business logic to Ollama.

Instead, it uses:

```java
public interface LLMProvider {

    String generate(String prompt);

}
```

Current implementation:

```text
LLMProvider
     │
     └── OllamaLLMProvider
```

This allows future implementations such as:

```text
LLMProvider
├── OllamaLLMProvider
├── OpenAIProvider
├── QwenCloudProvider
├── AnthropicProvider
└── LocalModelProvider
```

This abstraction is important because the review engine should not depend on a single LLM provider.

---

# 🗺️ Development Roadmap

The project will evolve incrementally.

## Phase 1 — Basic LLM Code Reviewer ✅

- [x] Spring Boot application
- [x] REST review endpoint
- [x] Ollama integration
- [x] Qwen2.5-Coder integration
- [x] LLM provider abstraction
- [x] Basic code review prompt
- [x] Severity classification
- [x] Git repository
- [x] Initial commit

---

## Phase 2 — Structured Code Review

Convert the current plain-text response into structured JSON.

Example:

```json
{
  "summary": "Potential division by zero",
  "score": 72,
  "findings": [
    {
      "severity": "HIGH",
      "category": "BUG",
      "file": "Test.java",
      "line": 4,
      "title": "Possible division by zero",
      "description": "The divisor is not validated.",
      "recommendation": "Validate the divisor before division.",
      "confidence": 0.98
    }
  ]
}
```

Planned improvements:

- Structured response model
- JSON schema
- Finding model
- Confidence score
- File and line references
- Better prompt engineering
- Validation of LLM output

---

# Phase 3 — Multi-Agent Architecture 🤖

Instead of one general-purpose reviewer, introduce specialized agents.

```text
                    Review Orchestrator
                           │
          ┌────────────────┼────────────────┐
          │                │                │
          ▼                ▼                ▼
     Bug Agent       Security Agent    Performance Agent
          │                │                │
          └────────────────┼────────────────┘
                           │
          ┌────────────────┼────────────────┐
          │                │                │
          ▼                ▼                ▼
    Quality Agent     Test Agent       Edge Case Agent
          │                │                │
          └────────────────┼────────────────┘
                           ▼
                   Finding Correlator
                           │
                           ▼
                    Scoring Engine
```

### Planned agents

- 🐛 Bug Detection Agent
- 🔐 Security Agent
- ⚡ Performance Agent
- 🧹 Code Quality Agent
- 🧪 Test Analysis Agent
- 🎯 Edge Case Agent
- 🏗️ Architecture Agent
- 📦 Dependency Agent
- 📚 Documentation Agent

---

# Phase 4 — Deterministic Analysis

AI should not be the only source of truth.

Integrate deterministic tools:

```text
                    Code Change
                        │
          ┌─────────────┼─────────────┐
          ▼             ▼             ▼
      SonarQube       SAST       Dependency Scan
          │             │             │
          └─────────────┼─────────────┘
                        ▼
                   AI Analysis
```

Planned integrations:

- SonarQube
- OWASP dependency analysis
- SAST tools
- Build validation
- Unit tests
- Static analysis
- Compiler errors
- Dependency vulnerability scanning

The AI will interpret and correlate deterministic findings instead of blindly replacing them.

---

# Phase 5 — Repository-Aware Review

The reviewer will understand more than the changed file.

Planned context:

```text
Changed Files
     │
     ├── Related Classes
     ├── Interfaces
     ├── Configuration
     ├── Tests
     ├── Dependencies
     └── Repository Documentation
```

The system will eventually analyze:

- Repository structure
- Related classes
- Existing implementations
- Interfaces
- Tests
- Configuration
- Dependency relationships
- API contracts
- Historical changes

This will reduce false positives and improve architectural reasoning.

---

# Phase 6 — GitLab Integration

Target flow:

```text
Developer
    │
    ▼
Git Push / Merge Request
    │
    ▼
GitLab
    │
    ▼
Webhook / CI Pipeline
    │
    ▼
Review Orchestrator
    │
    ▼
AI + Static Analysis
    │
    ▼
Review Report
    │
    ▼
GitLab Merge Request
```

The system will eventually:

- Detect changed files
- Analyze only relevant changes
- Review merge requests
- Publish inline comments
- Publish summary comments
- Store review history
- Fail the pipeline when blocking issues are detected

---

# Phase 7 — Quality Scoring 📊

Introduce weighted scoring.

Proposed scoring model:

| Category | Weight |
|---|---:|
| Bugs / Correctness | 20% |
| Code Quality | 15% |
| Security | 15% |
| Test Quality | 10% |
| Edge Cases | 10% |
| Performance | 10% |
| Maintainability | 5% |
| Architecture | 5% |
| Dependency / License | 5% |
| Documentation | 5% |
| **Total** | **100%** |

Example:

```text
Code Quality       92%
Correctness        84%
Security           96%
Testing            70%
Performance        88%
Maintainability    91%

Overall Score      86%
```

### Blocking rules

A high overall score should not hide critical vulnerabilities.

For example:

```text
Overall Score: 94%

BUT

Critical Security Issue: YES

Final Decision: ❌ BLOCK
```

This makes the scoring system more suitable for CI/CD.

---

# Phase 8 — Review Dashboard

Eventually provide a dashboard containing:

```text
┌─────────────────────────────────────┐
│         AI CODE REVIEW              │
├─────────────────────────────────────┤
│ Overall Score              86 / 100 │
│                                     │
│ Bugs                       2        │
│ Security                   1        │
│ Performance                3        │
│ Code Quality               4        │
│ Test Gaps                  5        │
│                                     │
│ Critical                   0        │
│ High                       2        │
│ Medium                     7        │
│ Low                        6        │
└─────────────────────────────────────┘
```

Possible future stack:

- React
- Spring Boot
- PostgreSQL
- Redis
- REST APIs
- WebSocket/SSE for live review progress

---

# Phase 9 — Production Architecture

Long-term architecture:

```text
                         GitLab
                           │
                           ▼
                    Webhook / CI
                           │
                           ▼
                 ┌──────────────────┐
                 │ Review Orchestrator│
                 └────────┬─────────┘
                          │
             ┌────────────┼────────────┐
             │            │            │
             ▼            ▼            ▼
        Change       Repository     Language
        Detector      Context       Detector
             │            │            │
             └────────────┼────────────┘
                          ▼
                  Analysis Pipeline
                          │
          ┌───────────────┼───────────────┐
          │               │               │
          ▼               ▼               ▼
       SonarQube        SAST          Dependency
          │               │               │
          └───────────────┼───────────────┘
                          ▼
                    AI Agent Layer
                          │
      ┌───────────────────┼───────────────────┐
      │                   │                   │
      ▼                   ▼                   ▼
   Bug Agent        Security Agent      Performance Agent
      │                   │                   │
      ├──────────────┬────┴────┬──────────────┤
                     ▼
              Finding Correlator
                     │
                     ▼
               Scoring Engine
                     │
          ┌──────────┼──────────┐
          ▼          ▼          ▼
       Report     GitLab MR   Dashboard
          │          │          │
          └──────────┼──────────┘
                     ▼
                Merge Gate
```

---

# 🔐 AI Safety and Reliability Principles

The reviewer should prioritize **evidence over speculation**.

### Core principles

1. Do not invent vulnerabilities.
2. Do not report issues without code evidence.
3. Include file and line information whenever possible.
4. Include confidence for AI-generated findings.
5. Correlate AI findings with static analysis.
6. Avoid duplicate findings from multiple agents.
7. Allow human developers to dismiss findings.
8. Keep deterministic tools authoritative for deterministic checks.
9. Never automatically modify production code without explicit approval.
10. Critical security findings should require strong evidence.

---

# 🧠 Future Agent Workflow

A future review could work like this:

```text
1. Receive GitLab Merge Request
          ↓
2. Detect changed files
          ↓
3. Detect programming languages
          ↓
4. Collect repository context
          ↓
5. Run build and tests
          ↓
6. Run SonarQube
          ↓
7. Run security/dependency scanners
          ↓
8. Execute specialized AI agents
          ↓
9. Correlate duplicate findings
          ↓
10. Validate findings
          ↓
11. Calculate category scores
          ↓
12. Calculate overall score
          ↓
13. Apply merge-gate rules
          ↓
14. Generate review report
          ↓
15. Publish results to GitLab
```

---

# 🎯 Current Goal

The immediate objective is **not** to build the entire platform at once.

The project will be developed incrementally:

```text
Basic LLM Reviewer
        ↓
Structured Findings
        ↓
Specialized Agents
        ↓
Repository Context
        ↓
Static Analysis
        ↓
Scoring Engine
        ↓
GitLab Integration
        ↓
CI/CD Quality Gate
        ↓
Production Multi-Agent Platform
```

This approach allows every stage to be independently tested and demonstrated.

---

# 📌 Project Status

### Current

🟢 Basic local LLM code review is working.

```text
Spring Boot
     ↓
Ollama
     ↓
Qwen2.5-Coder 7B
     ↓
Code Review
```

### Next milestone

**Structured Code Review Engine**

The next implementation will introduce:

- `ReviewRequest`
- `ReviewResponse`
- `Finding`
- `Severity`
- `Category`
- Structured JSON output
- LLM response validation
- Better code-review prompt
- Review score

---

# 📈 Long-Term Objective

Build a production-grade **AI Code Review Platform** capable of reviewing real-world software repositories and helping engineering teams identify defects, security issues, performance problems, test gaps, and maintainability risks before code reaches production.

The final platform should combine:

**LLMs + Static Analysis + Security Scanning + Testing + Repository Context + CI/CD + Human Review**

rather than relying on an LLM alone.

---

## 👨‍💻 Author

**Vijay Vasi**

Senior Java Backend Developer

Focus areas:

- Java
- Spring Boot
- Microservices
- Kafka
- Redis
- AWS
- Kubernetes
- CI/CD
- Generative AI
- AI-assisted Software Engineering

---

## ⭐ Roadmap Philosophy

> Start simple.  
> Build one agent.  
> Make it reliable.  
> Add specialized agents.  
> Add deterministic analysis.  
> Add repository context.  
> Integrate with CI/CD.  
> Then scale into a production-grade AI engineering platform.

---

## 📄 License

This project is currently intended as a personal learning and portfolio project.
