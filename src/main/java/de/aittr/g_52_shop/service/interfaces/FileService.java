package de.aittr.g_52_shop.service.interfaces;


import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    //метод, загружающий файл на дидж оушен и вернёт нам url нанего
    String upload(MultipartFile file, String productTitle);
}
