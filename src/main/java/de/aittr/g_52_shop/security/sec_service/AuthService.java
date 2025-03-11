package de.aittr.g_52_shop.security.sec_service;

import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.security.sec_dto.TokenResponseDto;
import de.aittr.g_52_shop.service.UserServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserServiceImpl userService;
    private final TokenService tokenService;
    //хранилилие последних выданных рефреш-токенов
    private final Map<String, String> refreshStorage;
    //поле для расшифроки пришедшего пароля
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(
            UserServiceImpl userService,
            TokenService tokenService,
            BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.refreshStorage = new HashMap<>();
    }

    //метод первичной проверки пароля и логина и отдачи ему access и refresh токенов
    public TokenResponseDto login(User inboundUser) throws AuthException {
        String username = inboundUser.getUsername();
        UserDetails foundUser = userService.loadUserByUsername(username);

        //сравниваем пароль входящего юзера (сырой пароль) и пароль из БД(foundUser пароль)
        //сначалы сырой пароль, потом зашифрованый пароль передаем, иначе не будет работать
        if(passwordEncoder.matches(inboundUser.getPassword(), foundUser.getPassword())){
          String accessToken = tokenService.generateAccessToken(foundUser);
          String refreshToken = tokenService.generateRefreshToken(foundUser);
          refreshStorage.put(username, refreshToken);
          return new TokenResponseDto(accessToken, refreshToken);
        } else {
            throw new AuthException("Password is incorrect");
        }
    }

    //метод выдачи нового access-токена (когда он истек) на основании refresh токена
    public TokenResponseDto getNewAccessToken(String inboundRefreshToken){
        //извлекаем инфо о пользователе из принятого токена
        Claims refreshClaims = tokenService.getRafreshClaims(inboundRefreshToken);
        //получаем имя пользователя из объекта токена
        String username = refreshClaims.getSubject();
        //находим рефреш-токен из БД, привязанный к этому юзеру
        String foundRefreshToken = refreshStorage.get(username);
        //проверяем эквивалентны ли наши пришедший и найденный в БД токены
        if (foundRefreshToken != null || foundRefreshToken.equals(inboundRefreshToken)) {
            //подгружаем юзера из БД
            UserDetails foundUser = userService.loadUserByUsername(username);
            //генерируем accessToken
            String accessToken = tokenService.generateAccessToken(foundUser);
            return new TokenResponseDto(accessToken);
        } else {
            return  new TokenResponseDto(null);
        }
    }
}
