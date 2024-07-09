package com.telemedicicne.telemedicicne.videoCallConfig;

public class AnswerPayload {
    private String from;
    private String to;
    private String signal;

    // Constructors, getters, and setters
    // Constructor
    public AnswerPayload(String from, String to, String signal) {
        this.from = from;
        this.to = to;
        this.signal = signal;
    }

    // Getters and setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }
}
