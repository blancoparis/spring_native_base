#!/bin/sh
./mvnw clean spring-boot:build-image  -Pdev
docker compose -f ./infra/docker-compose.yml -f ./infra/docker-compose.dev.yml up -d
