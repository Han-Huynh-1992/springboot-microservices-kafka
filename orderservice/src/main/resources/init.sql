-- ============================================================
-- OrderService Database Setup
-- ============================================================

CREATE DATABASE IF NOT EXISTS order_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE order_db;

-- ============================================================
-- Table: orders
-- ============================================================

CREATE TABLE IF NOT EXISTS orders (
    order_id          VARCHAR(36)     NOT NULL,
    order_number      VARCHAR(20)     NOT NULL,
    customer_id       VARCHAR(36)     NOT NULL,
    customer_name     VARCHAR(255)    NOT NULL,
    customer_email    VARCHAR(255)    NOT NULL,
	customer_address  VARCHAR(255)    NOT NULL,
    total_amount      DOUBLE          NOT NULL,
    status            ENUM('CREATED', 'PAID', 'CANCELLED') NOT NULL DEFAULT 'CREATED',
    created_date      DATETIME        NOT NULL,
    updated_date      DATETIME,

    CONSTRAINT pk_orders PRIMARY KEY (order_id)
);

-- ============================================================
-- Table: order_items
-- ============================================================

CREATE TABLE IF NOT EXISTS order_items (
    id              VARCHAR(36)     NOT NULL,
    order_id        VARCHAR(36)     NOT NULL,
    product_id      VARCHAR(36)     NOT NULL,
    product_name    VARCHAR(255)    NOT NULL,
    quantity        INT             NOT NULL,
    price           DOUBLE          NOT NULL,

    CONSTRAINT pk_order_items   PRIMARY KEY (id),
    CONSTRAINT fk_order_items   FOREIGN KEY (order_id)
                                REFERENCES orders(order_id)
                                ON DELETE CASCADE
);

-- ============================================================
-- Table: outbox_events (Transactional Outbox Pattern)
-- ============================================================
 
CREATE TABLE IF NOT EXISTS outbox_events (
    id           VARCHAR(36)                  NOT NULL,
    topic        VARCHAR(255)                 NOT NULL,
    message_key  VARCHAR(255)                 NOT NULL,
    payload      TEXT                         NOT NULL,
    status       ENUM('PENDING','PUBLISHED')  NOT NULL DEFAULT 'PENDING',
    created_date DATETIME                     NOT NULL,
 
    CONSTRAINT pk_outbox_events PRIMARY KEY (id)
);
