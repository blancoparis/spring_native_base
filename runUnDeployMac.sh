#!/bin/sh
docker compose -f ./infra/docker-compose.yml -f ./infra/docker-compose.dev.yml down
