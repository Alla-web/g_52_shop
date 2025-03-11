package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.dto.CustomerDto;
import de.aittr.g_52_shop.service.interfaces.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    //создаем поле сервиса и конструктор на него для использования методов сервиса
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    //Сохранить покупателя в базе данных (при сохранении покупатель автоматически считается активным).
    //POST -> http://IP-adress:8080/customers (customer передаётся в теле в виде JSON)
    @PostMapping
    public CustomerDto save(@RequestBody CustomerDto customer) {
        return service.save(customer);
    }

    //Вернуть всех покупателей из базы данных (активных).
    //GET -> http://IP-adress:8080/customers/all
    @GetMapping("/all")
    public List<CustomerDto> getAll() {
        return service.getAllActiveCustomers();
    }

    //Вернуть одного покупателя из базы данных по его идентификатору (если он активен).
    // GET -> http://IP-adress:8080/customers/3
    @GetMapping("/{id}")
    public CustomerDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    //Изменить одного покупателя в базе данных по его идентификатору.
    // PUT -> http://IP-adress:8080/customers (идентификатор будет отправлен в теле запроса)
    @PutMapping
    public void update(@RequestBody CustomerDto customer) {
        service.update(customer);
    }

    //Удалить покупателя из базы данных по его идентификатору.
    //DELETE -> http://IP-adress:8080/customers/3
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }

    //Удалить покупателя из базы данных по его имени.
    //DELETE -> http://IP-adress:8080/customers/by-name/alla
    @DeleteMapping("/by-name/{name}")
    public void deleteByName(@PathVariable String name) {
        service.deleteByName(name);
    }

    //Восстановить удалённого покупателя в базе данных по его идентификатору.
    //PUT -> http://IP-adress:8080/customers/3
    @PutMapping("/{id}")
    public CustomerDto restore(@PathVariable Long id) {
        return null;
    }

    //Вернуть общее количество покупателей в базе данных (активных).
    //GET -> http://IP-adress:8080/customers/quantity
    @GetMapping("/quantity")
    public long getCustomersQuantity() {
        return service.getAllActiveCustomersCount();
    }

    //Вернуть стоимость корзины покупателя по его идентификатору (если он активен).
    //GET -> http://IP-adress:8080/customers/cart/total-cost
    @GetMapping("/cart/total-cost")
    public BigDecimal getCartTotalCost() {
        return BigDecimal.ZERO;
    }

    //Вернуть среднюю стоимость продукта в корзине покупателя по его идентификатору (если он активен)
    //GET -> http://IP-adress:8080/customers/cart/item-avg-cost
    @GetMapping("/cart/items-avg-cost")
    public BigDecimal getItemsAverageCost() {
        return BigDecimal.ZERO;
    }

    //Добавить товар в корзину покупателя по их идентификаторам (если оба активны)
    //PUT -> http://IP-adress:8080/customers/5/add-product/5
    @PutMapping("/{customerId}/add-product/{productId}")
    public void addProductToCart(@PathVariable Long customerId, Long productId) {

    }

    //Удалить товар из корзины покупателя по их идентификаторам
    //DELETE -> http://IP-adress:8080/customers/cart/7
    @DeleteMapping("/deleteById-product/{itemId}")
    public void deleteItemById(@PathVariable Long itemId) {

    }

    //Полностью очистить корзину покупателя по его идентификатору (если он активен)
    //DELETE -> http://IP-adress:8080/customers/3
    @DeleteMapping("/empty-cart/{id}")
    public void emptyCustomersCart(@PathVariable Long id) {

    }
}
