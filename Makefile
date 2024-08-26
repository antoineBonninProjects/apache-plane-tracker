.PHONY: teardown stop start

teardown:
	@kubectl delete all --all -n kafka;
	@kubectl delete namespace kafka;
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
	@kubectl apply -f namespace/namespace.yaml;
	@kubectl apply -f zookeeper/zookeeper.yaml;
	@MINIKUBE_IP=$$(minikube ip) envsubst < kafka/kafka.yaml | kubectl apply -f -;
	@kubectl apply -f cronjobs/opensky_puller.yaml
