package de.aittr.g_52_shop.domain.dto;

import java.util.List;
import java.util.Objects;

public class CartDto {

    private Long id;
    private List<ProductDto> products;

    //getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    //methods

    @Override
    public String toString() {
        return String.format("Cart: id - %d", id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartDto cartDto = (CartDto) o;
        return Objects.equals(id, cartDto.id) && Objects.equals(products, cartDto.products);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(products);
        return result;
    }
}
