-- Add medicine price for transaction
-- Add column name price
ALTER TABLE `m_medicines` ADD COLUMN `price`  DECIMAL(19,2) NOT NULL after `units`;
ALTER TABLE `m_transactions`
          ADD COLUMN `medicine_id`  BIGINT  references `m_medicines`(`id`) ON DELETE NO ACTION,
          ADD CONSTRAINT FK_medicine_transactions FOREIGN KEY(`medicine_id`) REFERENCES `m_medicines`(`id`);