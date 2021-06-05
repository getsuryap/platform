-- Create insurance informations record table.
-- Keep tranck of supported insurance companies
-- Keep track of patient insurances and cards
-- 


drop table if exists `m_insurances`;
create table `m_insurances`(
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `po_box` varchar (50) not null ,
  `location` varchar (200),
  `tel_no` varchar (20) not null ,
  `email_address` varchar (250) default null,
  constraint  uc_insurance_company unique(`name`,`tel_no`,`email_address`),
  PRIMARY KEY (`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

drop table if EXISTS `m_client_insurances`;
create table `m_client_insurances`(
  `id` bigint NOT NULL AUTO_INCREMENT,
  `patient_name` varchar(200) NOT NULL,
  `membership_no` varchar(200) NOT NULL,
  `sex` varchar (10),
  `vote_no` varchar (20),
  `dob` timestamp not null comment 'date of birth',
  `issued_date` timestamp not null comment 'insurance start date',
  `expire_date` timestamp not null comment 'insurance expire date ',
  `code_no` varchar (20),
  is_active BOOLEAN NOT NULL DEFAULT true,
  `insurance_id` bigint references `m_insurances`(`id`) ON DELETE CASCADE ,
  `patient_id` bigint  references `m_patients`(`id`) ON DELETE NO ACTION,
  constraint  uc_membership_number unique(`insurance_id` ,`membership_no`),
   primary key (`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

replace into `m_privilege` ( `name`) VALUES
("CREATE_INSURANCE"),("UPDATE_INSURANCE"),("DELETE_INSURANCE"),("READ_INSURANCE"),
("CREATE_INSURANCE_COMPANY"),("UPDATE_INSURANCE_COMPANY"),("DELETE_INSURANCE_COMPANY"),("READ_INSURANCE_COMPANY");

