package com.hidemessage.errors;

public record ErrorMessage(String message, String type) {

    public ErrorMessage(String message) {
        this(message, "Error");
    }

    public ErrorMessage(String message, String type) {
        this.message = message;
        this.type = type;
    }
}
