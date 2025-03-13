package de.aittr.g_52_shop.security.sec_config;

import de.aittr.g_52_shop.security.sec_filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //класс конфигурации и нужно вызывать его методы, чтобы сконфигурировать наше приложение
@EnableWebSecurity
@EnableMethodSecurity //разрешать/запрещать методы для пользователей с разными ролями
public class SecurityConfig {

    private final String ADMIN_ROLE = "ADMIN";
    private final String USER_ROLE = "USER";

    private final TokenFilter filter;

    public SecurityConfig(TokenFilter filter) {
        this.filter = filter;
    }

    //прописываем методы, в которых в ручную создаём бины

    //бин для шифровки паролей
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    //бин для цепочки фильтров, которые проходит HTTP-запрос на пути к контроллеру
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //HttpSecurity http - конфигуратор фильтров для запросов
        return http
                //отключаем возможность подделывания запросов с целью похитить данные или перести деньги
                //csrf по-умолчанию запрещает все запросы, кроме GET
                .csrf(AbstractHttpConfigurer::disable)
                //настройка сессий. Такая настройка отключила сессии, т.е. сервер будет запрашивать логин и пароль при каждом запросе
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //добавляем базовую авторизацию(логин + пароль)
                .httpBasic(AbstractHttpConfigurer::disable)
                //конфигурируем доступ к разному функционалу приложения для разных ролей
                .authorizeHttpRequests(x -> x
                                //получить весь сиписок продуктов - доспупно всем,
                                //посмотреть 1 продукт - доспупно авторизованному юзеру,
                                //изменить продукт - доспупно только админу
                                .requestMatchers(HttpMethod.GET, "/products/all").permitAll()
                                //доступ на енд-поинт products - только админу и авторизированному юзеру
                                .requestMatchers(HttpMethod.GET, "/products/").hasAnyRole(ADMIN_ROLE, USER_ROLE)
                                //отправлять запросы на добавление новых товаров - доступно только админу
                                .requestMatchers(HttpMethod.POST, "/products").hasRole(ADMIN_ROLE)
                                .requestMatchers(HttpMethod.POST, "/customers").hasRole(ADMIN_ROLE)
                                .requestMatchers(HttpMethod.GET, "/customers/**").permitAll()
                                //правило применяется только к HTTP POST-запросам,
                                // которые отправляются на /auth/login и /auth/refresh.
                                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/refresh").permitAll()
                                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                                .requestMatchers(HttpMethod.GET, "/register/**").permitAll()
                                //
                                .requestMatchers(HttpMethod.GET, "/hello").permitAll()
                                .requestMatchers(HttpMethod.POST, "/files").hasRole(ADMIN_ROLE)
                                .requestMatchers(HttpMethod.PUT, "/customers/**").hasAnyRole(ADMIN_ROLE, USER_ROLE)
                                //ко всем настройкам имеют доступ только авторизированные, кто залогинился
                                //.anyRequest().authenticated() //остальные операции разрешены все авторизированным юзерам
                        //настройка, разрешающая всем доступ с запросами без авторизации
                        //.anyRequest().permitAll()
                        //build() позволяет сконфигурировать объект SecurityFilterChain для фильтрации HTTP-запросов
                ).addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
