package com.zgg.hochat.bean;

public class ChatChangeEvent {
    private String message;

    public ChatChangeEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
