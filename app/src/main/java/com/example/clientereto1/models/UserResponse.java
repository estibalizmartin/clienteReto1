package com.example.clientereto1.models;

public class UserResponse {

    private boolean Access;
    private String message;
    private String username;
    private int id;

    public UserResponse() {
    }

    public UserResponse(boolean hasAccess, String message, String username, int id) {
        this.Access = hasAccess;
        this.message = message;
        this.username = username;
        this.id = id;
    }

    public boolean isAccess() {
        return Access;
    }

    public void setAccess(boolean hasAccess) {
        this.Access = hasAccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "hasAccess=" + Access +
                ", message='" + message + '\'' +
                ", username='" + username + '\'' +
                ", id=" + id +
                '}';
    }
}
