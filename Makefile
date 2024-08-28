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
	@echo "create the namespace..."
	@kubectl apply -f namespace/namespace.yaml;
	@echo "install spark-operator in namespace. Make sure it references 'spark-operator' serviceAccount"
	@helm repo add spark-operator https://kubeflow.github.io/spark-operator && helm repo update;
	@helm status spark-operator --namespace apache-plane-tracker >/dev/null 2>&1 \
	  || helm install spark-operator spark-operator/spark-operator \
	  --namespace apache-plane-tracker \
	  -f spark/helm-spark-operator-values.yaml;
	@echo "apply the K8s stack..."
	@kubectl apply -f zookeeper/zookeeper.yaml;
	@MINIKUBE_IP=$$(minikube ip) envsubst < kafka/kafka.yaml | kubectl apply -f -;
	@/bin/bash -c 'set -a; source .envrc; set +a; envsubst < cronjobs/opensky_kafka_publisher.yaml | kubectl apply -f -';
	@kubectl apply -f spark/spark-serviceaccount.yaml;
	@kubectl apply -f spark/spark-rolebinding.yaml;
	@kubectl apply -f spark/aircraft_stream_analytics.yaml;
