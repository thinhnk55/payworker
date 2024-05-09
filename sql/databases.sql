CREATE DATABASE payworker CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE USER 'payworker'@'localhost' IDENTIFIED BY 'POQM@)$(*s334sllk';
GRANT ALL ON payworker.* TO 'payworker'@'localhost';
CREATE USER 'payworker'@'%' IDENTIFIED BY 'POQM@)$(*s334sllk';
GRANT ALL ON payworker.* TO 'payworker'@'%';