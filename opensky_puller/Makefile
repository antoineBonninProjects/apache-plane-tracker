.PHONY: upload

upload:
	@mvn clean package;
	@docker build -t abonnin33/opensky-puller:latest .
	@docker push abonnin33/opensky-puller:latest