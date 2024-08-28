.PHONY: teardown stop start apply

teardown:
	@kubectl delete all --all -n apache-plane-tracker;
	@kubectl delete namespace apache-plane-tracker;

stop:
	@minikube stop;

start:
	@minikube start;
	@echo "Waiting for MINIKUBE_IP to be available...";
	@while ! minikube ip > /dev/null 2>&1; do \
		echo "Minikube is not ready yet. Waiting..."; \
		sleep 5; \
	done; \
	echo "Minikube is ready.";
	@$(MAKE) update;

apply:
	@kubectl apply -f namespace/namespace.yaml;
	@kubectl apply -f zookeeper/zookeeper.yaml;
	@MINIKUBE_IP=$$(minikube ip) envsubst < kafka/kafka.yaml | kubectl apply -f -;
	@/bin/bash -c 'set -a; source .envrc; set +a; envsubst < cronjobs/opensky_kafka_publisher.yaml | kubectl apply -f -'