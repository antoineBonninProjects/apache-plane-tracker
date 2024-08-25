package com.opensky_puller;

import org.json.JSONArray;

public class AircraftState {
    private String icao24;
    private String callsign;
    private String originCountry;
    private Long lastUpdate;
    private Long firstSeen;
    private Double latitude;
    private Double longitude;
    private Double barometricAltitude;
    private Boolean onGround;
    private Double velocity;
    private Double trueTrack;
    private Double verticalRate;
    private String additionalInfo;
    private Double geometricAltitude;
    private String squawkCode;
    private Boolean emergency;
    private Integer reserved;

    public AircraftState(JSONArray stateArray) {

        if (stateArray.length() != 17) {
            System.out.println("Invalid stateArray, expecting 17 state variables" + stateArray);
        }

        this.icao24 = stateArray.getString(0);
        this.callsign = stateArray.getString(1).trim();
        this.originCountry = stateArray.getString(2);
        this.lastUpdate = stateArray.getLong(3);
        this.firstSeen = stateArray.getLong(4);
        this.latitude = stateArray.getDouble(5);
        this.longitude = stateArray.getDouble(6);
        this.barometricAltitude = stateArray.isNull(7) ? -1.0 : stateArray.getDouble(7);
        this.onGround = stateArray.getBoolean(8);
        this.velocity = stateArray.getDouble(9);
        this.trueTrack = stateArray.getDouble(10);
        this.verticalRate = stateArray.isNull(11) ? -1.0 : stateArray.getDouble(11);
        this.additionalInfo = stateArray.isNull(12) ? "N/A": stateArray.getString(12);
        this.geometricAltitude = stateArray.isNull(13) ? -1.0 : stateArray.getDouble(13);
        this.squawkCode = stateArray.isNull(14) ? "N/A" : stateArray.getString(14);
        this.emergency = stateArray.getBoolean(15);
        this.reserved = stateArray.getInt(16);
    }

    public String getIcao24() { return this.icao24; }
    public String getCallsign() { return this.callsign; }
    public String getOriginCountry() { return this.originCountry; }
    public Long getLastUpdate() { return this.lastUpdate; }
    public Long getFirstSeen() { return this.firstSeen; }
    public Double getLatitude() { return this.latitude; }
    public Double getLongitude() { return this.longitude; }
    public Double getBarometricAltitude() { return this.barometricAltitude; }
    public Boolean getOnGround() { return this.onGround; }
    public Double getVelocity() { return this.velocity; }
    public Double getTrueTrack() { return this.trueTrack; }
    public Double getVerticalRate() { return this.verticalRate; }
    public String getAdditionalInfo() { return this.additionalInfo; }
    public Double getGeometricAltitude() { return this.geometricAltitude; }
    public String getSquawkCode() { return this.squawkCode; }
    public Boolean getEmergency() { return this.emergency; }
    public Integer getReserved() { return this.reserved; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("icao24: ").append(icao24).append(", ")
        .append("callsign: ").append(callsign).append(", ")
        .append("originCountry: ").append(originCountry).append(", ")
        .append("lastUpdate: ").append(lastUpdate).append(", ")
        .append("firstSeen: ").append(firstSeen).append(", ")
        .append("latitude: ").append(latitude).append(", ")
        .append("longitude: ").append(longitude).append(", ")
        .append("barometricAltitude: ").append(barometricAltitude).append(", ")
        .append("onGround: ").append(onGround).append(", ")
        .append("velocity: ").append(velocity).append(", ")
        .append("trueTrack: ").append(trueTrack).append(", ")
        .append("verticalRate: ").append(verticalRate).append(", ")
        .append("additionalInfo: ").append(additionalInfo).append(", ")
        .append("geometricAltitude: ").append(geometricAltitude).append(", ")
        .append("squawkCode: ").append(squawkCode).append(", ")
        .append("emergency: ").append(emergency).append(", ")
        .append("reserved: ").append(reserved).append(", ");

        return sb.toString();
    }
}