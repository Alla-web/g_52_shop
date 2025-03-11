package de.aittr.g_52_shop.service.mapping;

import de.aittr.g_52_shop.domain.dto.CartDto;
import de.aittr.g_52_shop.domain.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ProductMappingService.class)
public interface CartMappingService {

    //пустой конструктор есть по умолчанию

    //методы конвертации:
    CartDto mapEntityToDto(Cart entity);

    @Mapping(target = "id", ignore = true) //игнорируем поле id
    Cart mapDtoToEntity(CartDto dto);

}
