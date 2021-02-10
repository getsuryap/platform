
ALTER TABLE `m_patients`
CHANGE COLUMN `thumbnail` `thumbnail` VARCHAR(200) NULL DEFAULT NULL ;

DROP TABLE IF EXISTS `m_sms`;
CREATE TABLE IF NOT EXISTS `m_sms`(
  id BIGINT NOT NULL AUTO_INCREMENT,

  api_version varchar (20),
  body varchar (500),
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
  updated  varchar(200) ,
  sent varchar (100) ,
  direction varchar (100),
  error_code varchar (20),
  error_msg varchar (200),
  from_phone varchar (100),
  to_phone varchar (100),
  media_number varchar (100),
  segment_number varchar (100),
  price varchar (50),
  price_unit varchar (50),

  `status` varchar (50),
PRIMARY KEY (`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;


DROP TABLE IF EXISTS `m_sms_configuration`;
CREATE TABLE IF NOT EXISTS `m_sms_configuration`(
  id BIGINT NOT NULL AUTO_INCREMENT,
  name varchar (50),
  `sid` varchar (200) NOT NULL ,
  `token` varchar (200) NOT NULL ,
  `phonenumber` varchar (50) NOT  NULL ,
  `is_active` boolean default  false,
  constraint  uc_sms_configuration_name unique(`name`),
  constraint  uc_sms_configuration_sid unique( `sid`),
  constraint  uc_sms_configuration_token unique(`token`),
  constraint pk_sms_configiration primary key (`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;
