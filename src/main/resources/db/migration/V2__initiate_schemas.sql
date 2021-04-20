-- Create @users database table
DROP TABLE IF EXISTS  `users`;
CREATE TABLE IF NOT EXISTS  `users` (
  id BIGINT(20) NOT NULL PRIMARY KEY  AUTO_INCREMENT, username VARCHAR (200) NOT NULL,
  email VARCHAR(30) NOT NULL, password VARCHAR (250) NOT NULL, isStaff BOOLEAN NOT NULL DEFAULT 0 ) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

-- Create ROLES tables
DROP TABLE IF EXISTS  `m_roles`;
CREATE TABLE IF NOT EXISTS  `m_roles` (
  id BIGINT  NOT NULL AUTO_INCREMENT,
  name VARCHAR (20) NOT NULL,
  constraint  uc_role_name unique(`name`),
  constraint roles_pk primary key (`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;



DROP TABLE IF EXISTS  `m_privilege`;
CREATE TABLE IF NOT EXISTS  `m_privilege` (
  id BIGINT  NOT NULL AUTO_INCREMENT,
  name VARCHAR (250) NOT NULL,
  constraint unique_constraint_privilege unique (`name`),
  constraint privilege_primary_key primary key (`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;



DROP TABLE IF EXISTS `m_blood_bank`;
CREATE TABLE IF NOT EXISTS `m_blood_bank`(
  id BIGINT NOT NULL AUTO_INCREMENT,
  blood_group VARCHAR (20) NOT NULL,
  bags_count BIGINT DEFAULT 0,PRIMARY KEY (`id`) ) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

DROP TABLE IF EXISTS `m_mdc_groups`;
CREATE TABLE IF NOT EXISTS `m_mdc_groups`(
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR (200) NOT NULL ,
  descriptions VARCHAR (250) NOT NULL,
  CONSTRAINT  uc_medicine_group_name UNIQUE(`name`),
  PRIMARY KEY (`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;


DROP TABLE IF EXISTS `m_mdc_categories`;
CREATE TABLE IF NOT EXISTS `m_mdc_categories`(
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR (200) UNIQUE NOT NULL ,
  descriptions VARCHAR (350) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT uc_medicine_category_name UNIQUE(`name`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

DROP TABLE IF EXISTS `m_medicines`;
CREATE TABLE IF NOT EXISTS `m_medicines`(
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR (200) NOT NULL,
  `generic_name` varchar (200) null ,
  company varchar (200) NOT NULL,
  compositions varchar(250) NOT NULL,
  units varchar (50) NOT NULL,
  quantity int NOT NULL,
  `expire_date` TIMESTAMP NULL,
  `effects` varchar (250) null ,
  `buying_price`  DECIMAL(19,2)  not null,
  `selling_price`  DECIMAL(19,2) not null,
  `store_box` varchar (10),
  constraint medicine_pk primary key  (`id`),
  `group_id` bigint references `m_mdc_groups`(`id`),
  category_id BIGINT REFERENCES `m_mdc_categories`(`id`),
  CONSTRAINT uc_name_company_group_category UNIQUE(`name`,`company`, `group_id`, `category_id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;


DROP TABLE IF EXISTS `m_admissions`;
CREATE TABLE IF NOT EXISTS `m_admissions`(
  id BIGINT NOT NULL AUTO_INCREMENT,
  is_active BOOLEAN NOT NULL DEFAULT true ,
  start_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `end_date` TIMESTAMP NULL  ,
  PRIMARY KEY (`id`),
`cid` BIGINT REFERENCES `m_consultations`(`id`) ON DELETE CASCADE
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;


DROP TABLE IF EXISTS `m_visits`;
CREATE TABLE IF NOT EXISTS `m_visits`(
  id BIGINT NOT NULL AUTO_INCREMENT,
  symptoms VARCHAR (550) NOT NULL ,
  `date_time` TIMESTAMP,
  PRIMARY KEY (`id`),
  admission_id BIGINT REFERENCES `m_admissions`(`id`) ON DELETE CASCADE
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

DROP TABLE IF EXISTS `m_wards`;
CREATE TABLE IF NOT EXISTS `m_wards`(
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR (20) NOT NULL,
  PRIMARY KEY (`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;


DROP TABLE IF EXISTS `m_beds`;
CREATE TABLE IF NOT EXISTS `m_beds`(
  id BIGINT NOT NULL AUTO_INCREMENT,
  identifier varchar  (20) ,
  is_occupied BOOLEAN NOT NULL DEFAULT false ,
  ward_id  bigint not null,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`ward_id`) references `m_wards`(`id`) ON DELETE CASCADE
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;


DROP TABLE IF EXISTS `m_patients` ;
CREATE TABLE IF NOT EXISTS `m_patients`(
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR (100) NOT NULL ,
  gender ENUM('male', 'female', 'unspecified') NOT NULL ,
  address VARCHAR (200) NOT NULL ,
  guardian_name VARCHAR (100),
  phone VARCHAR (15) NOT NULL ,
  email_address VARCHAR (20) NOT NULL ,
  height VARCHAR (20) ,
  weight VARCHAR (20),
  blood_pressure VARCHAR (20),
  blood_group VARCHAR (20),
  age INT NOT NULL ,
  symptoms VARCHAR (550),
  note VARCHAR (20),
  marital_status VARCHAR (20),
  isAdmitted BOOLEAN NOT NULL DEFAULT false,
  is_active BOOLEAN NOT NULL DEFAULT false,
  thumbnail VARCHAR (20) ,
  created_date varchar (100),
  created_by VARCHAR (200),
  last_modified_date TIMESTAMP  NOT NULL,
  last_modified_by VARCHAR (200),
  PRIMARY KEY (`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

DROP TABLE IF EXISTS `m_consultations` ;
CREATE TABLE IF NOT EXISTS `m_consultations`(
  id BIGINT NOT NULL AUTO_INCREMENT,
  fromdate TIMESTAMP  NOT NULL,
  todate TIMESTAMP NULL,
  is_active BOOLEAN NOT NULL DEFAULT true ,
  constraint consultations_pk primary key  (`id`),
  staff_id BIGINT references `m_staff`(`id`) ,
  patient_id BIGINT  references `m_patients`(`id`)  ON DELETE CASCADE
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;


DROP TABLE IF EXISTS `m_contacts` ;
CREATE TABLE IF NOT EXISTS `m_contacts`(
  id BIGINT NOT NULL AUTO_INCREMENT,
  is_active BOOLEAN NOT NULL DEFAULT true ,
  email_address VARCHAR (50) NOT NULL,
  zipcode VARCHAR (20),
  city VARCHAR (20),
  state VARCHAR (100) NOT NULL ,
  physical_address VARCHAR (200) NOT NULL ,
  home_phone VARCHAR (20) NOT NULL ,
  work_phone VARCHAR (20),
  patient_id BIGINT,
  CONSTRAINT contact_pk primary key (`id`),
  constraint patient_contact_fk  foreign key (`patient_id`) references `m_patients`(`id`) ON DELETE CASCADE,
  constraint contact_unique_key unique (`id`,`patient_id`),
  constraint patient_contact_unique_key unique(`patient_id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

DROP TABLE IF EXISTS `m_staff` ;
CREATE TABLE IF NOT EXISTS `m_staff`(
  id BIGINT NOT NULL  AUTO_INCREMENT,
  username VARCHAR (20) NOT NULL ,
  fullName VARCHAR (100) ,
  contacts VARCHAR (10) ,
  image_url VARCHAR (200),
  doc_type VARCHAR (10)  ,
  email VARCHAR (30) NOT NULL ,
  user_id BIGINT NOT NULL ,
  constraint staff_pk primary key  (`id`),
  constraint staff_user_fk foreign key (`user_id`) references `users`(`id`) ON DELETE CASCADE,
  constraint staff_unique_key unique(`id`,`user_id`),
  constraint user_staff_unique_key unique(`user_id`)
  ) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

DROP TABLE IF EXISTS `m_diagnoses`;
CREATE TABLE IF NOT EXISTS `m_diagnoses`(
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `date`  TIMESTAMP ,
  `symptoms` VARCHAR (500) NOT NULL,
  PRIMARY KEY (`id`),
  `cid` BIGINT REFERENCES `m_consultations`(`id`) ON DELETE CASCADE

) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;


DROP TABLE IF EXISTS  `admission_bed`;
CREATE TABLE IF NOT EXISTS  `admission_bed`(
  admission_id BIGINT  NOT NULL, bed_id BIGINT  NOT NULL,
  CONSTRAINT `FK_admissions_admission_id` FOREIGN KEY (admission_id) REFERENCES `m_admissions`(id) ON DELETE CASCADE,
  CONSTRAINT `FK_admissions_bed_id` FOREIGN KEY (bed_id) REFERENCES `m_beds`(id) ON DELETE CASCADE
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;


DROP TABLE IF EXISTS  `user_roles`;
CREATE TABLE IF NOT EXISTS  `user_roles`(
  user_id BIGINT  NOT NULL, role_id BIGINT  NOT NULL,
  CONSTRAINT `FK_user_roles` FOREIGN KEY (user_id) REFERENCES `users`(id) ON DELETE CASCADE,
  CONSTRAINT `FK_role_roles` FOREIGN KEY (role_id) REFERENCES `m_roles`(id) ON DELETE CASCADE
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;


DROP TABLE IF EXISTS `role_privileges`;
CREATE TABLE IF NOT EXISTS `role_privileges`(
  role_id BIGINT NOT NULL , privilege_id BIGINT ,
  CONSTRAINT `FK_role_privileges` FOREIGN KEY (role_id) REFERENCES `m_roles`(id) ON DELETE CASCADE,
  CONSTRAINT `FK_privilege_roles` FOREIGN KEY (privilege_id) REFERENCES `m_privilege`(id) ON DELETE CASCADE
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;


-- Create relational constraints between tables
ALTER TABLE `m_visits` ADD CONSTRAINT FK_admission_visits FOREIGN KEY(`admission_id`) REFERENCES `m_admissions`(`id`) ON DELETE CASCADE;
ALTER TABLE `m_medicines` ADD CONSTRAINT FK_medicine_categories FOREIGN KEY(`category_id`) REFERENCES `m_mdc_categories`(`id`);
ALTER TABLE `m_medicines` ADD CONSTRAINT FK_medicine_groups FOREIGN KEY(`group_id`) REFERENCES `m_mdc_groups`(`id`);
ALTER TABLE `m_diagnoses` ADD CONSTRAINT FK_consultations_diagnoes FOREIGN KEY(`cid`) REFERENCES `m_consultations`(`id`) ON DELETE CASCADE;
ALTER TABLE `m_beds` ADD CONSTRAINT FK_bed_ward FOREIGN KEY(`ward_id`) REFERENCES `m_wards`(`id`) ON DELETE CASCADE;
ALTER TABLE `m_admissions` ADD CONSTRAINT FK_consultations_admissions FOREIGN KEY(`cid`) REFERENCES `m_consultations`(`id`) ON DELETE CASCADE;

-- ALTER TABLE m_consultations
     --   ADD constraint  fk_patient_consultations_resource foreign key (`patient_id`) references `m_patients`(`id`),
   --     ADD constraint  fk_staff_consultations_resource foreign key(`staff_id`) references `m_staff`(`id`);

-- ALTER TABLE `m_consultations` ADD CONSTRAINT FK_patient_consultations_resource FOREIGN KEY(`patient_id`) REFERENCES `m_patients`(`id`) ON UPDATE CASCADE;
-- ALTER TABLE `m_consultations` ADD CONSTRAINT FK_staff_patient_consultations_resource FOREIGN KEY(`staff_id`) REFERENCES `m_staff`(`id`) ON UPDATE CASCADE;