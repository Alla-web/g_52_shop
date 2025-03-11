package de.aittr.g_52_shop.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //связь один к одному со стороны той таблицы, в которой есть
    // колонка, ссылающая ся на другую таблицу
    @OneToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore //говорит Json, что при формировании объекта корзины,
    // не нужно формировать объект покупателя,
    //иначе произойдёт зацикливание - Json будет прописывать покупателя,
    // в нем корзину, в ней покупателя и так по кругу
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "cart_product",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )

    private List<Product> products;

    //constructors
    public Cart() {

    }


    // getters + setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


    // methods


    public void addProduct(Product product){
        if(product.isActive()){
            products.add(product);
        }
    }


    public List<Product> getAllActiveProducts(){
        return  products
                .stream()
                .filter(Product::isActive)
                .toList();
    }


    public void removeProductById(Long id){
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()){
            if(iterator.next().getId().equals(id)){
                iterator.remove();
                break;
            }
        }

    }

    //удалить все продукты
    public void removeAll(){
        products.clear();
    }

    //посчитать
    public BigDecimal getActiveProductsTotalCost(){
        return products
                .stream()
                .filter(Product::isActive)
                .map(Product::getPrice)
                //reduce возвращает Optional, т.к. возможно не будет активных товаров
                .reduce(BigDecimal::add)
                //возвращаем значение по умолчанию, если нет активных товаров или другая ошибка
                .orElse(new BigDecimal(0));
    }

    //рассчёт средней стоиомсти продукта в корзине
    public BigDecimal getActiveProductAveragePrice(){
        //без конвертации из BigDecimal в другие типы для рассчёта среднего
        long activeProductCount = products
                .stream()
                .filter(Product::isActive)
                .count();

        if(activeProductCount == 0){
            return new BigDecimal(0);
        }
        return getActiveProductsTotalCost().divide(new BigDecimal(activeProductCount), RoundingMode.CEILING);
    }

    //не указываем покупателя в то стринг, чтобы не возникло цикличности
    @Override
    public String toString() {
        return String.format("Cart: id - %d", id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id) && Objects.equals(customer, cart.customer) && Objects.equals(products, cart.products);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(customer);
        result = 31 * result + Objects.hashCode(products);
        return result;
    }
}
