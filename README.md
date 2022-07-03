# Introduction
This app exposes some REST APIs that simulate different types of bottlenecks. Using these endpoints we can test the resilience of our infrastructure and do observability hands-on by creating alarms on several metrics.

# Endpoints
Here are the details of the endpoints exposed by this service
## Health Check
- `/ or /health` - Returns the health of the API. By default, it returns 200. 
- `/health/flip` - Simulates service down situation.  Calling this endpoint an odd number of times keeps the service down, calling this endpoint an even number of times keeps the service up. When the service is down, any call that comes to the service will result in 503, Service Unavailable. When the service is up, the request goes to respective APIs and produces results accordingly.

#### Use Case
We can play with these two APIs in combination with the Amazon Application Load balancer and Autoscaling group. When the service is unavailable, the load balancer health check will fail, and the auto-scaling group will read that health check failure status and dispose that ec2 instance and launch a new one. If we use the user data in the repository, we will see that the new ec2 is registered in the SSM Fleet, sending log to Cloudwatch and also sending Disk and Memory usage to Cloudwatch.

## Cpu Stress Simulation 
- `/cpu-stress?l=1&r=300000000` - is an API that takes two parameters l(for left) and r(for right) and does the following CPU intensive calculation - 
```
        double sum = 0;
        for (int i = l; i <= r; i++) {
            sum += Math.sqrt(i) / (r - l + 1);
        }
        return sum;
```
#### Use Case
We can use any load test tool to call this API concurrently simulating multiple users and do play with ALB+ASG. In t2.nano machine l=1, r=300000000 generates a good amount of load on the CPU. We can also do a cloudwatch Alarm on Cpu usage and Get an email notification using SNS. 

## Memory Stress Simulation
- `/memory-stress` - is an API that simulates a memory leak in the application. Each call to this API leaks 1MB of memory(approx.) by default. We can step it up by providing an optional parameter in the request like this - `/memory-stress?s=3` it will leak 3MB per API call instead of 1MB.

#### Use Case 
It is well known that AWS Cloudwatch does not support memory metrics from ec2 instances by default. Combining this API with the user-data.sh available in this repo, we can not only see the memory metrics in the cloudwatch but also use them with ASG to play with ALB+ASG. 

## Slow Query Simulation
- `/slow-query` - API simulates slow SQL query situation. It calls a postgresql function that takes [2.5,3.5) seconds to return. 
#### Use Case 
This API is helpful to observe how a slow query affects users' response time and overall experience when there is a high RPS(request per second).
This is a bit advanced use case that is not associated with AWS DevOps or infra but rather with application performance. However, we can see the fact that CRUD APIs are IO Bound not CPU Bound as calling this API with concurrency puts a very small load on the CPU. You will end up noticing that despite of having enough CPU, RAM, Disk and Network resources, the response time is slow and often erroneous! 

## Log Generator:
- `/generate-log?h=1` - generates trace(h=1),debug(h=2),info(h=3),warn(h=4),error(h=5,ArrayIndexOutOfBoundsException),error(h=6,RuntimeException) logs. The api will produce exception for h∉{1,2,3,4,5,6}.
#### Use Case 
We can play with log level, for example, we can simulate that the prod cloudwatch only has info, warn and error log, but the dev cloudwatch  has trace and debug logs as well. We can crate metrics from the log when there is an exception and trigger an alarm for a certain threshold on these exceptions.

## Infrastructure information

- `/meta` API will produce the expected output if the app is running on ec2 and has correct IAM permission. Here is the information it provides - 
  - instanceId - The instance id it's running on.
  - privateIp - The private ip of the machine.
  - publicIp - The public ip of the machine.
  - region- The region of the ec2 instance.
  - availabilityZone - The AZ of the ec2 instance;
  - network - The vpc and the subnet of the ec2 instance;
#### Use Case
  We can write some canary script to ensure that ASG and ALB are handling load properly by spinning up new instances.
- `/task-info` API will produce the expected output if the app is running in ECS. #Todo - Update API details and necessary IAM permission.

# Running the APP in Local
This app depends on PostGreSQL. Here are the mandatory environment variables needed to be available during application runtime -
- DB_URL(jdbc:postgresql://[HOSTNAME]:[PORT]/[DATABASE])
- DB_USERNAME
- DB_PASSWORD 

Here are some optional environment variables we can pass -
- SERVER_PORT : We can change it, the default is 80.
- DB_POOL_SIZE : We can change it, the default is 5.

I am using Heroku to have a free PostgreSQL server, we can also use docker. Also, I am using IntelliJ IDE for development.


# Running the app in Amazon EC2 Linux:
The user-data.sh does most of the heavy lifting for deploying this in AWS. The bash script has self-explanatory comments about what it does and how it does it. However, here are some highlights on what user-data.sh does -
- Installs jq that is used to parse the database password from the SSM parameter store response.
- Installs and configures cloudwatch unified agent(amazon-cloudwatch-agent), the config file(ssm:AmazonCloudWatch-healthy) comes from SSM.
- Installs and configures SSM agent.
- Installs Java
- Downloads the latest jar file from S3.
- Run the java app

Based on that, here is the step-by-step guide to deploying the app in amazon ec2 linux2 - 
1. Change DB_URL in the application.properties or export it in the user-data.sh
2. Change DB_USERNAME in the application.properties or export it in the user-data.sh
3. Create an SSM parameter to store db password securely,  look for this line in the user-data.sh
```
export DB_PASSWORD=$(aws ssm get-parameters --names heroku-postgress-password --with-decryption --region ap-southeast-2 | jq --raw-output '.Parameters[0].Value')
```
and change it to 
```
export DB_PASSWORD=$(aws ssm get-parameters --names <YOUR_SSM_PARAMETER_NAME_FOR_DB_PASSWORD> --with-decryption --region ap-southeast-2 | jq --raw-output '.Parameters[0].Value')
```
4. Create another SSM parameter named `AmazonCloudWatch-healthy` with the following text - 
```
{
  "agent": {
    "metrics_collection_interval": 60,
    "run_as_user": "root"
  },
  "logs": {
    "logs_collected": {
      "files": {
        "collect_list": [
          {
            "file_path": "/var/log/healthy-app/healthy.log",
            "log_group_name": "healthy-unified-cw-ec2",
            "log_stream_name": "{instance_id}",
            "retention_in_days": 1
          },
          {
            "file_path": "/var/log/cloud-init.log",
            "log_group_name": "cloud-init",
            "log_stream_name": "{instance_id}",
            "retention_in_days": 1
          },
          {
            "file_path": "/var/log/cloud-init-output.log",
            "log_group_name": "cloud-init-output",
            "log_stream_name": "{instance_id}",
            "retention_in_days": 1
          }
        ]
      }
    }
  },
  "metrics": {
    "aggregation_dimensions": [
      [
        "InstanceId"
      ]
    ],
    "append_dimensions": {
      "AutoScalingGroupName": "${aws:AutoScalingGroupName}",
      "ImageId": "${aws:ImageId}",
      "InstanceId": "${aws:InstanceId}",
      "InstanceType": "${aws:InstanceType}"
    },
    "metrics_collected": {
      "collectd": {
        "metrics_aggregation_interval": 30
      },
      "disk": {
        "measurement": [
          "used_percent"
        ],
        "metrics_collection_interval": 60,
        "resources": [
          "*"
        ]
      },
      "mem": {
        "measurement": [
          "mem_used_percent"
        ],
        "metrics_collection_interval": 60
      },
      "statsd": {
        "metrics_aggregation_interval": 30,
        "metrics_collection_interval": 10,
        "service_address": ":8125"
      }
    }
  }
}
```
5. Create an S3 bucket.
6. Do a clean build (./gradlew clean build) and upload the `build/libs/healthy-0.0.1-SNAPSHOT.jar` into the s3 bucket. Then find the following line in the `user-data.sh`
```
aws s3api get-object --bucket aws-sysops-exam-practice-4689 --key healthy-0.0.1-SNAPSHOT.jar app.jar
```
and replace it with 
```
aws s3api get-object --bucket <YOUR_S3_BUCKET_NAME> --key healthy-0.0.1-SNAPSHOT.jar app.jar
```

# Necessary IAM Permissions
- `AmazonEC2RoleforSSM` : Required to talk to SSM Fleet manager.
- `AmazonS3FullAccess`	: Required to download the jar file from s3. A better approach would be to host the jar file in GitHub and consume it from there. We can remove it then.
- `AmazonEC2ReadOnlyAccess` - Required for `/meta` API to read ec2-metadata.
- `CloudWatchAgentServerPolicy`: Required to send logs from ec2 to Cloudwatch.
- `AmazonSSMManagedInstanceCore`: Required for SSM interactions.(Advanced)
- `CloudWatchFullAccess` - Not sure. Need to check by removing it. #Todo.

# K6 
K6 is my go-to load/stress testing tool. Hence, this repo includes some utility script inside the k6 directory under the root of the project directory that helps to generate load using k6.
- Installing k6 : https://k6.io/docs/getting-started/installation/
- Running k6 : https://k6.io/docs/getting-started/running-k6/

Since k6 scripts are written in javascript, it is very easy to extend and maintain them.
