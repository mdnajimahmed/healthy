./gradlew clean build
docker-compose -f docker-compose-build.yml build
docker push ivplay4689/healthy
docker image rm -f ivplay4689/healthy:latest 

docker container rm -f $(docker container ls -aq)

http://localhost:8080/sum?s=1&e=100000000
http://localhost:8080/query

cpu credit *100 / 60* number of vpc