#!/bin/bash
set -e

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🔄 Database Sync Script${NC}"
echo "======================"

# Navigate to the database directory
cd "$(dirname "$0")/.."

# Check if .env exists
if [ ! -f ".env" ]; then
    echo -e "${YELLOW}⚠️  No .env file found. Copying from example.env...${NC}"
    if [ -f "example.env" ]; then
        cp example.env .env
        echo -e "${YELLOW}📝 Please edit .env with your database credentials${NC}"
        echo "   Then run this script again."
        exit 1
    else
        echo -e "${RED}❌ No example.env found either. Cannot proceed.${NC}"
        exit 1
    fi
fi

echo -e "${YELLOW}🔄 Pulling latest changes...${NC}"
git pull

echo -e "${YELLOW}🛑 Stopping existing containers...${NC}"
docker-compose down

echo -e "${YELLOW}📦 Starting PostgreSQL container...${NC}"
docker-compose up -d

echo -e "${YELLOW}⏳ Waiting for PostgreSQL to be ready...${NC}"
sleep 5

# Wait for PostgreSQL to be fully ready
max_attempts=30
attempt=0
while [ $attempt -lt $max_attempts ]; do
    if docker-compose exec -T db pg_isready -U ${POSTGRES_USER:-postgres} > /dev/null 2>&1; then
        echo -e "${GREEN}✅ PostgreSQL is ready${NC}"
        break
    fi
    attempt=$((attempt + 1))
    echo -e "${YELLOW}   Waiting for PostgreSQL... ($attempt/$max_attempts)${NC}"
    sleep 2
done

if [ $attempt -eq $max_attempts ]; then
    echo -e "${RED}❌ PostgreSQL failed to start within expected time${NC}"
    exit 1
fi

echo -e "${YELLOW}🗄️  Creating/updating databases...${NC}"
./scripts/create-databases.sh

echo -e "${GREEN}✅ Database sync completed successfully!${NC}"
echo -e "${BLUE}📊 Current database status:${NC}"
./scripts/list-databases.sh