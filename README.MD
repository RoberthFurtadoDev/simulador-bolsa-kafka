# 📈 Plataforma Financeira com Java, Spring Boot e Apache Kafka

![Java](https://img.shields.io/badge/Java-21-blue?style=for-the-badge\&logo=openjdk) ![Spring](https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge\&logo=spring) ![Kafka](https://img.shields.io/badge/Apache_Kafka-3.x-black?style=for-the-badge\&logo=apachekafka) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge\&logo=postgresql) ![Docker](https://img.shields.io/badge/Docker-gray?style=for-the-badge\&logo=docker)

---

## 📑 Visão Geral

Este projeto simula um ecossistema completo de processamento de dados financeiros em tempo real. Utilizando uma arquitetura de microsserviços reativa e orientada a eventos, a plataforma demonstra um fluxo de dados desde a geração até a análise, notificação e visualização.

É ideal como projeto de portfólio para demonstrar competências em desenvolvimento back-end, arquitetura distribuída, streaming, persistência e visualização em tempo real.

---

## 🏗️ Arquitetura do Sistema

A arquitetura foi desenhada para ser desacoplada, resiliente e escalável. O **Apache Kafka** atua como backbone (message broker), permitindo comunicação assíncrona entre microsserviços.

```mermaid
flowchart TD
  subgraph Infra["Infraestrutura (Docker Compose)"]
    ZK[Zookeeper (opcional)]
    Kafka[Apache Kafka Cluster]
    Postgres[PostgreSQL]
    Grafana[Grafana / Prometheus (opcional)]
  end

  subgraph Microsserviços["Microsserviços"]
    Simulator["market-data-simulator<br/>(Producer)"]
    Monitor["portfolio-monitor<br/>(API REST + Consumer)"]
    Indicator["technical-indicator-service<br/>(Kafka Streams)"]
    Notifier["notification-service<br/>(Consumer)"]
    Dashboard["dashboard-service<br/>(WebSocket + Consumer)"]
  end

  subgraph Externas["Interfaces Externas"]
    User[/'👤 Usuário (Postman, Curl)'/]
    Browser[/'💻 Navegador (Dashboard - WebSocket)'/]
  end

  Simulator -- "Publica cotações → topic: cotacoes_acoes" --> Kafka
  Kafka -- "Entrega cotações" --> Monitor
  Kafka -- "Entrega cotações" --> Indicator
  Kafka -- "Entrega cotações" --> Dashboard

  Indicator -- "Publica indicadores → topic: indicadores_tecnicos" --> Kafka
  Kafka -- "Entrega indicadores" --> Dashboard

  User -- "POST /alerts" --> Monitor
  Monitor -- "Persiste alertas → PostgreSQL" --> Postgres
  Monitor -- "Publica evento → topic: alertas_disparados" --> Kafka
  Kafka -- "Consumido" --> Notifier
  Notifier -- "Envia/Simula notificação (log/email/push)" --> Notifier

  Dashboard -- "Push via WebSocket" --> Browser

  style Simulator fill:#d4edda,stroke:#155724
  style Monitor fill:#cce5ff,stroke:#004085
  style Indicator fill:#fff3cd,stroke:#856404
  style Notifier fill:#f5c6cb,stroke:#721c24
  style Dashboard fill:#d1ecf1,stroke:#0c5460
  style Kafka fill:#f0f0f0,stroke:#e07a5f,stroke-width:2px
  style Postgres fill:#c8f7e5,stroke:#116c5a
```

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia              | Versão/Tipo   | Descrição/Propósito                                                 |
|------------------------:|:-------------:|---------------------------------------------------------------------|
| Java                   | 21            | Linguagem principal; uso de recursos modernos (Records, etc.)       |
| Spring Boot            | 3.x           | Framework para microsserviços                                       |
| Spring WebFlux         | -             | Reatividade e WebSockets (dashboard)                                |
| Apache Kafka           | 3.x           | Message broker e streaming                                          |
| Kafka Streams          | -             | Processamento de streams (indicadores, windowed/stateful)           |
| PostgreSQL             | 15            | Persistência de alertas e histórico                                 |
| Docker / Docker Compose| -             | Ambiente reproducível para infraestrutura (Kafka, DB, etc.)        |
| Maven                  | 3.8+          | Build e gerenciamento de dependências                               |
| Lombok                 | -             | Redução de boilerplate (getters/setters, construtores, etc.)        |
| HTML / CSS / JS        | -             | Tecnologias front-end para o dashboard                              |
| Postman / curl         | -             | Ferramentas para testes e validação da API                          |
| Prometheus / Grafana   | -             | Observability / métricas e dashboards (opcional)                    |


---

## ⚙️ Como Rodar o Projeto (resumo)

1. **Pré-requisitos:** JDK 21, Maven, Docker, Docker Compose.
2. **Iniciar infraestrutura:** `docker-compose up -d` (na raiz do repo).
3. **Iniciar microsserviços:** em terminais separados, `mvn spring-boot:run` nas pastas de cada serviço.

Ordem sugerida: `market-data-simulator` → `portfolio-monitor` → `technical-indicator-service` → `notification-service` → `dashboard-service`.

---

## ⚙️ Como Usar / Endpoints (exemplo)

* **Dashboard:** `http://localhost:8085`
* **Criar alerta:** `POST http://localhost:8082/alerts`

Exemplo de body JSON:

```json
{
  "symbol": "PETR4",
  "targetPrice": 20.00,
  "direction": "LESS_OR_EQUAL"
}
```

---

## 🔬 Schemas (exemplos)

**TICK (cotação) — `cotacoes_acoes`**

```json
{
  "symbol": "PETR4",
  "timestamp": "2025-09-23T21:05:00Z",
  "price": 19.85,
  "volume": 1200
}
```

**INDICADOR — `indicadores_tecnicos`**

```json
{
  "symbol": "PETR4",
  "type": "SMA",
  "window": 20,
  "value": 20.12,
  "timestamp": "2025-09-23T21:05:00Z"
}
```

**ALERTA (disparado) — `alertas_disparados`**

```json
{
  "alertId": "uuid-v4",
  "symbol": "PETR4",
  "targetPrice": 20.00,
  "currentPrice": 19.99,
  "direction": "LESS_OR_EQUAL",
  "firedAt": "2025-09-23T21:05:01Z"
}
```

---

## ✅ Próximos passos (sugestões)

* Versionar schemas com Avro / Schema Registry.
* Adicionar retry/backoff, DLQ e idempotência.
* Integrar observability (Prometheus + Grafana).
* Adicionar autenticação (JWT/OAuth2) nas APIs.

---

## Autor

**Roberth Furtado Ferreira de Oliveira** — LinkedIn: [https://www.linkedin.com/in/roberthfurtado/](https://www.linkedin.com/in/roberthfurtado/) — GitHub: [https://github.com/RoberthFurtadoDev](https://github.com/RoberthFurtadoDev)



