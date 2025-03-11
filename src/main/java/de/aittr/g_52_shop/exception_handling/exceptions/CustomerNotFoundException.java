package de.aittr.g_52_shop.exception_handling.exceptions;

public class CustomerNotFoundException extends RuntimeException{
    //создаем непроверяемое исключение путем наследования от RuntimeException

    public CustomerNotFoundException(Long id) {
        super(String.format("Customer with id %d not found", id));
    }
}
