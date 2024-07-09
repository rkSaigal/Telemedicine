package com.telemedicicne.telemedicicne.videoCallConfig;
public class CallPayload {
    private String from;
    private String userToCall;
    private String signalData;
    private String name;

    // Constructors, getters, and setters
    // Constructor
    public CallPayload(String from, String userToCall, String signalData, String name) {
        this.from = from;
        this.userToCall = userToCall;
        this.signalData = signalData;
        this.name = name;
    }

    // Getters and setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUserToCall() {
        return userToCall;
    }

    public void setUserToCall(String userToCall) {
        this.userToCall = userToCall;
    }

    public String getSignalData() {
        return signalData;
    }

    public void setSignalData(String signalData) {
        this.signalData = signalData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

