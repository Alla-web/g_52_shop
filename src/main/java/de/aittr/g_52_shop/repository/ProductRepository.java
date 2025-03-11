package de.aittr.g_52_shop.repository;

import de.aittr.g_52_shop.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//                                                      тип и тип id у Product
public interface ProductRepository extends JpaRepository<Product, Long> {

    //технология Spring пропишет сама основные CRUD-операции

    //метод, для получения из БД продукт по его наименованию
    Optional<Product> findByTitle(String title);
}
