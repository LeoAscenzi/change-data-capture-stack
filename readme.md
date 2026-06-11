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
[████████████████████████████████████░] 95%
```

| Component   | Status      |
| ----------- | ----------- |
| API         | 95%         |
| Database    | 95%         |
| Debezium    | 100%        |
| Kafka       | 99%         |
| Flink       | 99%         |
| Fraud Logic | 90%         |

---

## Stack

Spring Boot · Database · Debezium · Kafka · Flink · Elasticsearch

---

## Next Steps

* Define better fraud detection patterns
* Scale services
* Test load handling
* Make banning users or locking accounts persistent
* Host on AWS
