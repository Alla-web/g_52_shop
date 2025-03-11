package de.aittr.g_52_shop.service.mapping;

import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.domain.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-11T15:44:52+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class ProductMappingServiceImpl implements ProductMappingService {

    @Override
    public ProductDto mapEntityToDto(Product entity) {
        if ( entity == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setId( entity.getId() );
        productDto.setTitle( entity.getTitle() );
        productDto.setPrice( entity.getPrice() );

        return productDto;
    }

    @Override
    public Product mapDtoToEntity(ProductDto dto) {
        if ( dto == null ) {
            return null;
        }

        Product product = new Product();

        product.setTitle( dto.getTitle() );
        product.setPrice( dto.getPrice() );

        product.setActive( true );

        return product;
    }
}
