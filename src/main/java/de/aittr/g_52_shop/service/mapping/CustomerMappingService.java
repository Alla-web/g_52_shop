package de.aittr.g_52_shop.service.mapping;

import de.aittr.g_52_shop.domain.dto.CustomerDto;
import de.aittr.g_52_shop.domain.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CartMappingService.class)
public interface CustomerMappingService {

    //пустой конструктор есть по умолчанию

    //методы конвертации:
    CustomerDto mapEntityToDto(Customer entity);

    @Mapping(target = "id", ignore = true) //игнорируем поле id
    @Mapping(target = "active", constant = "true") //уст-м в поле active вегда true
    Customer mapDtoToEntity(CustomerDto dto);

}
