#!/bin/sh
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER debezium WITH PASSWORD 'dbz_password';
    ALTER USER debezium WITH REPLICATION;
    ALTER USER debezium WITH SUPERUSER; -- Required to create publications seamlessly
EOSQL
