package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.domain.entity.Product;
import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.exception_handling.exceptions.ProductNoFoundException;
import de.aittr.g_52_shop.exception_handling.exceptions.ProductValidationException;
import de.aittr.g_52_shop.repository.ProductRepository;
import de.aittr.g_52_shop.service.interfaces.ConfirmationService;
import de.aittr.g_52_shop.service.interfaces.ProductService;
import de.aittr.g_52_shop.service.mapping.ProductMappingService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/*
 Аннотация @Service говорит Spring, что нужно на страте приложения нужно создать
 объект этого класса и поместить его его в Спринг-контекст.
 Кроме того, данная аннотация носит информационный харакетер - говорит
 о том, что это класс сервиса
 */

@Service //сообщает Spring, что этот класс является сервисом и
// тогда Spring поместит его в конструктор контроллера при старте приложения
public class ProductServiceImpl implements ProductService {

    //создаем поле репозитория и конструктор на него
    private final ProductRepository repository;
    //будем вызывать, когда нам понадобится сконвертировать продуктк в dto и наоборот
    //также добавляем в констурктор для автоматического создания Spring bin-ов (объектов клаасса под управлением Spring)
    private final ProductMappingService mappingService;

    //объект логгера для осуществления логирования
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    /*
    Когда Спринг будет создавать объект сервиса продуктов, он вызовет этот конструктор
    (потому что других вариантов нет), а в этот конструктор требуется передать объект репозитория.
    Поэтому Спринг обратится в контекст, достанет оттуда репозиторий и передаст в этот параметр.
    А объект репозитория будет там уже находиться благодаря наследованию нашего интерфейса от
    класса JpaRepository<Product, Long> с указанием типизации
     */

    public ProductServiceImpl(ProductRepository repository, ProductMappingService mappingService) {
        this.repository = repository;
        this.mappingService = mappingService;
    }

    //methods

    @Override
    public ProductDto save(ProductDto dto) {
        try {
            Product entity = mappingService.mapDtoToEntity(dto);
            entity = repository.save(entity);
            System.out.println("******* Method save works *******");
            return mappingService.mapEntityToDto(entity);
        } catch (Exception e) {
            throw new ProductValidationException(e);
        }

    }

    @Override
    public List<ProductDto> getAllActiveProducts() {

        //logging process

        //при помощи разных методов логгирования мы можем фиксировать изменения на
        //на разных уровнях info, warn, error
        //на практике одно событие не нужно логировать на все уровни
//        logger.info("Request for all products received");
//        logger.warn("Request for all products received");
//        logger.error("Request for all products received");

        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .map(mappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public ProductDto getById(Long id) {
        //если продукт найдется - метод orElse вернет его,
        // если такого продукта нет - вернёт указанное нами занчение
//        Product product = repository.findById(id).orElse(null);
//        orElse - это метод класса Optional
//
//               if (product == null || !product.isActive()) {
//            throw new ProductNoFoundException(id);
//        }
//        return mappingService.mapEntityToDto(product);

        //более короткая, но сложная для понимания запись
        return mappingService.mapEntityToDto(repository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() -> new ProductNoFoundException(id)));
    }

    @Override
    @Transactional //говорит Спрингу, что нужно поддреживать трансакцию,
    // открытую в БД, до конца работы метода
    //Т.о. мы сохраняем наш объект в сосотоянии менеджд,
    // и все изменения с нашим объектом будут применены в БД
    public void update(ProductDto product) {
        //наименование остается неизменным
        //будем разрешать изменять только цену продукта

        //1-е -найти существующий в БД продукт по id, заложенному в пришедший продукт
        Long id = product.getId();

        Product existedProduct = repository.findById(product.getId())
                //метод Опшенал
                .filter(Product::isActive)
                .orElseThrow(() -> new ProductNoFoundException(id));

        //устанавливаем найденному продукту цену из пришедшего продукта
        existedProduct.setPrice(product.getPrice());
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByTitle(String title) {

    }

    @Override
    public void restoreById(Long id) {

    }

    @Override
    public long getAllActiveProductsCount() {
//        return repository.findAll()
//                .stream()
//                .filter(Product::isActive)
//                .count(); //возвращает long

//        return getAllActiveProducts().size();

        //метод без ненужной конвертации в getAllActiveProducts()
        return repository.findAll()
                .stream()
                .filter(Product::isActive)
                .count();
    }

    @Override
    public BigDecimal getAllActiveProductsTotalCost() {
        return null;
    }

    @Override
    public BigDecimal getAllActiveProductsAveragePrice() {
        return null;
    }

    @Transactional //стобы трансакцияне закрылась до конца выполнения метода и
    //изменился не только Джава-объект, а и объект в БД
    @Override
    public void attachImage(String imageUrl, String productTitle) {

        //находим продукт и сетим ему изображение
        repository.findByTitle(productTitle).
                orElseThrow(() -> new ProductNoFoundException(productTitle))
                .setImage(imageUrl);
    }

    //доступы к удалённому хранилищу фото (записали их в переменные окруженияж):
    // https://shop-bucket.fra1.digitaloceanspaces.com
    // fKu8t3B3geeXdTJksnMAqR8tCtIghvXKwkMdHNCXrWc - secret key
    // DO801MMWX3RLJ7YYDHQJ - access key
}
