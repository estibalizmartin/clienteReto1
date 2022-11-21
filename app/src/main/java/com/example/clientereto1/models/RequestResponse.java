package com.example.clientereto1.models;

public class RequestResponse {
    private boolean access;
    private String message;

    public RequestResponse() {
    }

    public RequestResponse(boolean access, String message) {
        this.access = access;
        this.message = message;
    }

    public RequestResponse(String message) {
        this.message = message;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
