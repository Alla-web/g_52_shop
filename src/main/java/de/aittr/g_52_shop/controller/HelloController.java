package de.aittr.g_52_shop.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    private final String hello;

    //constructor
    public HelloController(@Value("${messages.hello}") String hello) {
        this.hello = hello;
    }

    //метод вывода hello
    @GetMapping
    public String sayHello(){
        return hello;
    }
}
