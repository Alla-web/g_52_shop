package de.aittr.g_52_shop.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Objects;

/*
Сообщает Спрингу, что это Ентити-сущность, т.е. имеет
отражение в БД - под неё есть таблица в БД и нужно отображать её изменения в БД
 */
@Entity
/*
Аннотация @Table(name = "product") говорит Спрингу как называется
таблица для этой сущности в БД
 */
@Table(name = "product")
public class Product {
    /*
      @Id - указываем, что именно это поле является идентификатором
      @GeneratedValue(strategy = GenerationType.IDENTITY) - указываем, что генерацией
      идентификаторов занимается сама БД. Если id не генерируется базой, эта аннотация не нужна
      @Column(name = "id") - указываем в какой именно колонке таблице ледат значения этого поля
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /*
   Мы хотим, чтобы название продукта соответствовало требованиям:
    1. Не должно быть короче трёх символов.
    2. Не должно содержать цифры и спец.символы.
    3. Первая буква должна быть в верхнем регистре.
    4. Остальные буквы должны быть в нижнем регистре.
     */
    @Column(name = "title")
    @NotNull(message = "Product title cannot be null") //аннотация для валидации
    @NotBlank(message = "Product title cannot be empty") //аннотация для валидации
    @Pattern(
            //регулярное выражение:
            //1-я буква должна быть заглавная, далее идут буквы маленькие и поле может
            // состоять из 2-х и болле слов
            regexp = "[A-Z][a-z ]{2,}",
            message = "Product title should be at list thee characters length and start with capital letter"
    ) //аннотация для валидации
    private String title;


    @Column(name = "price")
    @NotNull(message = "Product title cannot be null") //аннотация для валидации
    @DecimalMin(
            value = "1.0",
            message = "Product price should be greater ar equal then 1"
    ) //аннотация для валидации (нестрогое равенство)
    @DecimalMax(
            value = "1000.0",
            inclusive = false,
            message = "Product price should be lesser then 1000"

    ) //максимальный предел, но не включительно сам предел
    private BigDecimal price;

    @Column(name = "active")
    private boolean active;

    @Column(name = "image")
    private String image;

    //constructors
    public Product() {

    }

    //getters and setters

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

    public boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //methods

    @Override
    public String toString() {
        return String.format("Product: id - %d; title - %s; price - %.2f; active - %s",
                id, title, price, active ? "yes" : "no");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return active == product.active && Objects.equals(id, product.id) && Objects.equals(title, product.title) && Objects.equals(price, product.price) && Objects.equals(image, product.image);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(title);
        result = 31 * result + Objects.hashCode(price);
        result = 31 * result + Boolean.hashCode(active);
        result = 31 * result + Objects.hashCode(image);
        return result;
    }
}
