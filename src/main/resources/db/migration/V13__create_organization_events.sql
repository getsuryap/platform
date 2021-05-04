-- Create organization calendar events
--

DROP TABLE IF EXISTS `m_calendar`;
CREATE TABLE `m_calendar` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `timed` tinyint(1) NOT NULL DEFAULT '0',
  `start` timestamp NULL DEFAULT NULL,
  `end` timestamp NULL DEFAULT NULL,
  `department` int(11) DEFAULT '0',
  `created_date` varchar(100) DEFAULT NULL,
  `created_by` varchar(200) DEFAULT NULL,
  `last_modified_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_modified_by` varchar(200) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;