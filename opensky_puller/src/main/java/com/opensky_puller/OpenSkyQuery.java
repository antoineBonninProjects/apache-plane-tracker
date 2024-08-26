package com.opensky_puller;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class OpenSkyQuery {

    private static final String KAFKA_BROKER = "kafka-service:9092";
    private static final String TOPIC = "aircraft-states";

    public static void main(String[] args) {

        HttpClient client = HttpClient.newHttpClient();

        Double lamin=42.5;
        Double lomin=-1.5;
        Double lamax=45.0;
        Double lomax=1.5;
        String url = String.format(
            "https://opensky-network.org/api/states/all?lamin=%.1f&lomin=%.1f&lamax=%.1f&lomax=%.1f", 
            lamin, lomin, lamax, lomax
        );

        // Kafka Producer Configuration
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {

            // Anonymous queries: 500 credits/day (1-4 credits per requests depending on area)
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

            // Send the request and handle the response - HTTP 200 expected
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject jsonObject = new JSONObject(response.body());
                JSONArray statesArray = jsonObject.getJSONArray("states");
                Long time = jsonObject.getLong("time");

                for (int i = 0; i < statesArray.length(); i++) {
                    JSONArray stateArray = statesArray.getJSONArray(i);
                    AircraftState aircraftState = new AircraftState(stateArray);

                    String key = aircraftState.getIcao24(); // Assume AircraftState has a method to get ICAO24
                    String message = aircraftState.toString(); // Serialize the AircraftState to a JSON string
                    System.out.println("Time: " + time + "\n");
                    System.out.println("key: " + key + "\n");
                    System.out.println("message: " + message + "\n");

                    // Produce message to Kafka with key
                    ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, key, message);
                    Future<RecordMetadata> future = producer.send(record);

                    // Optionally, handle the result
                    RecordMetadata metadata = future.get(); // This will block until the message is acknowledged
                    System.out.printf("Produced record to topic %s partition %d with offset %d%n",
                        metadata.topic(), metadata.partition(), metadata.offset());
                }
            } else {
                System.out.println("Failed to fetch data. HTTP Status Code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
