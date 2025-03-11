package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.domain.entity.Role;
import de.aittr.g_52_shop.repository.ProductRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.server.reactive.HttpHandler;

import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

//аннотация SpringBootTest при старте тестов запускает наше приложение полноценно на
// тестовом экземпляре томкат
//SpringBootTest.WebEnvironment.RANDOM_PORT говрит, что тестовый экземпляр Томкат
// должен подняться на случайно выбранном свободном порту нашей системы
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

//в интеграционных тестах порядок тестирования методов иногда имеет значение,
// когда методы взаимосвязаны. Чтобы включить порядок нужна аннотация SpringBootTest
//чтобы задать нужный нам порядок тестов
//MethodOrderer.OrderAnnotation.class - говорит, что порядок будут регулировать
// отдельные аннотации над самими меодами
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTestIT {
    //узнать каким случайно выбранным портом пользуется Томкат
    //порт - это всегда целое число
    @LocalServerPort //позволяет в это поле сохранрить значение случайно вбранного порта,
    // на котором стартовал Томкат
    private int port;

    //TestRestTemplate - объект, при помощи которого мы можем отправлять реальные HTTP-запросы на
    //наши рест-контроллерыи получать ответы
    @Autowired //говорит фреймворку, что нужно создать объект типа TestRestTemplate и поместить в это поле
    //умеет отправлять реальные HTTP-запросы на сервер
    private TestRestTemplate restTemplate;

    //создаём тестовый объект продукта
    private ProductDto testProduct;
    //поле для создания токена доступа для авторизации юзера
    private String adminAccessToken;

    //секретный ключ для генерации и серетную фразу
    private SecretKey accessKey;

    @Value("${key.access}")
    private String accessPhrase;

    private final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private ProductRepository productRepository;

    //метод, который запускается перед тестами
    @BeforeEach
    public void setUp() {
        accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));
        adminAccessToken = generateAdminAccessToken();
        testProduct = createTestProduct();
    }

    @Test //говорит фреймворку, что это именно тестовый метод и его нужно запускать как тест
    @Order(1) //задает порядок тестирования. Должна быть из гото же пакета, что и
    // аннотация @TestMethodOrder
    public void checkRequestForAllActiveProducts() {
        //здесь создаём заголовки Http-запроса. Пока нам нечего положить в них,
        // поэтому они пока остаются пустыми
        HttpHeaders headers = new HttpHeaders();

        //создаем тело Http-запроса
        //создаём объект Http-запроса, передавая ему в конструктор объект заголовков.
        // При этом запрос мы параметризуем типом Void - никакие атрибуты не будем отправлять в тело запроса
        HttpEntity<Void> request = new HttpEntity<>(headers);

        //отправляем на наше тестовое приложение подготовленный реальный Http-запрос запрос
        //для сохранения ответов запросов исп-ся класс ResponseEntity
        //ожидаем ответ в виде листа продуктов
        ResponseEntity<ProductDto[]> response = restTemplate.exchange(
                //указываем энд-поинт для запроса, тип запроса,
                // сам объект запроса и класс, объекты которого ожидаем получить в теле ответа
                "/products/all", HttpMethod.GET, request, ProductDto[].class
        );

        //сравниваем ожидания / реальность

        //сранвиваем реальный и ожидаемый статусы ответов
        //проверяем действительно с сервера пришёл ожидаемый статус ответа и выводим соосбщение, если нет
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status");

        //проверяем не пустое ли тело ответа. Даже если в БД нет ни одного продукта, мы ожидаем пустой лист, а
        //пустой лист - это объект, а не null
        Assertions.assertNotNull(response.getBody(), "Response body should not null");

        //проверяем, не пустые ли свойства пришедших продуктов
        for (ProductDto productDto : response.getBody()) {
            Assertions.assertNotNull(productDto.getId(), "Product id should by not null");
            Assertions.assertNotNull(productDto.getTitle(), "Product title should by not null");
            Assertions.assertNotNull(productDto.getPrice(), "Product price should by not null");
        }

        //ещё можно проверить не повторяются ли id,

    }

    //обратимся без авторизации
    //обратимся с логином юзера
    //обратимся с логином админа
    //обратимся с невалидным токеном

    //действительно запрещает ли сервер доступ к этому методу без авторизации
    @Test
    @Order(2)
    public void checkForbiddenStatusWhileSavingProductWithoutAuthorization() {
        //создаём заголовки
        HttpHeaders headers = new HttpHeaders();

        //создаем объект запроса, куда передаем тестовый продукт и заголовки
        //на этот раз мы хотим отправить на сервер объект продукта, поэтому в объект запроса мы вкладываем
        // не только закголовки, а и объект продукта
        HttpEntity<ProductDto> request = new HttpEntity<>(testProduct, headers);

        //создаём переменную для ответа - продукта
        ResponseEntity<ProductDto> response = restTemplate.exchange(
                "/products", HttpMethod.POST, request, ProductDto.class
        );

        //сравниваем статусы
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Unexpected http status");
        Assertions.assertNull(response.getBody(), "Response body should be null");
    }

    @Test
    @Order(3)
    public void checkSuccessWhileSavingProductWithAdminToken() {
        HttpHeaders headers = new HttpHeaders();

        //стандартное наименование для заголовка авторизации - Authorization
        //где взять токен - генерируем его при помощи метода
        headers.add(HttpHeaders.AUTHORIZATION, adminAccessToken);

        HttpEntity<ProductDto> request = new HttpEntity<>(testProduct, headers);
        ResponseEntity<ProductDto> response = restTemplate.exchange(
                "/products", HttpMethod.POST, request, ProductDto.class
        );

        //мы ожидаем ответ со статусом 200 и объект продукта с айди
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status");

        //проверяем, корректный ли нам пришёл объект продукта после его сохранения
        ProductDto savedProduct = response.getBody();
        Assertions.assertNotNull(savedProduct, "Saved product should not be null");
        Assertions.assertNotNull(savedProduct.getId(), "Saved product id should not be null");
        Assertions.assertEquals(testProduct.getTitle(), savedProduct.getTitle(), "Saved product title has incorrect title");
        Assertions.assertEquals(testProduct.getPrice(), savedProduct.getPrice(), "Saved product title has incorrect price");

        //удаляем сохранённый продукт из базы для возможности при новых тестах использовать тестовый продукт
        productRepository.deleteById(savedProduct.getId());
    }

    //конструктор работает в тестах по-другому, поэтому наполняем объект при помощи метода
    //наполняем тестовый продукт свойствами
    private ProductDto createTestProduct() {
        ProductDto product = new ProductDto();
        product.setTitle("Test product");
        product.setPrice(new BigDecimal(777));
        return product;
    }

    //метод генерации токена доступа
    private String generateAdminAccessToken() {
        Role role = new Role();
        role.setTitle("ROLE_ADMIN");

        return BEARER_PREFIX + Jwts.builder()
                .subject("TestAdmin")
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .claim("roles", Set.of(role))
                .signWith(accessKey)
                .compact();
    }
}