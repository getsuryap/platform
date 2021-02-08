-- Create measurement units

DROP TABLE IF EXISTS `m_units` ;
CREATE TABLE IF NOT EXISTS `m_units`(
  id BIGINT NOT NULL AUTO_INCREMENT,
  `unit` varchar  (100),
  `symbol` varchar  (20),
  quantity varchar  (80),
  PRIMARY KEY (`id`)
) COLLATE='utf8mb4_general_ci' ENGINE=InnoDB;

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


