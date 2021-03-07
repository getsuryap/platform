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