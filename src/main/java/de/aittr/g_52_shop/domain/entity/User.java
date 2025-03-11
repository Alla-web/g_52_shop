package de.aittr.g_52_shop.domain.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements UserDetails {
//implements UserDetails - для использования методов Спринг-секьюрити

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "active")
    private boolean active;


    //при обращении к юзеру, из базы вытащатся сразу и его роли
    //есть жадная и ленивая автоподгрузка ролей
    //жадная - всегда подтягивает роли и грузит память приложения, но гарантирует точную подгрузку ролей до окончания запросов
    //ленивая - подгружает юзера, а роли не сразу, но экономит память, но может не успеть подтянуть роли до окончания запроса
    @ManyToMany(fetch = FetchType.EAGER)
    //объясняем спрингу - для связей юзеров и ролей у нас есть таблица user_role
    //в ней колонка user_id ссылается на таблицу юзеров
    //в ней колонка role_id ссылается на таблицу ролей
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    //пустой конструктор, чтобы не забыть потом его добавить для Спринга
    public User(){

    }

    //getters and setters

    //исп-ся фреймворком Сприг-секьюрити для получения списка ролей нашего пользователя
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    //methods

    @Override
    public String toString() {
        return String.format("Пользователь: ИД - %d, логин - %s, почта - %s, активен - %s, роли - %s.",
                id, username, email, active ? "да" : "нет", roles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return active == user.active && Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(username);
        result = 31 * result + Objects.hashCode(password);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Boolean.hashCode(active);
        result = 31 * result + Objects.hashCode(roles);
        return result;
    }

    // временный метод для создания тестового зашифрованного пароля
    // public static void main(String[] args) {
    // System.out.println(new BCryptPasswordEncoder().encode("111"));
    // }
}
