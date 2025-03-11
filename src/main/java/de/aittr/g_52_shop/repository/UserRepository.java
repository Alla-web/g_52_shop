package de.aittr.g_52_shop.repository;

import de.aittr.g_52_shop.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {



    //нестандартный метод пишем сами
    //Спринг реализует сам абстракный метод, догадавшись по его названию
    Optional<User> findByUsername(String username);
    //возвращаем Optional, чтобы избежать null.
    // Optional возвращает пустой объект, если юзер не нащёлся
}
