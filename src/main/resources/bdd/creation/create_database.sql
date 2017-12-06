/* Script to create database and user for it*/

CREATE DATABASE MDM;

CREATE USER 'mdm_user'@'localhost' IDENTIFIED BY 'mdm_password';

GRANT ALL ON MDM.* TO 'mdm_user'@'localhost';