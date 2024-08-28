# apache_plane_tracker

A simple plane tracker data project built with Apache stack tools

## Kubernetes + Kafka basic setup


https://dzone.com/articles/how-to-deploy-apache-kafka-with-kubernetes

Install the following dependencies based on your distribution:
* minikube
* kubectl (may come with minikube)
* docker (or other container solution)
* kafkacat
* [helm](https://helm.sh/docs/intro/install/)

## Stack lifecycle

Before starting the stack, please copy the *.envrc.tpl* to a file named *.envrc*:
```sh
cp .envrc.tpl .envrc
```

Then, edit the environment variables defined in the *.envrc*:
- IS_DRY_RUN: set it to **true** if you do not want the cronJobs to publish to Kafka
- KAFKA_BROKER: default to kafka-service:9092 (our stack kafka broker service)
- KAFKA_TOPIC: kafka topic for openSky cronJob to publish aircraft states
- LATITUDE_MIN / LATITUDE_MAX / LONGITUDE_MIN / LONGITUDE_MAX: coordinates representing the zone to scan for aricrafts

The stack is meant to be run/stopped via the Makefile

```sh
# start minikube + the full k8s stack
make start

# deploy changes to the k8s stack, without restarting minikube
make apply

# reset to a clean k8s state without this stack
make teardown

# stop minikube
make stop

```

Once the stack is started and the service kafka is Running, you can test it via kafkacat commands.

We set kafka up in a way that it is reacheable on $(minicube ip):30092

```sh
# Terminal 1 - Producer
echo "hello world!" | kafkacat -P -b $(minikube ip):30092 -t test
```

```sh
# Terminal 2 - Consumer
kafkacat -C -b $(minikube ip):30092 -t test
kafkacat -C -b $(minikube ip):30092 -t aircraft-states # To read the aircraft-states published by opensky-kafka-publisher-cronjob
```
## Dependencies

### opensky-kafka-publisher
The code to periodically fech data from opensky API is in my [opensky-kafka-publisher](https://github.com/antoineBonninProjects/opensky-kafka-publisher) repo.

The docker image containing this Java app is [the following](https://hub.docker.com/repository/docker/abonnin33/opensky-kafka-publisher/general).
Having a container image is key to create a Kubernetes CronJob.

### aircraft-stream-analytics
The code of my spark Streaming job to compute simple aircraft stats and alerts is in my [opensky-kafka-publisher](https://github.com/antoineBonninProjects/opensky-kafka-publisher) repo. It consumes data produced by *opensky-kafka-publisher* on kafka.

The docker image containing this Scala job is [the following](https://hub.docker.com/repository/docker/abonnin33/aircraft-stream-analytics/general).
Having a container image is key to create a Kubernetes SparkApplication.

# Next steps

* create the streaming spark job that consumes kafka and pushes into HDFS
* add logic inside the streaming spark job to compute the distance from previous state for each plane in this area
* send an alert if plane have not emitted a state for more than 1 hour