package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.service.interfaces.ConfirmationService;
import de.aittr.g_52_shop.service.interfaces.EmailService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    //сущность для отправки имейлов
    private final JavaMailSender sender;

    //обязательно из класса freemarker
    //делаем специальные настройки имейла
    private final Configuration mailConfig;

    //сервис генерации кодов подтверждения
    private final ConfirmationService confirmationService;

    //constructor

    //нужно указать кодировку, при помощи которой будем отправлять письма
    //при помощи конфигуратора должны сказать, где лежат наши ресурсы, связанные с почтой
    public EmailServiceImpl(JavaMailSender sender, Configuration mailConfig, ConfirmationService confirmationService) {
        this.sender = sender;
        this.mailConfig = mailConfig;
        this.confirmationService = confirmationService;

        //задаём кодировку
        mailConfig.setDefaultEncoding("UTF-8");

        //указываем, где хранятся образцы наших писем
        //настроили
        //new ClassTemplateLoader(EmailServiceImpl.class - использовать объект класса ClassTemplateLoader
        //"/mail/" - подтягивать образцы из этой папки
        mailConfig.setTemplateLoader(new ClassTemplateLoader(EmailServiceImpl.class, "/mail/"));
    }

    //methods
    @Override
    public void sendConfirmationEmail(User user) {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        String text = generateConfirmationEmail(user);

        try {
            helper.setFrom("umnyj.start@gmail.com");
            helper.setTo(user.getEmail());
            helper.setSubject("Registration");
            helper.setText(text, true);

            sender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateConfirmationEmail(User user) {
        try {
            Template template = mailConfig.getTemplate("confirm_reg_mail.ftlh");
            String code = confirmationService.generateConfirmationCode(user);

            Map<String, Object> params = new HashMap<>();
            params.put("name", user.getUsername());
            params.put("link", "http://localhost:8080/register/" + code);

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
