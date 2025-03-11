package de.aittr.g_52_shop.repository;

import de.aittr.g_52_shop.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    //технология Spring пропишет сама основные CRUD-операции
}
