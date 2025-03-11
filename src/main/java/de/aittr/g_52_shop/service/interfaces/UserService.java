package de.aittr.g_52_shop.service.interfaces;

import de.aittr.g_52_shop.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
  //extends UserDetailsService - доступ к метода получения ролей юзера

    void register(User user);
}
