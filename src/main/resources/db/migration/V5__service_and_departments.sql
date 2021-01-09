ALTER TABLE `m_service` add `is_admitted` boolean  default false  after `is_active`;

DROP TABLE IF EXISTS `m_department`;
CREATE TABLE `m_department` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT DEFAULT NULL,
  `hierarchy` varchar(100) DEFAULT NULL,
  `extra_id` varchar(100) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `opening_date` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_org` (`name`),
  UNIQUE KEY `extra_id_org` (`extra_id`),
  KEY `FK2291C477E2551DCC` (`parent_id`),
  CONSTRAINT `FK2291C477E2551DCC` FOREIGN KEY (`parent_id`) REFERENCES `m_department` (`id`)
) COLLATE='utf8_general_ci' ENGINE=InnoDB;