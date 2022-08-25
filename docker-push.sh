tag=$(echo $(git rev-parse HEAD) | cut -c1-6)
./gradlew clean build -x test && docker image build . -t ivplay4689/healthy:$tag && docker push ivplay4689/healthy:$tag
