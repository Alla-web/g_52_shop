package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.dto.CartDto;
import de.aittr.g_52_shop.domain.dto.CustomerDto;
import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.domain.entity.Role;
import de.aittr.g_52_shop.repository.CustomerRepository;
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
import static org.junit.jupiter.api.Assertions.*;

import javax.crypto.SecretKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerControllerTestIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private CustomerDto testCustomer;

    private String adminAccessToken;
    private SecretKey accessKey;

    @Value("${key.access}")
    private String accessPhrase;

    private final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private CustomerRepository repository;

    //метод, который запускается перед тестами
    @BeforeEach
    public void setUp(){
        accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));
        adminAccessToken = generateAdminAccessToken();
        testCustomer = createTestCustomer();
    }

    //тестируем метод getAllActiveCustomersCount
    @Test
    @Order(1)
    public void checkRequestForAllActiveCustomer() {
        //создаём заголовки HTTP-запроса
        HttpHeaders headers = new HttpHeaders();

        //создаем тело Http-запроса
        HttpEntity<Void> request = new HttpEntity<>(headers);

        //отправляем на наше тестовое приложение подготовленный реальный Http-запрос запрос
        //для сохранения ответов запросов исп-ся класс ResponseEntity
        //ожидаем ответ в виде листа продуктов
        ResponseEntity<CustomerDto[]> response = testRestTemplate.exchange(
                //указываем энд-поинт для запроса, тип запроса,
                // сам объект запроса и класс, объекты которого ожидаем получить в теле ответа
                "/customers/all", HttpMethod.GET, request, CustomerDto[].class
        );

        //сравниваем ожидания / реальность

        //сранвиваем реальный и ожидаемый статусы ответов
        // проверяем действительно с сервера пришёл ожидаемый статус ответа
        // и выводим сообщение, если нет
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status");

        //проверяем, не пустой ли нам пришел объект покупателя
        for (CustomerDto customer : response.getBody()) {
            assertNotNull(customer.getId(), "Customer id should by not null");
            assertNotNull(customer.getName(), "Customer name should by not null");
            assertNotNull(customer.getCart(), "Customer cart should by not null");
        }

    }

    //тестируем save покупателя в БД
    //действительно запрещает ли сервер доступ к этому методу без авторизации
    @Test
    @Order(2)
    public void checkForbiddenStatusWhileSavingCustomerWithoutAuthorization() {

        //создаём заголовки
        HttpHeaders headers = new HttpHeaders();

        //создаем объект запроса, куда передаем тестового покупателя и заголовки
        HttpEntity<CustomerDto> request = new HttpEntity<>(testCustomer, headers);

        //создаем переменную для ответа запроса
        ResponseEntity<CustomerDto> response = testRestTemplate.exchange(
              "/customers", HttpMethod.POST, request, CustomerDto.class
        );

        //сравниваем статусы
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Unexpected http status");
        assertNull(response.getBody(), "Response body should be null");
    }

    @Test
    @Order(3)
    public void checkSuccessWhileSavingCustomerWithAdminToken(){

        HttpHeaders headers = new HttpHeaders();
        //стандартное наименование для заголовка авторизации - Authorization
        //где взять токен - генерируем его при помощи метода
        headers.add(HttpHeaders.AUTHORIZATION, adminAccessToken);

        HttpEntity<CustomerDto> request = new HttpEntity<>(testCustomer, headers);

        ResponseEntity<CustomerDto> response = testRestTemplate.exchange(
               "/customers", HttpMethod.POST, request, CustomerDto.class
        );

        //мы ожидаем ответ со статусом 200 и объект продукта с айди
        //сравниваем статусы
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status");

        // проверяем, корректный ли нам пришёл объект покупателя после его сохранения
        CustomerDto savedCustomer = response.getBody();
        assertNotNull(savedCustomer, "Saved customer should not be null");
        assertNotNull(savedCustomer.getId(), "Saved customer id should not be null");
        assertEquals(testCustomer.getName(), savedCustomer.getName(), "Saved customer title has incorrect title");

        //удалять покупателя из БД нет необходимости, т.к. допускаются совпадения

    }

    private CustomerDto createTestCustomer(){

        CustomerDto testCustomer = new CustomerDto();

        CartDto testCart = new CartDto();
        testCart.setProducts(new ArrayList<>());

        testCustomer.setName("TestCustomer");
        testCustomer.setCart(testCart);
        return testCustomer;
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