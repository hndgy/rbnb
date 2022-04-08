GRANT ALL PRIVILEGES ON *.* TO 'user'
WITH
GRANT OPTION;

flush privileges;


CREATE TABLE UtilisateurService.Utilisateurs
(
    Id VARCHAR(100) NOT NULL PRIMARY KEY,
    Nom VARCHAR(50) NULL,
    Prenom VARCHAR(50) NULL,
    Mail VARCHAR(100) NULL,
    UserName VARCHAR(50) NULL,
    PhotoUrl VARCHAR(255) NULL,
    Bio VARCHAR(255) NULL
);