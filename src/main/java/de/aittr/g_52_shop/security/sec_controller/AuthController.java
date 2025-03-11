package de.aittr.g_52_shop.security.sec_controller;

import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.security.sec_dto.RefreshRequestDto;
import de.aittr.g_52_shop.security.sec_dto.TokenResponseDto;
import de.aittr.g_52_shop.security.sec_service.AuthService;
import jakarta.security.auth.message.AuthException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    //задача этого контроллера - принимпать все запросы, которые касаются ваторизации

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    //methods

    //login user и выдача ему 2-х токенов
    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody User user){
        //возможны ошибки, поэтому try / catch
        try {
            return service.login(user);
        } catch (AuthException e) {
            return new TokenResponseDto(null);
        }
    }

    //получение нового ексес-токена
    @PostMapping("/refresh")
    public TokenResponseDto getNewAccessToken(@RequestBody RefreshRequestDto refreshRequest){
        return service.getNewAccessToken(refreshRequest.getRefreshToken());
    }
}
