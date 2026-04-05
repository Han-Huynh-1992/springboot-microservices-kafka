-- ============================================================
-- ShipmentService Database Setup
-- ============================================================

CREATE DATABASE IF NOT EXISTS shipment_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE shipment_db;

-- ============================================================
-- Table: shipments
-- ============================================================

CREATE TABLE IF NOT EXISTS shipments (
    shipment_id             VARCHAR(36)                             NOT NULL,
    order_id                VARCHAR(36)                             NOT NULL,
    shipping_address        VARCHAR(500)                            NOT NULL,
    estimated_delivery_date DATETIME                                NOT NULL,                     
    status                  ENUM('PENDING','SHIPPED','DELIVERED')   NOT NULL DEFAULT 'PENDING',
    created_date            DATETIME                                NOT NULL,
    shipped_date            DATETIME,

    CONSTRAINT pk_shipments PRIMARY KEY (shipment_id)
);

-- ============================================================
-- Table: shipment_items
-- ============================================================

CREATE TABLE IF NOT EXISTS shipment_items (
    id              VARCHAR(36)     NOT NULL,
    shipment_id     VARCHAR(36)     NOT NULL,
    product_id      VARCHAR(36)     NOT NULL,
    quantity        INT             NOT NULL,

    CONSTRAINT pk_shipment_items    PRIMARY KEY (id),
    CONSTRAINT fk_shipment_items    FOREIGN KEY (shipment_id)
                                    REFERENCES shipments(shipment_id)
                                    ON DELETE CASCADE
);

-- ============================================================
-- Table: shipment_failed_logs
-- ============================================================
 
CREATE TABLE IF NOT EXISTS shipment_failed_logs (
    id               VARCHAR(36)     NOT NULL,
    order_id         VARCHAR(36)     NOT NULL,
    order_number     VARCHAR(20),
    customer_name    VARCHAR(255),
    customer_email   VARCHAR(255),
    error_message    TEXT,
    resolved_status                  ENUM('PENDING','RESOLVED')   NOT NULL DEFAULT 'PENDING',
    created_date     DATETIME        NOT NULL,
 
    CONSTRAINT pk_shipment_failed_logs PRIMARY KEY (id)
);
