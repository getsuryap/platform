-- Define Medical service properties
-- Define Medical services transactions.
-- Define Medical service and transaction relation.
-- Define Medical service and department relation.
-- Define Medical service.
-- Define Transaction and Consultation relation.


DROP TABLE IF EXISTS `m_services`;
CREATE TABLE IF NOT EXISTS `m_services`(
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'sesrvice id',
  name VARCHAR (50) NOT  NULL COMMENT 'service name',
  price  DECIMAL(13,4) NOT NULL COMMENT 'service price',
  enabled boolean NOT NULL default true COMMENT 'check if this service is still provided',
  primary key (`id`),
  unique key(`name`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

DROP TABLE IF EXISTS `m_transactions`;
CREATE TABLE IF NOT EXISTS `m_transactions`(
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'transaction id',
  currency_code VARCHAR (5) NOT NULL COMMENT 'currency used' ,
  amount decimal (13,4) COMMENT 'transaction amount' ,
  is_reversed boolean default false COMMENT 'check if transaction was reversed' ,
  transaction_date DATETIME NOT NULL COMMENT 'transactio date',
  consultation_id BIGINT  references `m_consultations`(`id`) ON DELETE NO ACTION,
  department_id BIGINT references `m_departments`(`id`) ON DELETE NO ACTION,
  medical_service_id BIGINT  references `m_services`(`id`) ON DELETE NO ACTION,
  CONSTRAINT FK_m_transactions_services FOREIGN KEY(`medical_service_id`) REFERENCES `m_services`(`id`),
  CONSTRAINT FK_consultation_transactions FOREIGN KEY(`consultation_id`) REFERENCES `m_consultations`(`id`),
  CONSTRAINT FK_medical_service_transactions FOREIGN KEY(`medical_service_id`) REFERENCES `m_services`(`id`),
  constraint transactions_primary_key primary key  (`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;
