package de.aittr.g_52_shop.security.sec_controller;

import de.aittr.g_52_shop.domain.entity.Role;
import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.repository.UserRepository;
import de.aittr.g_52_shop.security.sec_dto.TokenResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class AuthControllerTestIT {

    //не нужно писать тесты, зависящие от БД
    /*
    н-р, нам для теста нужен тестовый юзер, мы его создаем в классе, но в БД его нет.
    Вручную добавлять юзера в БД не нужно, а нужно добавить заглушку
    */

    @Autowired
    private TestRestTemplate restTemplate;

    private User testUser;
    private User mockUser; //заглушка

    @MockitoBean //делает этот репозиторий заглушкой
    //и нужно ее запрограммировать, чтобы она нам возвращщала мокюзера
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        testUser = createTestUser();
        mockUser = createMocktUser();
        //инструкции для загрузки -
        when(userRepository.findByUsername("Test User")).thenReturn(Optional.of(mockUser));
    }

    //тестируем процесс логина
    @Test
    public void checkSuccessWhileLogin(){
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<User> request = new HttpEntity<>(testUser, headers);
        ResponseEntity<TokenResponseDto> response = restTemplate.exchange(
                "/auth/login", HttpMethod.POST, request, TokenResponseDto.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status");
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getAccessToken());
        assertNotNull(response.getBody().getRefreshToken());
    }

    private User createTestUser() {
        User user = new User();
        user.setUsername("Test User");
        user.setPassword("777");
        return user;
    }

    private User createMocktUser() {
        Role role = new Role();
        role.setTitle("ROLE_USER");

        User user = new User();
        user.setId(777L);
        user.setActive(true);
        user.setUsername("Test User");
        user.setRoles(Set.of(role));
        user.setPassword(new BCryptPasswordEncoder().encode("777"));
        return user;
    }
}