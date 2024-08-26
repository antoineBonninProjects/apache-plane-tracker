# OpenSky Puller

This is a simple Java Project to pull Aircrafts State from the [OpenSky public API](https://openskynetwork.github.io/opensky-api/rest.html).
Then this project sends State variables to kafka topics.

The zone to scan for Aircrafts and the Kafka broker address can be configured via environment variables in *.envrc*.

# Project use

## Setup

Make sure you have *docker* installed.

Fill in the .envrc file and source it.

```sh
source .envrc
```

## Run from prebuilt image

Pull the abonnin33/opensky-puller image
```sh
docker pull abonnin33/opensky-puller:latest
```

Then run the code

```sh
docker run abonnin33/opensky-puller:latest
```

## Run you own image

You can fork this project and create your own image

```sh
mvn clean package;                                      # Build the .jar fatjar with dependencies
docker build -t <your-username>/opensky-puller:latest;  # Build the docker image
docker login;
docker push <your-username>/opensky-puller:latest;      # Push the image: optionnal
```

Then  run it

```sh
docker run <your-username>/opensky-puller:latest

```