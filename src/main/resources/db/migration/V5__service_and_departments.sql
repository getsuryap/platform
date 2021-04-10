ALTER TABLE `m_consultations` add `is_admitted` boolean  default false  after `is_active`;

DROP TABLE IF EXISTS `m_department`;
CREATE TABLE `m_department` (
  `id` BIGINT NOT NULL AUTO_INCREMENT comment 'unit auto-increment id',
  `parent_id` BIGINT  comment 'office parent id',
  `hierarchy` varchar(100)  comment 'office number in hierachy',
  `extra_id` varchar(100)  comment 'separate office id',
  `name` varchar(50) NOT NULL comment 'office name',
  `descriptions` varchar (300) comment 'descriptions',
  `opening_date` TIMESTAMP NOT NULL comment 'date of starting',
  constraint `uc_department_name` UNIQUE (`name`),
  KEY `FK2291C477E2551DCC` (`parent_id`),
  CONSTRAINT `FK2291C477E2551DCC` FOREIGN KEY (`parent_id`) REFERENCES `m_department` (`id`)  ON DELETE CASCADE ,
  PRIMARY KEY (`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;
ALTER TABLE `m_staff` add `department_id` bigint after `user_id`;
ALTER TABLE `m_staff` ADD  constraint  fk_staff_department FOREIGN KEY(`department_id`) REFERENCES `m_department`(`id`) ;

INSERT INTO `m_department` VALUES
  (1, NULL, NULL,"MAIN", "Highest Board","Highest Board",  CURDATE()),
  (2, 1,NULL, "Administration", "Administration Board","Administration Board", CURDATE()),
  (3, 2,NULL, "Information", "Information Service", "Information Service", CURDATE()),
  (4, 2,NULL, "Therapeutic", "Therapeutic Service", "Therapeutic Service", CURDATE()),
  (5, 2,NULL, "Diagnostic", "Diagnostic Service","Diagnostic Service", CURDATE()),
  (6, 2,NULL, "Support", "Support Service","Support Service", CURDATE());