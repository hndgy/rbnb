docker run --name keycloak -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -p 8080:8080   quay.io/keycloak/keycloak:17.0.0 start-dev

docker build . -f DockerFile -t utilisateur-service

docker compose  -f "docker-compose.yml" up --build keycloak

docker exec utilisateur-service-keycloak-1 /opt/jboss/keycloak/bin/add-user-keycloak.sh -u admin -p admin

docker compose down;docker compose -f docker-compose.yml up --build 