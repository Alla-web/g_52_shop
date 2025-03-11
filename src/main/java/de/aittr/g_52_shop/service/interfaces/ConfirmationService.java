package de.aittr.g_52_shop.service.interfaces;

import de.aittr.g_52_shop.domain.entity.User;

public interface ConfirmationService {

    //сервис будет гененрировать код для пользователя

    //метод для генерации кода подтверждения
    String generateConfirmationCode(User user);

    //метод для подтверждения регистрации юзера (ДЗ 18)
    boolean confirmRegistration(String code);
}
