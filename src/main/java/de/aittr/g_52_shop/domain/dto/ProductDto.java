package de.aittr.g_52_shop.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Objects;

@Schema(description = "Class that describes Product DTO")
public class ProductDto {

    @Schema(
            description = "Product unique identifier",
            example = "777",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(description = "Product title", example = "Banana")
    private String title;

    @Schema(description = "Product price", example = "190.00")
    private BigDecimal price;

    //active не нужен для передачи на фрон-энд

    private String productImageUrl;


    //геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    //методы

    @Override
    public String toString() {
        return String.format("Product-DTO: id - %d; title - %s; price - %.2f",
                id, title, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductDto that = (ProductDto) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(price, that.price) && Objects.equals(productImageUrl, that.productImageUrl);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(title);
        result = 31 * result + Objects.hashCode(price);
        result = 31 * result + Objects.hashCode(productImageUrl);
        return result;
    }
}
