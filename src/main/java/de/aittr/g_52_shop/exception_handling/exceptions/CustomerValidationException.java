package de.aittr.g_52_shop.exception_handling.exceptions;

public class CustomerValidationException extends RuntimeException{

    //конструктор, принимающий сообщение об ошибке
    public CustomerValidationException(String message) {
        super(message);
    }

    //конструктор, который на вход принимает Throwable cause, который принимает
    // причину возникновения ошибки
    public CustomerValidationException(Throwable cause) {
        super(cause);
    }
}
