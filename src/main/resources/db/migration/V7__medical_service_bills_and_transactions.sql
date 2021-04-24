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

DROP TABLE IF EXISTS `m_bills`;
CREATE TABLE `m_bills`(
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Bill id',
  is_paid boolean NOT NULL default false COMMENT 'check if this bill is paid',
  extra_id VARCHAR (50) NOT NULL comment 'bill extra name',
  total_amount DECIMAL(13,4) DEFAULT 0   COMMENT 'total expected amount ',
  paid_amount DECIMAL(13,4) DEFAULT 0  COMMENT 'amount paid',
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL comment 'bill date',
  created_by VARCHAR (200) comment 'bill creator',
  last_modified_date TIMESTAMP NULL comment 'bill modification date',
  last_modified_by VARCHAR (200) comment 'bill modifier',
  consultation_id bigint comment 'consultation bill id',
  constraint contact_pk primary key (`id`),
  constraint consultation_bill_fk  foreign key (`consultation_id`) references `m_consultations`(`id`) ON DELETE CASCADE,
  constraint bill_unique_key unique (`id`,`consultation_id`),
  constraint consultation_bill_unique_key unique(`consultation_id`)
) COLLATE = 'utf8_unicode_ci' ENGINE = InnoDB;



DROP TABLE IF EXISTS `m_transactions`;
CREATE TABLE IF NOT EXISTS `m_transactions`(
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'transaction id',
  currency_code VARCHAR (5) NOT NULL COMMENT 'currency used' ,
  amount decimal (13,4) COMMENT 'transaction amount' ,
  is_reversed boolean default false COMMENT 'check if transaction was reversed' ,
  transaction_date DATETIME NOT NULL COMMENT 'transactio date',
   constraint transactions_primary_key primary key  (`id`),
  bill_id BIGINT null references `m_bills`(`id`) ON DELETE NO ACTION,
  department_id BIGINT references `m_department`(`id`) ON DELETE NO ACTION,
  medical_service_id BIGINT  references `m_services`(`id`) ON DELETE NO ACTION,
  CONSTRAINT fk_m_transactions_services FOREIGN KEY(`medical_service_id`) REFERENCES `m_services`(`id`),
  CONSTRAINT fk_consultation_transactions_bill FOREIGN KEY(`bill_id`) REFERENCES `m_bills`(`id`),
  CONSTRAINT fk_medical_service_departments FOREIGN KEY(`department_id`) REFERENCES `m_department`(`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;
