package de.aittr.g_52_shop.exception_handling.exceptions;

import jakarta.validation.executable.ValidateOnExecution;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
Аннотация @ControllerAdvice говорит о том, что перед нами - адвайс,
глобальный обработчик ошибок, которые возникают во всём проекте.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    //в этом классе можно также расположить и логирование ошибок

    //спринг понимает, что этот метод именно для этой ошибки


    /*
    ПЛЮС -  мы создаём глобальный обработчик ошибок, который умеет ловить
            ошибки, возникающие во всем проекте и обрабатывать их в одном месте
    ПЛЮС -  логика обработки ошибок вынесена в отдельный класс, таким образом
            исходные методы содержат только чистую бизнес-логику, не
            нагруженную обработкой ошибок
    МИНУС - такой подход нам не подойдёт, если нам нужна разная логика обработки
            ошибок для разных контроллеров. В таком случае лучше воспользоваться
            первыми двумя способами
     */
    @ExceptionHandler(ProductNoFoundException.class)
    /*
    ResponseEntity - это специальный объект, внутрь которого мы можем
    заложить статус ответа, который получит наш клиент, а также
    любой объект, какой хотим, который будет отправлен клиенту.
    В данном случае помимо статуса мы в объект ResponseEntity закладываем
    ещё и объект своего Response, заложив в него сообщение об ошибке.
     */
    public ResponseEntity<Response> handleException(ProductNoFoundException e) {
        //создаем объект ответа и закладываем в него ошибку
        Response response = new Response(e.getMessage());
        //возвращаем это сообщение и статус ответа
        //т.о. мы получим в ответе статус и информативный ответ об ошибке для юзера
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    //обработчик исключения Не найден покупатель по id
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Response> handleException(CustomerNotFoundException e){
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ProductValidationException.class)
    public ResponseEntity<Response> handleException(ProductValidationException e) {

//        Response response = new Response(e.getMessage());

        Response response = new Response(cutMessage(e));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        //оставить только messageTemplate для юзера от всего этого вывода
        /*
        "message": "jakarta.validation.ConstraintViolationException:
        Validation failed for classes [de.aittr.g_52_shop.domain.entity.Product]
        during persist time for groups [jakarta.validation.groups.Default, ]
        \nList of constraint violations:[\n\tConstraintViolationImpl
        {interpolatedMessage='Product title should be at list thee characters
        length and start with capital letter', propertyPath=title,
        rootBeanClass=class de.aittr.g_52_shop.domain.entity.Product,
        messageTemplate='Product title should be at list thee characters
        length and start with capital letter'}\n]"
         */
    }

    //обработчик исключений для обработки исключения не корректного имени сохраняемого покупателя
    @ExceptionHandler
    public ResponseEntity<Response> handleException(CustomerValidationException e){
        Response response = new Response(cutMessage(e));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    private String cutMessage(Exception e) {
        String fullMessage = e.getMessage();

        String extractedMessage = "Error"; // значение по умолчанию
        int start = fullMessage.indexOf("messageTemplate='") +  17;
        int end = fullMessage.indexOf("'}", start);

        if(start > 16 && end > start){ // проверяем, что индексы найдены корректно
            extractedMessage = fullMessage.substring(start, end);
        }
        return  extractedMessage;
    }


}
