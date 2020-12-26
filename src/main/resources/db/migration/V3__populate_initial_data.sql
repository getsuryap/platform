
REPLACE INTO `m_blood_bank` (`id`, `bags_count`, `blood_group`)
VALUES (1, 0,"A+"),(2, 0,"A-"),(3, 0,"B+"),(4, 0,"B-"),(5, 0,"O+"),
(6, 0,"O-"),(7, 0,"AB+"),(8, 0,"AB-");

REPLACE INTO `m_wards` (`id`, `name`) VALUES
            (1,"WD01"),(2,"WD02"),(3,"WD03"),
            (4,"WD05"),(1,"WD01"),(1,"WD06");
REPLACE INTO `m_beds` (`id`, `identifier`, `is_occupied`,`ward_id`) VALUES
          (1, "BD",0,1),(2, "BD",0,1),(3, "BD",0,1),(4, "BD",0,1),
          (5, "BD",0,1),(6, "BD",0,1);

INSERT INTO `users` (`id`, `username`, `email`, `password`, `isStaff`) VALUES
(2, 'demo', 'demo@email.com', '$2a$10$VFeJvZC7YbXtMFSuz6ILSO2eAYv2va3jxTWJUKJ/ERQNmdgFYla/W', 1),
(3, 'moderator', 'moderator@ospic.com', '$2a$10$FpclmzPw4eU4jMsoTjqz0uc6X47NyjWPrWj.jRNgan9R7AocmEOPy', 1),
(4, 'auditor', 'auditor@ospic.com', '$2a$10$.rw20APlcm.rxRS69g0nOOioxXyUvRQOY0n4wY11L4iOwS.Pm09UC', 1),
(5, 'doctor', 'doctor@ospic.com', '$2a$10$Wi8JhUxvAcBlP/LlmoX65OEPHtMhuKMOehdQJo5VPk8ljHUrJA4oS', 1),
(6, 'nurse', 'nurse@ospic.com', '$2a$10$5Av5ILrCmCtaNWXfRzbQZ.zX4u.izvNC.9t4u2bSgFqWTak7sqwvq', 1);


REPLACE INTO `m_patients` (`id`, `name`, `gender`, `address`, `guardian_name`, `phone`, `email_address`, `height`, `weight`, `blood_pressure`, `blood_group`, `age`, `symptoms`, `note`, `marital_status`, `isAdmitted`, `is_active`, `thumbnail`, `created_by`, `last_modified_date`, `last_modified_by`) VALUES
(1, ' Weston Washington', 'female', '123 Hawaii, 31ST, H32KL', 'John Doe', '2193801221312', 'email@example.com', '10KG', '10KG', 'Pa34', 'AB', 34, 'none', 'note', 'Single', 0, 0, NULL, NULL, '2020-12-26 15:19:57', NULL),
(2, 'Vijay Suresh', 'female', '123 Hawaii, 31ST, H32KL', 'John Doe', '2193801221312', 'email@example.com', '10FT', '10KG', 'Pa34', 'AB', 34, 'none', 'note', 'Single', 0, 0, NULL, NULL, '2020-12-26 15:07:36', NULL),
(3, 'Drew Lowe', 'female', '123 Hawaii, 31ST, H32KL', 'John Doe', '2193801221312', 'email@example.com', '10KG', '10KG', 'Pa3', 'AB', 34, 'none', 'note', 'Single', 0, 0, NULL, NULL, '2020-12-26 15:17:15', NULL),
(4, 'Bryce Armstrong', 'female', '123 Hawaii, 31ST, H32KL', 'John Doe', '2193801221312', 'email@example.com', '10KG', '10KG', 'Pa34', 'AB', 34, 'none', 'note', 'Single', 0, 0, NULL, NULL, '2020-12-26 15:33:32', NULL),
(5, 'Meredith Perkins', 'female', '123 Hawaii, 31ST, H32KL', 'John Doe', '2193801221312', 'email@example.com', '10KG', '10KG', 'Pa34', 'AB', 34, 'none', 'note', 'Single', 0, 0, NULL, NULL, '2020-12-26 15:18:50', NULL),
(6, 'Willie Curtis', 'female', '123 Hawaii, 31ST, H32KL', 'John Doe', '2193801221312', 'email@example.com', '10KG', '10KG', 'Pa34', 'AB', 34, 'none', 'note', 'Single', 0, 0, NULL, NULL, '2020-12-26 15:09:56', NULL),
(7, 'Lori Henderson', 'female', '123 Hawaii, 31ST, H32KL', 'John Doe', '2193801221312', 'email@example.com', '10KG', '10KG', 'Pa34', 'AB', 34, 'none', 'note', 'Single', 0, 0, NULL, NULL, '2020-12-26 15:09:01', NULL),
(8, 'Callie Marshall', 'female', '123 Hawaii, 31ST, H32KL', 'John Doe', '2193801221312', 'email@example.com', '10KG', '10KG', 'Pa34', 'AB', 34, 'none', 'note', 'Single', 0, 0, NULL, NULL, '2020-12-26 15:39:01', NULL);


REPLACE INTO `m_staff` (`id`, `username`, `fullName`, `contacts`, `image_url`, `doc_type`, `email`, `user_id`) VALUES
(1, 'demo', 'Giselle Vargas', '213021932', 'https://picsum.photos/200/100', NULL, 'demo@email.com', 2),
(2, 'moderator', 'Selena Espinoza', '4293293912', 'https://picsum.photos/200/200', NULL, 'moderator@ospic.com', 3),
(3, 'auditor', 'Harry Farmer', '8936291', 'https://picsum.photos/200/300', NULL, 'auditor@ospic.com', 4),
(4, 'doctor', 'Dale Perez', '213021932', 'https://picsum.photos/200/400', NULL, 'doctor@ospic.com', 5),
(5, 'nurse', 'Evan Aguilar', '09173936', 'https://picsum.photos/200/500', NULL, 'nurse@ospic.com', 6);