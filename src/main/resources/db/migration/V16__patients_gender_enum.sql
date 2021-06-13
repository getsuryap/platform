-- Change Patients Email column size to 254
-- Reference: https://stackoverflow.com/questions/1199190/what-is-the-optimal-length-for-an-email-address-in-a-database

ALTER TABLE `m_patients` MODIFY `gender` ENUM('Male', 'Female', 'Unspecified') NOT NULL;

-- Fixing existing data from lower case to camelcase
UPDATE `m_patients` SET  `gender`='Female' WHERE `gender`='female';
UPDATE `m_patients` SET  `gender`='Male' WHERE `gender`='male';
UPDATE `m_patients` SET  `gender`='Unspecified' WHERE `gender`='unspecified';
