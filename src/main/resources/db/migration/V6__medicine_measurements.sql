-- Create measurement units

DROP TABLE IF EXISTS `m_units` ;
CREATE TABLE IF NOT EXISTS `m_units`(
  id BIGINT NOT NULL AUTO_INCREMENT comment 'unit auto-increment id',
  `unit` varchar  (100) comment  'measure unit',
  `symbol` varchar  (20) comment 'measurement unit',
  quantity varchar  (80) comment 'quantity',
  PRIMARY KEY (`id`)
) COLLATE='utf8_unicode_ci' ENGINE=InnoDB;

ALTER TABLE `m_mdc_categories` add `unit_id` bigint after `descriptions`;
ALTER TABLE `m_mdc_categories` ADD  constraint  fk_medicine_categories_meassure FOREIGN KEY(`unit_id`) REFERENCES `m_units`(`id`) ON DELETE CASCADE;

INSERT INTO `m_units` (`id`, `unit`, `symbol`, `quantity`) VALUES
    (1, "Kilogram", "kg", "Mass(weight)" ),
    (2, "gram", "g", "Mass(weight)" ),
    (3, "milligram", "mg", "Mass(weight)" ),
    (4, "Microgram", "mcg", "Mass(weight)" ),
    (5, "litre", "L", "Volume" ),
    (6, "millilitre", "ml", "Volume" ),
    (7, "cubic centimetre", "cc", "Volume" ),
    (8, "mole", "mol", "Amount" ),
    (9, "millimole", "mmol", "Amount" );


