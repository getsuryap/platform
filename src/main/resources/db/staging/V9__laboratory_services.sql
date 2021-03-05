-- Define Medical laboratory services

DROP TABLE IF EXISTS `m_laboratory_services`;
CREATE TABLE `m_laboratory_services`(
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'laboratory service id',
  name VARCHAR (50) NOT  NULL COMMENT 'laboratory service name',
  descriptions VARCHAR (250) NULL COMMENT 'laboratory service extra descriptions',
  price  DECIMAL(13,4) NOT NULL COMMENT 'laboratory service price',
  is_active boolean NOT NULL default true COMMENT 'check if this service is active',
  primary key (`id`),
  unique key(`name`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

ALTER TABLE `m_staff`
      ADD COLUMN `is_active` boolean NOT NULL default true COMMENT 'check if this staff is active',
      ADD COLUMN `is_available` boolean NOT NULL default true COMMENT 'check if this staff is available';

DROP TABLE IF EXISTS `m_bills`;
CREATE TABLE `m_bills`(
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Bill id',
  is_paid boolean NOT NULL default false COMMENT 'check if this bill is paid',
  extra_id VARCHAR (50) NOT NULL comment 'bill extra name',
  total_amount DECIMAL(13,4) DEFAULT 0   COMMENT 'total expected amount ',
  paid_amount DECIMAL(13,4) DEFAULT 0  COMMENT 'amount paid',
  created_date TIMESTAMP comment 'bill date',
  created_by VARCHAR (200) comment 'bill creator',
  last_modified_date TIMESTAMP comment 'bill modification date',
  last_modified_by VARCHAR (200) comment 'bill modifier',
  consultation_id bigint comment 'consultation bill id',
  CONSTRAINT contact_pk primary key (`id`),
  constraint consultation_bill_fk  foreign key (`consultation_id`) references `m_consultations`(`id`) ON DELETE CASCADE,
  constraint bill_unique_key unique (`id`,`consultation_id`),
  constraint consultation_bill_unique_key unique(`consultation_id`)
) COLLATE = 'utf8_unicode_ci' ENGINE = InnoDB;


DROP TABLE IF EXISTS `m_radiology`;
CREATE TABLE `m_radiology`(
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'radiology service id',
  name VARCHAR (50) NOT  NULL COMMENT 'radiology service name',
  descriptions VARCHAR (250) NULL COMMENT 'radiology service extra descriptions',
  price  DECIMAL(13,4) NOT NULL COMMENT 'radiology service price',
  is_active boolean NOT NULL default true COMMENT 'check if this service is active',
  primary key (`id`),
  unique key(`name`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

DROP TABLE IF EXISTS `m_reports`;
CREATE TABLE `m_reports`(
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'report id',
  name VARCHAR (50) NOT  NULL COMMENT 'report name',
  filename VARCHAR (50) NOT  NULL COMMENT 'fiport file name',
  descriptions VARCHAR (250) NULL COMMENT 'extra descriptions',
  `type` BIGINT  NOT NULL COMMENT 'report type',
  primary key (`id`),
  unique key(`name`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

REPLACE INTO `m_privilege` ( `name`) VALUES
  ("DELETE_PRIVILEGE"),("ALL_FUNCTIONS"),("READ_USER"),
  ("READ_BILL"),("CREATE_BILL"),("UPDATE_BILL"),("DELETE_BILL"),
 ("READ_DEPARTMENT"),("CREATE_DEPARTMENT"),("UPDATE_DEPARTMENT"),("DELETE_DEPARTMENT");