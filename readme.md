# Procédure d'installation
- Lancer toutes les bases de données et les dépendances aux services avec `docker compose up` 
- Lancer `./mvn_clean_install` pour générer les .jar des services en Spring
- Puis lancer `docker compose up --build` pour builder les tous les services