#!/usr/bin/env bash
set -euo pipefail

ENV="${1:-local}"

echo "📦 Building Springboot API JAR..."
(cd ../../backend/api && mvn clean package -q -DskipTests)

echo "📦 Building Flink job JAR..."
(cd ../../backend/flink && mvn clean package -q)

case "$ENV" in
  local)
    docker compose \
      -f docker-compose.yml \
      -f docker-compose.local.yml \
      --env-file .env.local \
      up -d --build
    ;;
  prod)
    docker compose \
      -f docker-compose.yml \
      -f docker-compose.prod.yml \
      --env-file .env.prod \
      up -d
    ;;
  *)
    echo "Unknown env: $ENV"
    exit 1
    ;;
esac

echo "⏳ Waiting for Kafka Connect to be fully healthy..."
until curl -s -o /dev/null -w "%{http_code}" http://localhost:8083/connectors | grep -q "200"; do
  printf '.'
  sleep 2
done
echo -e "\n✅ Kafka Connect is up and listening!"

echo "⏳ Waiting for PostgreSQL database to accept connections..."
until docker exec db pg_isready -U admin -d db &>/dev/null; do
  printf '.'
  sleep 2
done
echo -e "\n✅ PostgreSQL is ready!"

echo "📦 Registering Debezium PostgreSQL connector..."
curl -s -X POST \
  -H "Content-Type: application/json" \
  -d @postgres-connector.json \
  http://localhost:8083/connectors/
echo -e "\n🎉 Connector successfully initialized and monitoring 'transactions'!"

echo "⏳ Waiting for Flink JobManager to be ready..."
until curl -s -o /dev/null -w "%{http_code}" http://localhost:8081/overview | grep -q "200"; do
  printf '.'
  sleep 2
done
echo -e "\n✅ Flink JobManager is up!"

echo "🚀 Submitting Flink job..."
docker exec flink-jobmanager flink run -d /opt/flink/jobs/job.jar
echo -e "\n✅ Flink job submitted! Monitor at http://localhost:8081"