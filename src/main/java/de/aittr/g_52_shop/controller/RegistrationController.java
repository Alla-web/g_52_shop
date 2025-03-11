package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.exception_handling.exceptions.Response;
import de.aittr.g_52_shop.service.interfaces.ConfirmationService;
import de.aittr.g_52_shop.service.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final ConfirmationService confirmationService;

    //constructor
    public RegistrationController(UserService userService, ConfirmationService confirmationService) {
            this.userService = userService;
            this.confirmationService = confirmationService;
    }

    //сохраняем пользователя (отправка email с кодом)
    @PostMapping
    public Response register(@RequestBody User user) {
        userService.register(user);
        return new Response("Registration is successful. Check your mailbox for confirmation your registration");
    }

    //ДЗ 18
    //подтвержение регистрации (активируем пользователя после перехода ним по ссылке в имейле)
    @GetMapping("/{code}")
    public ResponseEntity<String> confirmRegistration(@PathVariable String code) {
        try {
            confirmationService.confirmRegistration(code);
            return ResponseEntity.ok("User successfully activated!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
