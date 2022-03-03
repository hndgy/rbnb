CREATE USER 'user'@'172.17.0.1' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'user'@'172.17.0.1'
WITH
GRANT OPTION;

-- Create a new database called 'UtilisateurService'
-- Connect to the 'master' database to run this snippet
USE master
GO
-- Create the new database if it does not exist already
IF NOT EXISTS (
    SELECT name
FROM sys.databases
WHERE name = N'UtilisateurService'
)
CREATE DATABASE UtilisateurService
GO


-- Create a new table called 'Utilisateurs' in schema 'SchemaName'
-- Drop the table if it already exists
IF OBJECT_ID('SchemaName.Utilisateurs', 'U') IS NOT NULL
DROP TABLE UtilisateurService.Utilisateurs
GO
-- Create the table in the specified schema
CREATE TABLE UtilisateurService.Utilisateurs
(
    Id VARCHAR(100) NOT NULL PRIMARY KEY,
    -- primary key column
    Nom VARCHAR(50) NULL,
    Prenom VARCHAR(50) NULL,
    Mail VARCHAR(100) NULL
    -- specify more columns here
);
GO