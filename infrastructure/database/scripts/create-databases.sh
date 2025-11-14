#!/bin/bash
set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🗄️  Database Creation Script${NC}"
echo "================================"

# Navigate to the database directory
cd "$(dirname "$0")/.."

# Load environment variables from .env file
if [ -f ".env" ]; then
    echo -e "${YELLOW}📄 Loading environment variables from .env${NC}"
    export $(cat .env.common | grep -v '#' | grep -v '^$' | xargs)
    export $(cat .env | grep -v '#' | grep -v '^$' | xargs)
else
    echo -e "${RED}⚠️  No .env file found. Please create one based on example.env${NC}"
    exit 1
fi

# Database connection settings
DB_HOST=${POSTGRES_HOST:-localhost}
DB_PORT=${POSTGRES_PORT:-5432}
DB_USER=${POSTGRES_USER}
DB_PASSWORD=${POSTGRES_PASSWORD}
DATABASES=${MULTIPLE_DB_NAMES:-"warehouse"}

# Validate required environment variables
if [ -z "$DB_USER" ] || [ -z "$DB_PASSWORD" ]; then
    echo -e "${RED}❌ Missing required environment variables: POSTGRES_USER and/or POSTGRES_PASSWORD${NC}"
    exit 1
fi

function test_connection() {
    echo -e "${YELLOW}🔌 Testing PostgreSQL connection...${NC}"
    if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres -c "SELECT version();" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ PostgreSQL connection successful${NC}"
        return 0
    else
        echo -e "${RED}❌ Cannot connect to PostgreSQL. Is the server running?${NC}"
        echo "   Connection: postgresql://$DB_USER@$DB_HOST:$DB_PORT"
        return 1
    fi
}

function database_exists() {
    local database=$1
    local result=$(PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname='$database'" 2>/dev/null || echo "0")
    [ "$result" = "1" ]
}

function create_database_if_not_exists() {
    local database=$1

    if database_exists "$database"; then
        echo -e "${GREEN}✓ Database '$database' already exists${NC}"
    else
        echo -e "${YELLOW}→ Creating database '$database'${NC}"
        if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres -c "CREATE DATABASE $database;" > /dev/null 2>&1; then
            echo -e "${GREEN}✅ Database '$database' created successfully${NC}"
        else
            echo -e "${RED}❌ Failed to create database '$database'${NC}"
            return 1
        fi
    fi
}

# Main execution
if ! test_connection; then
    exit 1
fi

echo -e "${BLUE}📋 Processing databases: $DATABASES${NC}"
echo "--------------------------------"

for db in $(echo $DATABASES | tr ',' ' '); do
    db_trimmed=$(echo "$db" | xargs)  # Trim whitespace
    create_database_if_not_exists "$db_trimmed"
done

echo "--------------------------------"
echo -e "${GREEN}🎉 All databases are ready!${NC}"