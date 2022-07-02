#!/bin/bash
yum update

# install jq to parse db password from ssm
yum install -y jq

#configure cloudwatch unified agent
yum install -y amazon-cloudwatch-agent
mkdir -p /usr/share/collectd/
touch /usr/share/collectd/types.db
/opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -s -c ssm:AmazonCloudWatch-healthy

#configure ssm agent
yum install -y https://s3.ap-southeast-2.amazonaws.com/amazon-ssm-ap-southeast-2/latest/linux_amd64/amazon-ssm-agent.rpm
systemctl start amazon-ssm-agent
systemctl status amazon-ssm-agent

#Install Java
amazon-linux-extras install -y java-openjdk11
java -version

#Download the latest jar file from S3
aws s3api get-object --bucket aws-sysops-exam-practice-4689 --key healthy-0.0.1-SNAPSHOT.jar app.jar

#Extract db password from SSM
export DB_PASSWORD=$(aws ssm get-parameters --names heroku-postgress-password --with-decryption --region ap-southeast-2 | jq --raw-output '.Parameters[0].Value')

#Run the Java app
mkdir -p /var/log/healthy-app/
java -jar app.jar &> /var/log/healthy-app/healthy.log &

# Kill java and re-deploy
# kill $(ps aux | grep "java" | grep -v 'grep' | awk '{print $2}')