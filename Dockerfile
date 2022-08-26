FROM openjdk:8-jdk-alpine
ARG COMMIT="MISSING"
RUN apk add curl
EXPOSE 80
COPY ./build/libs/healthy-0.0.1-SNAPSHOT.jar ./app.jar
RUN hostname >> meta.txt
RUN date >> meta.txt

ENV COMMIT_SHA_6=${COMMIT}
# HEALTHCHECK --interval=5s --timeout=1s --start-period=5s --retries=3 CMD [ "executable" ]
ENTRYPOINT ["java","-Dserver.port=80","-jar","app.jar"]
