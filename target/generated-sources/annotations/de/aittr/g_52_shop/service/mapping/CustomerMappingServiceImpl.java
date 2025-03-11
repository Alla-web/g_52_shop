package de.aittr.g_52_shop.service.mapping;

import de.aittr.g_52_shop.domain.dto.CustomerDto;
import de.aittr.g_52_shop.domain.entity.Customer;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-11T15:44:54+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class CustomerMappingServiceImpl implements CustomerMappingService {

    @Autowired
    private CartMappingService cartMappingService;

    @Override
    public CustomerDto mapEntityToDto(Customer entity) {
        if ( entity == null ) {
            return null;
        }

        CustomerDto customerDto = new CustomerDto();

        customerDto.setId( entity.getId() );
        customerDto.setName( entity.getName() );
        customerDto.setCart( cartMappingService.mapEntityToDto( entity.getCart() ) );

        return customerDto;
    }

    @Override
    public Customer mapDtoToEntity(CustomerDto dto) {
        if ( dto == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setName( dto.getName() );
        customer.setCart( cartMappingService.mapDtoToEntity( dto.getCart() ) );

        customer.setActive( true );

        return customer;
    }
}
