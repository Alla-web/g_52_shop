package de.aittr.g_52_shop.repository;

import de.aittr.g_52_shop.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {


    //метод, позволяющий доставать роль по ее названию
    Optional<Role> findByTitle(String title);
}
