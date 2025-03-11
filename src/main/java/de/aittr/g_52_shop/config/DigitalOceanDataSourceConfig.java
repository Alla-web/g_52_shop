package de.aittr.g_52_shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.BindParam;

import javax.sql.DataSource;

@Configuration
@Profile("fake-profile") //чтобы этот файл исп-ся только на Digital Ocean
//т.к. мы на локальном компьютере подключаемся через файл с пометкой dev
public class DigitalOceanDataSourceConfig {

    //прописываем все реквизиты доступа к БД и
    // добавляем аннотации к ним
    @Value("${DB_USERNAME}")
    private String username;

    @Value("${DB_PASSWORD}")
    private String password;

    @Value("${DB_HOST}")
    private String host;

    @Value("${DB_PORT}")
    private String port;

    @Value("${DB_NAME}")
    private String dbName;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    //создаем объект DataSource для подключения к БД на удалённом сервере
    //создаем для этого метод
    // DataSource импортируем из пакета javax
    //этот должен быть бином
    @Bean
    public DataSource dataSource(){
        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                //тоже берём из файла application.yaml
                .url("jdbc:postgresql://" + host + ":" + port + "/" + dbName)
                .username(username)
                .password(password)
                .build();
    }


}
