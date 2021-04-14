-- Populate built in reports
-- create medical reports database table
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

replace into `m_reports` (`name`, `filename`, `descriptions`, `type`) VALUES
("All patients", "patient_list", "List of all registered patients", 1),
("Admissions ","admissions_list", "List all available admissions", 1),
("Transactions", "transactions_list", "List of all organization service transaactions", 1);