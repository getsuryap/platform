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

