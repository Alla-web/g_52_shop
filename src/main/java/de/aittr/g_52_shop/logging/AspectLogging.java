package de.aittr.g_52_shop.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect //определяет, что этот класс является аспектом - классом, который
// содержит Advices - методы, который внедряются в стандартную бизнес-логику приложения
@Component //потому что это не сервис, но нужен бин на этот класс и
// его размещение в Спринг-контекст
public class AspectLogging {

    //объект логгера для осуществления логирования
    private Logger logger = LoggerFactory.getLogger(AspectLogging.class);

    //общий поинт-кат для всех методов продакт-сервиса
    @Pointcut("execution(* de.aittr.g_52_shop.service.ProductServiceImpl.*(..))")
    public void allProductServiceMethods() {}

    //логирование перед вызовом любого метода сервиса продуктов
    @Before("allProductServiceMethods()")
    public void beforeMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.info("Method {} called with arguments: {}", methodName, args);
    }

    //логирование после завершения любого метода сервиса продуктов
    @After("allProductServiceMethods()")
    public void afterMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Method {} finished execution", methodName);
    }

/////////////////////////////////////////////////////////////////////////////////////////////


    //описываем Поинт-кат - совокупность точек
    //@Pointcut - задаёт срез - правила, описывающих,
    //  куда именно будет внедряться дополнительный код
    //в () указываем ссылку на метод, в который нужно добавить наш сквозной код,
    //  и тип данных, который принимает этот метод
   // @Pointcut("execution(* de.aittr.g_52_shop.service.ProductServiceImpl.save(de.aittr.g_52_shop.domain.dto.ProductDto))")
   // public void saveProduct(){}

    //создаём Advise before
    //говорим, что этот эдвайс привязан к поинт-кату выше, т.к. мы указали его имя (имя метода)
   // @Before("saveProduct()") //этот метод является Advise before, т.е. будет срабатывать перед вызовом метода
    //JoinPoint - объект со всей информацией о вызываемом целевом методе, который создаётся фреймворком
   // public void beforeSavingProduct(JoinPoint joinPoint){
        //получаем инфо о аргументах, передаваемых в метод
   //     Object[] args = joinPoint.getArgs();
  //      logger.info("Method save of the class ProductServiceImpl called with argument {}", args[0]);
  //  }

//@After("saveProduct()")
  //  public void afterSavingProduct(){
        //эдвайс, который должен срабатывать после отработки метода save
 //       logger.info("Method save of the class ProductServiceImpl finished its work");
//    }


    //method getById
//    @Pointcut("execution(* de.aittr.g_52_shop.service.ProductServiceImpl.getById(Long))")
 //   public void getProductById(){}

 //   @AfterReturning(
//            pointcut = "getProductById()",
//            returning = "result"
//    )
    //когда наш метод что-то вернёт - перехвати его и положи это в переменную result
//    public void afterReturningProductById(Object result) {
//        logger.info("Method getById of the ProductServiceImpl successfully returned product: {}", result);
//    }

    //проверка эксепшенов
//    @AfterThrowing(
//            pointcut = "getProductById()",
//            throwing = "e"
//    )
//    public void afterThrowingExceptionWhileGettingProduct(Exception e) {
//        logger.warn("Method getById of the ProductServiceImpl threw an exception: {}", e.getMessage());
//    }

}
