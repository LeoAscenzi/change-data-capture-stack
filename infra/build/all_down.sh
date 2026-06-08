#!/usr/bin/env bash
set -euo pipefail

ENV="${1:-local}"

case "$ENV" in
  local)
    docker compose \
      -f docker-compose.yml \
      -f docker-compose.local.yml \
      --env-file .env.local \
      down
    ;;
  prod)
    docker compose \
      -f docker-compose.yml \
      -f docker-compose.prod.yml \
      --env-file .env.prod \
      down
    ;;
  *)
    echo "Unknown env: $ENV"
    exit 1
    ;;
esac