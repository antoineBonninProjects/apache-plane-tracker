.PHONY: teardown stop start

teardown:
	@kubectl delete all --all -n kafka
	@kubectl delete namespace kafka
	@minikube stop

stop:
	@minikube stop

start:
	@minikube start;
	@echo "Waiting for MINIKUBE_IP to be available..."
	@while [ -z "$$MINIKUBE_IP" ]; do \
		export MINIKUBE_IP=$$(minikube ip); \
		if [ -n "$$MINIKUBE_IP" ]; then \
			echo "MINIKUBE_IP is now set to $$MINIKUBE_IP"; \
		else \
			echo "MINIKUBE_IP is not set yet. Waiting for minikube to start..."; \
			sleep 5; \
		fi; \
	done	
	@kubectl apply -f namespace/namespace.yaml
	@kubectl apply -f zookeeper/zookeeper.yaml
	@envsubst < kafka/kafka.yaml | kubectl apply -f -