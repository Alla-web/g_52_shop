package de.aittr.g_52_shop.exception_handling.exceptions;

public class Response {

    private String message;

    //constructor
    public Response(String message) {
        this.message = message;
    }

    //getter
    public String getMessage() {
        return message;
    }
}
