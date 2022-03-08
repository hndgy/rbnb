CREATE TABLE TokenService.Token
(
    idToken INTEGER NOT NULL PRIMARY KEY,
    idCoinGecko VARCHAR(256) NULL,
    symbole VARCHAR(256) NULL,
    nom VARCHAR(256) NULL,
    urlPrix VARCHAR(64) NULL
);