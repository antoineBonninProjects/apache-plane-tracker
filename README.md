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


# Terminal 2 - Consumer
kafkacat -C -b $(minikube ip):30092 -t test

```