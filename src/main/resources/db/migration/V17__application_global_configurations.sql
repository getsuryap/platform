-- Create application global configurations for application setup
-- Reference: https://github.com/ospic/platform/discussions/565#discussioncomment-868351

drop table if EXISTS `m_configurations`;
create table `m_configurations`(
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(250) NOT NULL,
  `code` varchar(250) NOT NULL,
  `value` bigint NOT NULL DEFAULT 0,
  `active` BOOLEAN NOT NULL DEFAULT true,
  primary key (`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

insert into `m_configurations`(`name`,`code`,`value`,`active`) values ("Timely based consultation","code.value.timely.based.consultation",0,false);
replace into `m_privilege` (`name`) VALUES ("UPDATE_CONFIGURATION"),("READ_CONFIGURATION");

