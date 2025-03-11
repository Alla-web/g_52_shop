package de.aittr.g_52_shop.security;

import de.aittr.g_52_shop.domain.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
//                    именно интерфейс Authentication
public class AuthInfo implements Authentication {

    //этот объект нужен самому фреймворку Спрингсекьюрити

    private boolean authentication;
    private String username;
    private Set<Role> roles;

    //конструктор на username и roles

    public AuthInfo(String username, Set<Role> roles) {
        this.username = username;
        this.roles = roles;
    }

    //methods

    @Override
    public String toString() {
        return String.format("Auth info: authorized - %s; name - %s; roles - %s",
                authentication, username, roles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthInfo authInfo = (AuthInfo) o;
        return authentication == authInfo.authentication && Objects.equals(username, authInfo.username) && Objects.equals(roles, authInfo.roles);
    }

    @Override
    public int hashCode() {
        int result = Boolean.hashCode(authentication);
        result = 31 * result + Objects.hashCode(username);
        result = 31 * result + Objects.hashCode(roles);
        return result;
    }

    //методы интерфейса Authentication

    @Override
    public String getName() {
        //мы не возвращаем имя юзера, т.к. мы не будем пользоваться реальным именем
        return username;
    }

    //метод вернёт роли, которые принадлежат юзеру
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    //метод вернёт имя юзера
    @Override
    public Object getPrincipal() {
        return username;
    }

    //вернёт статус юзера - авторизирован или нет
    @Override
    public boolean isAuthenticated() {
        return authentication;
    }

    //это по сути обычный сеттер, позволяющий установить статус авторизации
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authentication = isAuthenticated;
    }
}
