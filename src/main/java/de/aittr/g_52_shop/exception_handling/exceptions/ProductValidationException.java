package de.aittr.g_52_shop.exception_handling.exceptions;

public class ProductValidationException extends RuntimeException{

    //конструктор по автосхеме с приемом String message
    public ProductValidationException(String message) {
        super(message);
    }

    //конструктор, который на вход принимает Throwable cause, который принимает
    // причину возникновения ошибки
    public ProductValidationException(Throwable cause) {
        super(cause);
    }
}
