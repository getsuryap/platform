-- Define medical service types
-- Create medical services database table

DROP TABLE IF EXISTS `m_service_types`;
CREATE TABLE IF NOT EXISTS `m_service_types`(
  `id` BIGINT NOT NULL AUTO_INCREMENT comment 'auto-increment id',
  `name`  varchar(100) not null comment 'service name',
  `description` varchar(200) comment 'service description',
   primary key (`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

insert into  `m_service_types` values (1, 'Laboratory','For laboratory'), (2, 'Laboratory','For laboratory');

alter table `m_services`
              add column `is_measurable` boolean default '0' comment 'is service measurable',
              add column `units` varchar(6) after `enabled`,
             add column `service_type_id` bigint  references `m_service_types`(`id`) ON DELETE NO ACTION,
              add constraint fk_medical_service_service_type_id FOREIGN KEY(`service_type_id`) REFERENCES `m_service_types`(`id`);