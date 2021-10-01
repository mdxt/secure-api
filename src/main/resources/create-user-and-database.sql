CREATE USER 'insurance_app'@'localhost' IDENTIFIED BY 'insurance_app';

DROP SCHEMA IF EXISTS `insurance_app`;

CREATE SCHEMA `insurance_app`;

GRANT ALL PRIVILEGES ON insurance_app.* TO 'insurance_app'@'localhost';

USE `insurance_app`;