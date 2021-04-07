-- Define self service privileges
-- Create new role privileges
-- Create new role for self-service
ALTER TABLE `users`add constraint unique_username_email unique (`id`,`username`,`email`);
INSERT INTO `m_roles` ( `name`) VALUES ("SELF SERVICE");


SET @roleId = (SELECT `id` FROM `m_roles` WHERE `name` = "SELF SERVICE");
INSERT INTO `role_privileges` (`role_id`,`privilege_id`) VALUES
      (@roleId, 19),(1,20);

REPLACE INTO `m_privilege` ( `name`) VALUES
      ("CREATE_SELF_SERVICE"), ("READ_SELF_SERVICE"), ("UPDATE_SELF_SERVICE"), ("DELETE_SELF_SERVICE");


DROP TABLE IF EXISTS `m_files`;
CREATE TABLE `m_files`(
            id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'file index id',
            name VARCHAR (200) NOT  NULL COMMENT 'file name',
            url VARCHAR (250) NULL COMMENT 'file http url',
            `location` VARCHAR (250) NULL COMMENT 'file location',
            `type`  VARCHAR (250) NOT NULL COMMENT 'file type',
            `size`  VARCHAR (250) NOT NULL COMMENT 'file size',
            primary key (`id`),
            `consultation_id` BIGINT REFERENCES `m_consultations`(`id`) ON DELETE CASCADE,
            constraint uc_name_consultation_url unique(`name`, `url`,`consultation_id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

ALTER TABLE `m_patients` change column `last_modified_date` `last_modified_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE `m_patients` change column  `symptoms` `allergies` VARCHAR (250);