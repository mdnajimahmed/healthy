#!/bin/bash

# bnd stands for build and dockerize, it creates new jar, builds docker image and publishes to the docker hub

./gradlew clean build -x test &&  \
docker-compose -f docker-compose-build.yml build && \
docker image push ivplay4689/healthy

