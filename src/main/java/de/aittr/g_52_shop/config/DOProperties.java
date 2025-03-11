package de.aittr.g_52_shop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "do") //спринг прочитает все переменные
// окружения с этим префиксом и разместит их в поля нашего класса
public class DOProperties {

    //объекты этого класса будут бинами и Спринг на старте приложения
    // создаст объект этого класса, залезет в переменные окружения и
    // запишет из них данные в наши переменные. Т.к. этот объект бин - спринг
    // создаст и запишет объекты этого класса в контекст и это даст нам возможность
    // работать с ними из других классов

    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String region;


    //getters and setters
    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
