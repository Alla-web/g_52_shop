package de.aittr.g_52_shop.service.mapping;

import de.aittr.g_52_shop.domain.dto.CartDto;
import de.aittr.g_52_shop.domain.dto.ProductDto;
import de.aittr.g_52_shop.domain.entity.Cart;
import de.aittr.g_52_shop.domain.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-11T15:44:53+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class CartMappingServiceImpl implements CartMappingService {

    @Autowired
    private ProductMappingService productMappingService;

    @Override
    public CartDto mapEntityToDto(Cart entity) {
        if ( entity == null ) {
            return null;
        }

        CartDto cartDto = new CartDto();

        cartDto.setId( entity.getId() );
        cartDto.setProducts( productListToProductDtoList( entity.getProducts() ) );

        return cartDto;
    }

    @Override
    public Cart mapDtoToEntity(CartDto dto) {
        if ( dto == null ) {
            return null;
        }

        Cart cart = new Cart();

        cart.setProducts( productDtoListToProductList( dto.getProducts() ) );

        return cart;
    }

    protected List<ProductDto> productListToProductDtoList(List<Product> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductDto> list1 = new ArrayList<ProductDto>( list.size() );
        for ( Product product : list ) {
            list1.add( productMappingService.mapEntityToDto( product ) );
        }

        return list1;
    }

    protected List<Product> productDtoListToProductList(List<ProductDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Product> list1 = new ArrayList<Product>( list.size() );
        for ( ProductDto productDto : list ) {
            list1.add( productMappingService.mapDtoToEntity( productDto ) );
        }

        return list1;
    }
}
