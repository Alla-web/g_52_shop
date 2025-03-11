package de.aittr.g_52_shop.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    /*
   Мы хотим, чтобы имя юзера соответствовало требованиям:
    1. Не должно быть короче 2 символов.
    2. Не должно содержать цифры и спец.символы.
    3. Первая буква должна быть в верхнем регистре.
    4. Остальные буквы должны быть в нижнем регистре.
     */
    @Column(name = "name")
    @NotNull(message = "Customer name should be not null")
    @NotBlank(message = "Customer name should be not empty")
    @Pattern(
            //регулярное выражение:
            //1-я буква должна быть заглавная, далее идут буквы маленькие и поле может
            // состоять из 2-х и болле слов
            regexp = "[A-Z][a-z ]{1,}",
            message = "Customer name should be at list two characters length and start with capital letter"
    )
    private String name;

    @Column(name = "active")
    private boolean active;

    //связь один к одному со стороны той таблицы, в которой нет
    // колонки, ссылающейся ся на другую таблицу
    //указывается поле private Customer customer в классе Cart

    //каскадные операции - когда мы что-то делаем с покупателем, тоже
    // самое нужно делать и с его дочерним элементом - корзиной
    //CascadeType.ALL - все операции с покупателем будут каскадно применяться и к корзине
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;

    //constructors
    public Customer(){

    }

    //getters and setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    //methods

    //не указываем корзину в то стринг, чтобы не возникло цикличности
    @Override
    public String toString() {
        return String.format("Customer: id - %d; name - %s; isActive - %s",
                id, name, active ? "yes" : "no");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;
        return active == customer.active && Objects.equals(id, customer.id) && Objects.equals(name, customer.name) && Objects.equals(cart, customer.cart);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Boolean.hashCode(active);
        result = 31 * result + Objects.hashCode(cart);
        return result;
    }
}
