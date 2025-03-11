package de.aittr.g_52_shop.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    //метод, который создаёт и возвращает объект
    // клиента при подключении к сервисам Диджитал оушен/Amazon
    @Bean
    public AmazonS3 doClient(DOProperties properties) {

        //создаем объект содержащий ключи доступа
        AWSCredentials credentials = new BasicAWSCredentials(
                properties.getAccessKey(),
                properties.getSecretKey()
        );

        //создаем объект содержащий ендпоинты - куда нужно обращаться
        AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration(
                properties.getEndpoint(),
                properties.getRegion()
        );

        //создаем объект билдера, который создаст объект клиентаю, при этом мы заклажываем
        // информацию
        // где находятся сервисы дидж оушен и какие реквизиты при этом нужно использовать,
        // чтобы авторизоваться
        AmazonS3ClientBuilder clientBuilder =
                AmazonS3ClientBuilder.standard()
                        .withEndpointConfiguration(endpointConfig)
                        .withCredentials(new AWSStaticCredentialsProvider(credentials));

        return clientBuilder.build();
    }

}
