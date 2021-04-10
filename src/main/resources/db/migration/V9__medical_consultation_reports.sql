-- Define Medical laboratory services

ALTER TABLE `m_staff`
      ADD COLUMN `is_active` boolean NOT NULL default true COMMENT 'check if this staff is active',
      ADD COLUMN `is_available` boolean NOT NULL default true COMMENT 'check if this staff is available';


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



ALTER TABLE `users`
        ADD COLUMN `is_self_service` boolean NOT NULL default false COMMENT 'check if user is self service',
        ADD COLUMN `patient_id` BIGINT NULL COMMENT 'self service patient account',
        ADD CONSTRAINT `fk_patient_self_service_account` FOREIGN KEY (`patient_id`) REFERENCES `m_patients`(`id`) ON DELETE CASCADE;

ALTER TABLE `m_patients`
          ADD COLUMN `has_self_service_account` boolean NOT NULL default false COMMENT 'check if user has self service';
