CREATE TABLE FacadeCompte.Compte
(
    id INTEGER NOT NULL PRIMARY KEY,
    idOwner VARCHAR(256) NOT NULL,
    libelleCompte VARCHAR(256) NULL,
    typeCompte VARCHAR(256) NULL
);
