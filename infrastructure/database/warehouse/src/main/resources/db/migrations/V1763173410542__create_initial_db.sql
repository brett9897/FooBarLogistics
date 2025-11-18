-- Migration: Create Initial DB
-- Created: Fri Nov 14 20:23:30 CST 2025

-- Warehouse Table
CREATE TABLE warehouse (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Inventory Table
CREATE TABLE inventory (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sku VARCHAR(255) NOT NULL,
    warehouse_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_warehouse
        FOREIGN KEY(warehouse_id)
        REFERENCES warehouse(id)
);
