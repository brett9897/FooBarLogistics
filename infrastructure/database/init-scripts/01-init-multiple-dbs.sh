#!/bin/bash
set -e

function create_user_and_database() {
	local database=$1
	echo "Creating database '$database'"
	psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
	    CREATE DATABASE $database;
	    GRANT ALL PRIVILEGES ON DATABASE $database TO $POSTGRES_USER;
EOSQL
}

if [ -n "$MULTIPLE_DB_NAMES" ]; then
	echo "Multiple database creation requested: $MULTIPLE_DB_NAMES"
	for db in $(echo "$MULTIPLE_DB_NAMES" | tr ',' ' '); do
		create_user_and_database "$db"
	done
	echo "Multiple databases created"
fi