CREATE DATABASE IF NOT EXISTS unideal;

CREATE USER IF NOT EXISTS 'unideal'@'%' IDENTIFIED BY 'unideal';

GRANT ALL PRIVILEGES ON unideal.* TO 'unideal'@'%' WITH GRANT OPTION;

FLUSH PRIVILEGES;

USE unideal;

INSERT INTO category (name) VALUES ('book');
INSERT INTO category (name) VALUES ('clothes');
INSERT INTO category (name) VALUES ('electronics');
INSERT INTO category (name) VALUES ('etc');