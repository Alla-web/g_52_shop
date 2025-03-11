package de.aittr.g_52_shop.service.interfaces;

import de.aittr.g_52_shop.domain.entity.User;

public interface EmailService {

    //метод отпраки письма на имейл юзера с кодом подтверждения его регистрации
    void sendConfirmationEmail(User user);

}
