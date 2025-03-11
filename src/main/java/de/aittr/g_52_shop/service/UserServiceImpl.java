package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.repository.UserRepository;
import de.aittr.g_52_shop.service.interfaces.EmailService;
import de.aittr.g_52_shop.service.interfaces.RoleService;
import de.aittr.g_52_shop.service.interfaces.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    //добавляем репозиторий юзеров (спингоский) и конструктор на него
    private final UserRepository repository;

    private final BCryptPasswordEncoder encoder;

    private final RoleService roleService;

    private final EmailService emailService;


    //constructor
    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder encoder, RoleService roleService, EmailService emailService) {
        this.repository = repository;
        this.encoder = encoder;
        this.roleService = roleService;
        this.emailService = emailService;
    }

    //при помощи этого метода Спринг будет получать из БД пользователей вместе с их ролями
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //обработка исключения на случай не найденого юзера заложена во фреймворке
        //но нам нужно её вов время вызвать

        //нам нужен не стандартный метод, а значит пишем сами
        //orElseThrow - выбросит эксепшен сам, если юзер не найден
        return repository.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException("User " + username + " not found")
        );
    }

    @Override
    public void register(User user) {
        user.setId(null);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(false);
        user.setRoles(Set.of(roleService.getRoleUser()));

        repository.save(user);
        emailService.sendConfirmationEmail(user);
    }
}
