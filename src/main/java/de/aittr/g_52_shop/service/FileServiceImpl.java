package de.aittr.g_52_shop.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import de.aittr.g_52_shop.service.interfaces.FileService;
import de.aittr.g_52_shop.service.interfaces.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    //клиент, который будет подключать к сервисам Дидж Оушен
    private final AmazonS3 client;
    private final ProductService productService;

    //constructor
    public FileServiceImpl(AmazonS3 client, ProductService productService) {
        this.client = client;
        this.productService = productService;
    }

    //метод, загружающий файл на дидж оушен и вернёт нам url нанего
    @Override
    public String upload(MultipartFile file, String productTitle) {
        try {
            //создали уникальное имя для файа
            String uniqueName = generateUniqueFileName(file);

            //ObjectMetadata - из амазоновской библиотеки. Объект со всеми meta-данными о
            //передаваемом файле - его тип
            ObjectMetadata metadata = new ObjectMetadata();
            //закладываем тип отправляемого файла
            metadata.setContentType(file.getContentType());

            //создаем объект запросана Дидж Оушен о добавлении туда новго файла
            PutObjectRequest request = new PutObjectRequest(
                    //пепедаем
                    // имя баккета на сервисе Дидж Оушен
                    // сгенерированное уникальное имя передаваемого файла
                    // поток ввода из файла
                    // объект с типом передаваемого файла
                    "shop-bucket", uniqueName, file.getInputStream(), metadata
                    // CannedAccessControlList.PublicRead - разрешаем ЧТЕНИЕ этого файла
                    // для всех, у кого есть ссылка на этот файл
            ).withCannedAcl(CannedAccessControlList.PublicRead);

            //отправка запроса
            client.putObject(request);

            //получение ссылки на отправленный файл
            //передаем
            //бакет-имя
            //гникальное имя
            String url = client.getUrl("shop-bucket", uniqueName).toString();

            //привязка загруженной картинки к продукту в БД
            productService.attachImage(url, productTitle);

            return url;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //метод генерации уникальных имён изображений товаров
    private String generateUniqueFileName(MultipartFile file){

        //получаем текущее имя файла
        //banana.picture.jpg - например
        String sourceFileName = file.getOriginalFilename();
        //вычисляем индекс последней точки, чтобы разделить имя на имя и раширение
        int dotIndex = sourceFileName.lastIndexOf(".");
        //banana.picture.jpg -> banana.picture
        String fileName = sourceFileName.substring(0, dotIndex);
        // banana.picture.jpg -> .jpg
        String extension = sourceFileName.substring(dotIndex);

        return String.format("%s-%s%s", fileName, UUID.randomUUID(), extension);
    }
}
