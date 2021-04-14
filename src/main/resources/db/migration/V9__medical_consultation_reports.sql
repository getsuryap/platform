-- Define Medical laboratory services

ALTER TABLE `m_staff`
      ADD COLUMN `is_active` boolean NOT NULL default true COMMENT 'check if this staff is active',
      ADD COLUMN `is_available` boolean NOT NULL default true COMMENT 'check if this staff is available';




ALTER TABLE `users`
        ADD COLUMN `is_self_service` boolean NOT NULL default false COMMENT 'check if user is self service',
        ADD COLUMN `patient_id` BIGINT NULL COMMENT 'self service patient account',
        ADD CONSTRAINT `fk_patient_self_service_account` FOREIGN KEY (`patient_id`) REFERENCES `m_patients`(`id`) ON DELETE CASCADE;

ALTER TABLE `m_patients`
          ADD COLUMN `has_self_service_account` boolean NOT NULL default false COMMENT 'check if user has self service';
