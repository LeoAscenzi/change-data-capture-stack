FROM postgres:18-bookworm

ENV POSTGRES_USER=admin
ENV POSTGRES_PASSWORD=admin
ENV POSTGRES_DB=db

COPY postgresql.conf /etc/postgresql/postgresql.conf
COPY 01-init.sql /docker-entrypoint-initdb.d/
COPY 02-init-debezium.sh /docker-entrypoint-initdb.d/

CMD ["postgres", "-c", "config_file=/etc/postgresql/postgresql.conf"]

EXPOSE 5432