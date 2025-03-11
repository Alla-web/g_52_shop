package de.aittr.g_52_shop.domain.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {
//implements GrantedAuthority - для использования методов Спринг-секьюрити
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    public Role(){

    }

    //getters and setters

    //исп-ся фреймворком Сприг-секьюрити для узнавания, как называется эта роль
    @Override
    public String getAuthority() {
        return title;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //methods

    @Override
    public String toString() {
        return String.format("User: id - %d, role - %s", id, title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(title, role.title);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(title);
        return result;
    }


}
