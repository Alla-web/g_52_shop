package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.dto.CustomerDto;
import de.aittr.g_52_shop.domain.entity.Customer;
import de.aittr.g_52_shop.domain.entity.Product;
import de.aittr.g_52_shop.exception_handling.exceptions.CustomerNotFoundException;
import de.aittr.g_52_shop.exception_handling.exceptions.CustomerValidationException;
import de.aittr.g_52_shop.exception_handling.exceptions.ProductNoFoundException;
import de.aittr.g_52_shop.repository.CustomerRepository;
import de.aittr.g_52_shop.service.interfaces.CustomerService;
import de.aittr.g_52_shop.service.mapping.CustomerMappingService;
import liquibase.exception.CustomChangeException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service //сообщает Spring, что этот класс является сервисом и
// тогда Spring поместит его в конструктор контроллера при старте приложения
public class CustomerServiceImpl implements CustomerService {

    //создаем поле репозитория и конструктор на него
    private final CustomerRepository repository;
    //создаём поле маппинга, для доступа к методам конвертации и добавл. его в конструктор
    private final CustomerMappingService mappingService;

    public CustomerServiceImpl(CustomerRepository repository, CustomerMappingService mappingService) {
        this.repository = repository;
        this.mappingService = mappingService;
    }

    //methods

    @Override
    public CustomerDto save(CustomerDto dto) {
        try {
            Customer entity = mappingService.mapDtoToEntity(dto);
            entity = repository.save(entity);
            return mappingService.mapEntityToDto(entity);
        } catch (Exception e){
            throw new CustomerValidationException(e);
        }
    }

    @Override
    public List<CustomerDto> getAllActiveCustomers() {
        return repository.findAll()
                .stream()
                .filter(Customer::isActive)
                .map(mappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public CustomerDto getById(Long id) {
//        Customer customer = repository.findById(id).orElse(null);
//
//        if (customer == null || !customer.isActive()){
//            return null;
//        }
//        return mappingService.mapEntityToDto(customer);

        //более короткая, но сложная для понимания запись
        return mappingService.mapEntityToDto(repository.findById(id)
                .filter(Customer::isActive)
                .orElseThrow(()-> new CustomerNotFoundException(id)));
    }

    @Override
    public void update(CustomerDto customer) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByName(String name) {

    }

    @Override
    public void restoreById(Long id) {

    }

    @Override
    public long getAllActiveCustomersCount() {
        return repository.findAll()
                .stream()
                .filter(Customer::isActive)
                .count();
    }

    @Override
    public BigDecimal getCustomersCartTotalCost(Long id) {
        return null;
    }

    @Override
    public BigDecimal getProductsAverageCost(Long id) {
        return null;
    }

    @Override
    public void addProductToCart(Long customerId, Long productId) {

    }

    @Override
    public void deleteProductFromCart(Long customerId, Long productId) {

    }

    @Override
    public void emptyCart(Long id) {

    }
}
