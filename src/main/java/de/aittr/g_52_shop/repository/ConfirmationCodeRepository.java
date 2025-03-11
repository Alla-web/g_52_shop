package de.aittr.g_52_shop.repository;

import de.aittr.g_52_shop.domain.entity.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {


    //пишем дополнительный абстрактный метод для поиска по самому коду
    // т.к. мф не будем знать до момента подтверждения его id
    Optional<ConfirmationCode> findByCode(String code);
}
