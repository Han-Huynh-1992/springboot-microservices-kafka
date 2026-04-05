-- ============================================================
-- EmailService Database Setup
-- ============================================================

CREATE DATABASE IF NOT EXISTS email_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE email_db;

-- ============================================================
-- Table: email_logs
-- ============================================================

CREATE TABLE IF NOT EXISTS email_logs (
    email_id        VARCHAR(36)             NOT NULL,
    order_id        VARCHAR(36)             NOT NULL,
    recipient_email VARCHAR(255)            NOT NULL,
    subject         VARCHAR(255)            NOT NULL,
    body            TEXT                    NOT NULL,
    status          ENUM('SENT', 'FAILED')  NOT NULL DEFAULT 'SENT',
    error_message   TEXT,
    sent_date       DATETIME                NOT NULL,

    CONSTRAINT pk_email_logs PRIMARY KEY (email_id)
);
