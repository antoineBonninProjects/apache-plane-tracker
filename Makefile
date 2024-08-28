.PHONY: teardown stop start update

export $(shell cat .envrc | xargs)

teardown:
	@kubectl delete all --all -n apache-plane-tracker;
	@kubectl delete namespace apache-plane-tracker;
	@minikube stop;

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

update:
	@kubectl apply -f namespace/namespace.yaml;
	@kubectl apply -f zookeeper/zookeeper.yaml;
	@MINIKUBE_IP=$$(minikube ip) envsubst < kafka/kafka.yaml | kubectl apply -f -;
	/bin/bash -c 'set -a; source .envrc; set +a; envsubst < cronjobs/opensky_puller.yaml | kubectl apply -f -'