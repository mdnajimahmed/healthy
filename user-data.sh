#!/bin/bash
yum update
yum install -y jq
sudo yum install -y https://s3.ap-southeast-2.amazonaws.com/amazon-ssm-ap-southeast-2/latest/linux_amd64/amazon-ssm-agent.rpm
systemctl status amazon-ssm-agent
systemctl start amazon-ssm-agent
export DB_PASSWORD=$(aws ssm get-parameters --names heroku-postgress-password --with-decryption --region ap-southeast-2 | jq --raw-output '.Parameters[0].Value')
amazon-linux-extras install -y java-openjdk11
java -version
aws s3api get-object --bucket aws-sysops-exam-practice-4689 --key healthy-0.0.1-SNAPSHOT.jar app.jar
mkdir -p /var/log/healthy-app/
java -jar app.jar &> /var/log/healthy-app/healthy.log &