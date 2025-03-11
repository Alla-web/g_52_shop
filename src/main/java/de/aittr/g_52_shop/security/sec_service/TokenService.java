package de.aittr.g_52_shop.security.sec_service;

import de.aittr.g_52_shop.domain.entity.Role;
import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.repository.RoleRepository;
import de.aittr.g_52_shop.security.AuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.xml.crypto.Data;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class TokenService {
    //Цель этого сервиса
    // создавать токены
    // провенрять токены
    // считывать токены
    // и передавать их дальше

    //сектерные ключи для генерации токена
    private SecretKey accessKey;
    private SecretKey refreshKey;
    //доступ к ролям юзера
    private RoleRepository roleRepository;

    //constructor для генерации ключей и репозитория
    public TokenService(
            //accessPhrase - фраза доступа
            @Value("${key.access}") String accessPhrase,
            //refreshPhrase - фраза обновления
            @Value("${key.refresh}") String refreshPhrase,
            RoleRepository roleRepository) {
        //вызываем у класса Keys метод, который генерирует секретные ключи для токенов,
        // указывая  при этом декодер, которым мы кодировали нашу секретную фразу
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshPhrase));
        this.roleRepository = roleRepository;
    }

    //методы генерации токенов

    //access - 24 hour, refresh - 1 week

    //метод генерироваться access-токена
    public String generateAccessToken(UserDetails user) {
        //получаем дату истечения токена
        LocalDateTime currentDate = LocalDateTime.now();
        //Instant класс для работы с временем с наносекундной точностью
        Instant expiration = currentDate.plusDays(1).atZone(ZoneId.systemDefault()).toInstant();
        //токен работает только с Date, поэтому переделываем Instant в Date
        Date expirationDate = Date.from(expiration);

        //формируем access-токен
        //Jwts - класс Json Web Tocens
        return Jwts.builder()
                //заклыдываем в токен имя юзера в токен
                .subject(user.getUsername())
                //закладываем дату истечения токена
                .expiration(expirationDate)
                //подписываем токен нашим ключем
                .signWith(accessKey)
                //допинформация закладывается методом claim:
                //добавляем данные о ролях юзера
                //"roles", "name" - ключи, которые мы придумываем сами
                .claim("roles", user.getAuthorities())
                //добавляем реальное имя юзера
                .claim("name", user.getUsername())
                //формирует строку в формате стринг
                .compact();
    }

    //метод генерироваться refresh-токена
    public String generateRefreshToken(UserDetails user) {
        //получаем дату истечения токена
        LocalDateTime currentDate = LocalDateTime.now();
        Instant expiration = currentDate.plusWeeks(1).atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(expiration);

        //формируем refresh-токен
        //refresh-токен не содержит столько информации,
        // т.к. это всё мы получим с access-токеном
        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(expirationDate)
                .signWith(refreshKey)
                .compact();
    }

    //методы проверки токенов, которые будут вызываться снаружи

    //ключи лежат только в этом классе, поэтому методы проврки пишем здесь
    //пишем сами 3 метода, 2 из которых вызывают одни и тот же, только с разными ключами
    //токены проверяются ординаково, просто при помощи разных ключей

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, accessKey);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, accessKey);
    }

    public boolean validateToken(String token, SecretKey key) {
        // try/catch - т.к. будут эксепшены
        try {
            //метод parser позволяет распарсить токен
            Jwts.parser()
                    //проверка на наличие внешних вмешательств,
                    // корректность, что он не истёк
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            //если проверка прошла успешно - токен валидный
            return true;
            //если проверка токена выбросит Exception - ключ не валидный
        } catch (Exception e) {
            return false;
        }
    }

    //методы, которые извлекают инфо из токенов

    //Claims это объект, содержащий данные пользотеля, полученные из токена
    public Claims getAccessClaims(String accessToken) {
        return getClaims(accessToken, accessKey);
    }

    public Claims getRafreshClaims(String refreshToken) {
        return getClaims(refreshToken, refreshKey);
    }

    //метод непосредственного извлечения данных юзера из обоих токенов
    public Claims getClaims(String token, SecretKey key) {
        return Jwts.parser()
                //передаем ключ, которым будем читать токен
                .verifyWith(key)
                .build()
                //передаём токен, который будем читать
                .parseSignedClaims(token)
                //получить итоговую загрузку - переделываем в объект Claims
                .getPayload();
    }

    //метод, который переделывает объект Claims в объект AuthInfo
    //по сути это обычный парсинг
    public AuthInfo mapClaimsToAuthInfo(Claims claims) {
        //смотрим, кто к нам пришел - какой юзернейм
        String username = claims.getSubject();

        /*
        внутри там сидит лист
        List: [
                HashMap: {"authority": "ROLE_ADMIN"} - если одна роль
                HashMap: {"authority": "ROLE_USER"} - если есть вторая роль и так далее, есло ролей много
              ]
         */
        List<LinkedHashMap<String, String>> rolesList =
                //claims.get("roles") - по этому ключу мы закладывали роли в токен
                //claims.get возвращает Object - неявно преобразовываем к List<LinkedHashMap<String, String>>
                (List<LinkedHashMap<String, String>>) claims.get("roles");

        //циклом переписываем роли из этого листа в сэт
        Set<Role> roles = new HashSet<>();

        for (LinkedHashMap<String, String> roleEntry : rolesList) {
            //название роли, вытащенное по ключу authority запишется в переменную roleTitle
            String roleTitle = roleEntry.get("authority");
            //в RoleRepository прописалди спец. метотод, позволяющий доставать роль по ее названию
            //достаём роль по названию и добавляем её в наш сэт roles
            //ifPresent делает что-то если роль существует - добавляет в сэт у нас
            roleRepository.findByTitle(roleTitle).ifPresent(x -> roles.add(x));
        }

        return new AuthInfo(username, roles);
    }
}
