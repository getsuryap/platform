-- Populate built in reports
-- create medical reports database table

DROP TABLE IF EXISTS `m_reports`;
CREATE TABLE `m_reports`(
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'report id',
  name VARCHAR (50) NOT  NULL COMMENT 'report name',
  filename VARCHAR (50) NOT  NULL COMMENT 'fiport file name',
  entity varchar(100) NOT NULL comment 'report targeted entitty',
  descriptions VARCHAR (250) NULL COMMENT 'extra descriptions',
  `type` BIGINT  NOT NULL COMMENT 'report type',
  primary key (`id`),
unique key(`name`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

replace into `m_reports` (`id`,`name`, `filename`,`entity`, `descriptions`, `type`) VALUES
(1, "All patients", "patient_list","client","List of all registered patients", 1),
(2, "Admissions ","admissions_list","admissions", "List all available admissions", 1),
(3, "Transactions", "transactions_list","transactions", "List of all organization service transaactions", 2),
(4, "Service bills","service_bill_list","bills", "List all available  service bills", 2),
(5, "Consultations","consultations_list","consultations", "List all consultations", 1),
(6, "Wards list","wards_list","wards", "List of wards and number of beds", 1),
(7, "Blood bank list","blood_bank_list","bloods", "Blood bank quntities report", 1),
(8, "Medical services","medicalservices_list","services", "List of medical services provided", 1),
(9, "Medicines","medicines_list","medicines", "List of all medicines", 1);

