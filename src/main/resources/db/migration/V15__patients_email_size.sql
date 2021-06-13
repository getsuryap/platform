-- Change Patients Email column size to 254
-- Reference: https://stackoverflow.com/questions/1199190/what-is-the-optimal-length-for-an-email-address-in-a-database

ALTER TABLE `m_patients` MODIFY `email_address` VARCHAR (254);