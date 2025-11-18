#!/bin/bash
set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

cd "$(dirname "$0")/.."

# Load environment variables
export $(cat .env.common | grep -v '#' | grep -v '^$' | xargs)
if [ -f ".env" ]; then
    export $(cat .env | grep -v '#' | grep -v '^$' | xargs)
fi

DB_HOST=${POSTGRES_HOST:-localhost}
DB_PORT=${POSTGRES_PORT:-5432}
DB_USER=${POSTGRES_USER}
DB_PASSWORD=${POSTGRES_PASSWORD}

echo -e "${BLUE}📊 Database Status${NC}"
echo "=================="

if [ -z "$DB_USER" ] || [ -z "$DB_PASSWORD" ]; then
    echo -e "${RED}❌ Missing database credentials${NC}"
    exit 1
fi

echo -e "${YELLOW}🔍 Listing all databases:${NC}"
PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres -c "
SELECT
    datname as \"Database Name\",
    pg_size_pretty(pg_database_size(datname)) as \"Size\"
FROM pg_database
WHERE datistemplate = false
ORDER BY datname;
"

echo ""
echo -e "${YELLOW}📋 Expected databases: ${MULTIPLE_DB_NAMES:-warehouse}${NC}"

for db in $(echo ${MULTIPLE_DB_NAMES:-warehouse} | tr ',' ' '); do
    db_trimmed=$(echo "$db" | xargs)
    if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname='$db_trimmed'" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ $db_trimmed${NC}"
    else
        echo -e "${RED}❌ $db_trimmed (missing)${NC}"
    fi
done