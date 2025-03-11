package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.exception_handling.exceptions.Response;
import de.aittr.g_52_shop.service.interfaces.FileService;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    //метод, принимающий саму картинку по чттп
    // и название продукта, для которого она предназначена
    @PostMapping
    public Response upload(
            @RequestParam MultipartFile file,
            @RequestParam String productTitle
            ) {
        String productImageUrl = fileService.upload(file, productTitle);
        return new Response("Saved image URL" + productImageUrl);
    }

}
