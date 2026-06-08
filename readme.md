# CDC Pipeline Experiment

Lightweight project to explore Change Data Capture (CDC) pipelines for real-time processing and search.

---

## Architecture

**Fraud Detection (in progress)**

```
Spring Boot API → Database → Debezium → Kafka → Flink → Fraud Detection
```

**Search / Analytics**

```
Spring Boot API → Database → Debezium → Kafka → Elasticsearch
```

---

## Progress (Fraud Pipeline)

```
[██████████░░░░░░░░░░░░░░░░░░░░░░] 35%
```

| Component   | Status      |
| ----------- | ----------- |
| API         | 80%         |
| Database    | 90%         |
| Debezium    | In progress |
| Kafka       | Not started |
| Flink       | Not started |
| Fraud Logic | Not started |

---

## Stack

Spring Boot · Database · Debezium · Kafka · Flink · Elasticsearch

---

## Next Steps

* Configure Debezium connector
* Stand up Kafka and topics
* Validate CDC event flow
* Implement Flink processing job
* Define fraud detection patterns
