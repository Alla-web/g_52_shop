package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.exception_handling.exceptions.ProductNoFoundException;
import de.aittr.g_52_shop.exception_handling.exceptions.Response;
import de.aittr.g_52_shop.service.interfaces.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
/*
Анотация @RestController позволяет определить Spring то, что наш класс является
контроллером. На старте приложения Spring сканирует наши классы и находит анотации.
По нахождению контроллера Spring автоматически создаёт java-объект этого класса (spring-bin)
для дальнейшей работы с ними в рамках выполнения программы.
Spring-контекст - хранилище в памяти работающего приложения  на технологии Spring, где хранятся
все объекты (spring-bin)

Анотация @RequestMapping говорит Spring говорит, что все http-запросы, пришедшие на
энд-поинт /products нужно адресовать именно этому контроллеру.
Когда на наше приложение придёт http-запрос на /products, Spring будет сам вызывать нужные
методы у этого контроллера.
IoC - Inversion of Control (инверсия контроля) - этот принцип говорит о том, что мы
 только пишем методы, а управление этими методами ложится на плечи фреймворка.
 Фреймворк создаёт сам нужные объекты наших классов
и сам в нужный момент вызывает их методы
 */

@RestController //делает наш класс spring-bin-ном
@RequestMapping("/products") //при запросе на этот вызываем методы этого класса
public class ProductController {

    //поле сервиса продуктов, чтобы мымогли в контроллере вызывать его методы
    private final ProductService service;

    //конструктор на поле сервиса для Spring для вызова метода
    /*
    При старте приложения Спринг будет создавать объект данного контроллера.
    При создании объекта контроллера Спринг вызовет этот конструктор, потому
    что других вариантов создать объект больше нет, конструктор у нас один.
    Этот конструктор требует на вход объект сервиса продуктов.
    Поэтому Спринг обратится к Спринг контексту, найдёт там объект сервиса
    и передаст его в это поле.
    Поэтому, чтобы всё отработало, нам нужно обеспечить, чтобы объект сервиса
    вообще был в этом контексте.
     */

    public ProductController(ProductService service) {
        this.service = service;
    }

    //Разрабатываем Rest-API для нашего приложения
    //Разрабатываем Rest-API - это значит определить на какие енд-поинты должен обращаться клиент,
    // чтобы выполнить те или иные операции

    //договариваемся на берегу:

    //Сохранить продукт в базе данных (при сохранении продукт автоматически считается активным).
    // POST -> http://IP-adress:8080/products (продукт передаётся в теле в виде JSON)
    @PostMapping //работает в паре с @RequestMapping("/products")
    // @PostMapping - когда прийдёт Post-запрос на ресурс /products Spring нужно
    // вызвать именно этот метод

    //@RequestBody говорит Spring, что он должен прочитать в JSON в теле запроса,
    // преобразовать его при помощи встроегного Jackson в Джава-объект и получившийся
    // Джава-объект передать в параметр product
    public ProductDto save(@RequestBody ProductDto product) {
        return service.save(product);
    }

    //Вернуть все продукты из базы данных (активные).
    // GET -> http://IP-adress:8080/products/all
    @GetMapping("/all")
    public List<ProductDto> getAll() {
        return service.getAllActiveProducts();
    }

    //Вернуть один продукт из базы данных по его идентификатору (если он активен):
    //2 варианта:
    //1) GET -> http://IP-adress:8080/products?id=5 - при помощи указания параметра (редко исп-ся)
    //2) GET -> http://IP-adress:8080/products/5 - при помощи подстроки запроса (проще и короче)

    @GetMapping("/{id}") //
    // когда прийтдёт GET-запрос, нужно записать число,
    // стоящее в самом конце и передать его в параметр метода, который называется id

    //аннотация @PathVariable говорит Спрингу, что значение для этого параметра нужно
    // получить из строки запроса. А какой
    public ProductDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    //Изменить один продукт в базе данных по его идентификатору.
    // PUT -> http://IP-adress:8080/products (идентификатор будет отправлен в теле запроса)
    @PutMapping
    public void update(@RequestBody ProductDto product) {
        service.update(product);
    }

    //Удалить продукт из базы данных по его идентификатору.
    // DELETE -> http://IP-adress:8080/products/5
    @DeleteMapping("/{id}") // берём наш id из конца запроса
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }

    //Удалить продукт из базы данных по его наименованию.
    //2 варианта:
    // DELETE -> http://IP-adress:8080/products/by-title/banana - используем этот
    // DELETE -> http://IP-adress:8080/products?title=banana - аннотация @RequestParam и
    //           в параметрах и пустой  @DeleteMapping
    @DeleteMapping("/by-title/{title}")
    public void deleteByTitle(@PathVariable String title) {
        service.deleteByTitle(title);
    }

    //Восстановить удалённый продукт в базе данных по его идентификатору.
    // нельзя льправлять одинаковые запросы (см. стр 46).
    // Должен отличаться либо тип запроса, либо энд-поинт
    // PUT -> http://IP-adress:8080/products/restore/5
    @PutMapping("/{id}")
    public void restoreById(@PathVariable Long id) {
        service.restoreById(id);
    }

    //Вернуть общее количество продуктов в базе данных (активных).
    // GET -> http://IP-adress:8080/products/quantity - количество
    @GetMapping("/quantity")
    public long getProductsQuantity() {
        return service.getAllActiveProductsCount();
    }

    //Вернуть суммарную стоимость всех продуктов в базе данных (активных).
    // GET -> http://IP-adress:8080/products/total-cost - сумма
    @GetMapping("/total-cost")
    public BigDecimal getProductsTotalCost() {
        return service.getAllActiveProductsTotalCost();
    }

    //Вернуть среднюю стоимость продукта в базе данных (из активных).
    // GET -> http://IP-adress:8080/products/avg-price
    @GetMapping("/avg-price")
    public BigDecimal getProductsAveragePrice() {
        return service.getAllActiveProductsAveragePrice();
    }

    //метод обработки ошибки не найденного товара по id
    /*
1 способ обработки ошибок
ПЛЮС -  мы точечно настраиваем обработчик ошибок именно для данного контроллера,
        если нам требуется разная логика обработки ошибок для разных контроллеров
МИНУС - если нам не требуется разная логика обработки ошибок для разных контроллеров,
        то при таком подходе нам придётся во всех контроллерах создавать такие
        одинаковые обработчики
 */
/*
    Этот метод является обработчиком конкретных исключений
    типа ProductNotFoundException.
    На это указывает аннотация @ExceptionHandler.
    Как это работает:
1. Эксепшен выбрасывается сервисом.
2. Т.к. эксепшен в сервисе не обрабатывается, он пробрасывается
   вызывающему коду - то есть контроллеру.
3. Т.к. в контроллере есть обработчик этого эксепшена, и Спринг это видит,
   благодаря аннотации, Спринг перехватывает этот эксепшен.
4. Спринг вызывает сам наш метод handleException, передавая в параметр e
   перехваченный эксепшен.
 */
//    @ExceptionHandler(ProductNoFoundException.class)
 //   public Response handleException(ProductNoFoundException e){
 //       return new Response(e.getMessage());
  //  }
}
