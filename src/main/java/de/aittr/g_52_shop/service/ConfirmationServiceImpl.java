package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.entity.ConfirmationCode;
import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.repository.ConfirmationCodeRepository;
import de.aittr.g_52_shop.service.interfaces.ConfirmationService;
import org.aspectj.apache.bcel.classfile.Code;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationServiceImpl implements ConfirmationService {

    //сервис будет генерировать код подтверждения регистрации для пользователя

    private final ConfirmationCodeRepository repository;

    //constructor
    public ConfirmationServiceImpl(ConfirmationCodeRepository repository) {
        this.repository = repository;
    }

    //methods
    @Override
    public String generateConfirmationCode(User user) {
        //генерируем код
        String code = UUID.randomUUID().toString();
        //рассчитываем срок истечен7ия кола - 5 минут
        LocalDateTime expired = LocalDateTime.now().minusMinutes(5);

        //создаем сущность подтверждения кода
        ConfirmationCode codeEntity = new ConfirmationCode(code, expired, user);

        //сохраняем сущность подтверждения кода в БД
        repository.save(codeEntity);

        return code;
    }

    //метод для подтверждения регистрации юзера (ДЗ 18)
    @Override
    public boolean confirmRegistration(String code) {

        Optional<ConfirmationCode> optionalCode = repository.findByCode(code);

        if(optionalCode.isEmpty()){
            throw new RuntimeException("Invalid confirmation code");
        }

        ConfirmationCode confirmationCode = optionalCode.get();

        //проверяем состояние полученного кода (истёт или нет)
        if (confirmationCode.getExpired().isBefore(LocalDateTime.now())){
            repository.delete(confirmationCode);
        }

        //получаем пользователя и активируем его
        User user = confirmationCode.getUser();
        user.setActive(true); //устанавливаем статус активный
        repository.delete(confirmationCode); //удаляем код после подтверждения

        return true;
    }
}

//метод для генерации рандомного уникального кода подтверждения регистрации пользователя
//    public static void main(String[] args) {
//        for (int i = 0; i < 5; i++) {
//            System.out.println(UUID.randomUUID());
//        }
//    }
//}
