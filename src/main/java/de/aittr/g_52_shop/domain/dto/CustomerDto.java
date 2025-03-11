package de.aittr.g_52_shop.domain.dto;

import jakarta.persistence.Column;

import java.util.Objects;

public class CustomerDto {

    private Long id;
    private String name;
    //active не нужен для передачи на фрон-энд
    private CartDto cart;

    //конструктор пустой по-умолчанию

    //геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CartDto getCart() {
        return cart;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCart(CartDto cart) {
        this.cart = cart;
    }

    //методы

    @Override
    public String toString() {
        return String.format("Customer: id - %d; name - %s",
                id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerDto that = (CustomerDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(cart, that.cart);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(cart);
        return result;
    }
}
