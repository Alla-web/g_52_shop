package de.aittr.g_52_shop.service.mapping;

import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.domain.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
public interface ProductMappingService {

    //пустой конструктор есть по умолчанию

    //после генерации класса в generated-sources:
    //делаем автоматически созданный в generated-sources класс сервисом - @Mapper(componentModel = "spring") -
    //после этого в автоматически сгенерированном классе появится аннот. @Component - работате как
    // ан. @Service, но она цепляется к несервисным классам

    //методы можно автоматически генерировать при помощи mapstruct
    //mapstruct сам пропишет эти методы
    ProductDto mapEntityToDto(Product entity);

    //здесь нужно научить mapstruct делать продкут актоматически активным и
    // не брать id из dto, а игнорировать это поле
    //делаем это при помощи аннотаций:

    @Mapping(target = "id", ignore = true) //игнорируем поле id
    @Mapping(target = "active", constant = "true") //уст-м в поле active вегда true
    Product mapDtoToEntity(ProductDto dto);

    //ВАРИАНТ РУЧНОГО КОДА, класс при этом не интерфейс и аннотация @Service

//    public ProductDto mapEntityToDto(Product entity) {
//        ProductDto dto = new ProductDto();
//        dto.setId(entity.getId());
//        dto.setTitle(entity.getTitle());
//        dto.setPrice(entity.getPrice());
//        return dto;
//    }

//    public  Product mapDtoToEntity(ProductDto dto){
//        Product entity = new Product();
//        entity.setId(dto.getId());
//        entity.setTitle(dto.getTitle());
//        entity.setPrice(dto.getPrice());
//        //в объекте dto нет свойства Active, но мы же знаем, что все
//        // объекты, которые мы сохраняем по умолчанию активны
//        entity.setActive(true);
//        return entity;
//    }

}
