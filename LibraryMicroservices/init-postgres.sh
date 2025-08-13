#!/bin/bash
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE userdb;
    CREATE DATABASE bookdb;
    GRANT ALL PRIVILEGES ON DATABASE userdb TO bereketab24;
    GRANT ALL PRIVILEGES ON DATABASE bookdb TO bereketab24;
EOSQL