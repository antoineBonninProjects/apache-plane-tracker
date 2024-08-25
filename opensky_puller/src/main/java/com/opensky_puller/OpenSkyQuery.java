package com.opensky_puller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class OpenSkyQuery {

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

        // Anonymous queries: 500 credits/day (1-4 credits per requests depending on area)
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();

        // Send the request and handle the response - HTTP 200 expected
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject jsonObject = new JSONObject(response.body());
                JSONArray statesArray = jsonObject.getJSONArray("states");
                Long time = jsonObject.getLong("time");

                System.out.println("Time: " + time + "\n");
                for (int i = 0; i < statesArray.length(); i++) {
                    JSONArray stateArray = statesArray.getJSONArray(i);
                    AircraftState aircraftState = new AircraftState(stateArray);
                    System.out.println(aircraftState.toString() + '\n');
                }
            } else {
                System.out.println("Failed to fetch data. HTTP Status Code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
