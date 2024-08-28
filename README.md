# apache_plane_tracker

A simple plane tracker data project built with Apache stack tools

## Kubernetes + Kafka basic setup


https://dzone.com/articles/how-to-deploy-apache-kafka-with-kubernetes

Install the following dependencies based on your distribution:
* minikube
* kubectl (may come with minikube)
* docker (or other container solution)
* kafkacat

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
# start the stack
make start

# stop the stack
make stop

# teardown if you want to reset to a clean k8y state without this project
make teardown
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
```
## Dependencies

### opensky-puller
The code to periodically fech data from opensky API is in [another repo of mine](https://github.com/antoineBonninProjects/opensky-puller)

The docker image containing this Java app is [the following](https://hub.docker.com/repository/docker/abonnin33/opensky-puller/general).
Having a container image is key to create a Kubernetes CronJob to periodically fetch this data.

# Next steps

* junit testing on opensky_puller
* rename opensky-puller to kafka-opensky-publisher
