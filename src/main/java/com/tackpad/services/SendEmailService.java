package com.tackpad.services;

import com.google.common.collect.Lists;
import it.ozimov.springboot.templating.mail.model.Email;
import it.ozimov.springboot.templating.mail.model.impl.EmailImpl;
import it.ozimov.springboot.templating.mail.service.EmailService;
import it.ozimov.springboot.templating.mail.service.exception.CannotSendEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Przemysław Żynis on 02.12.2016.
 */
@Component
public class SendEmailService extends BaseService {

    @Value("${local.server.url}")
    private String serverUrl;

    @Autowired
    public EmailService emailService;

    public void sendRegisterEmailConfirm(String emailAddress, String companyName, String tokenValue) throws
            UnsupportedEncodingException, CannotSendEmailException {

        //Defining the model object for the given Freemarker template
        final Map<String, Object> modelObject = new HashMap<>();
        modelObject.put("email", emailAddress);
        modelObject.put("link", serverUrl + "/tokens/value/" + tokenValue);

        final Email email = EmailImpl.builder()
                .from(new InternetAddress(emailAddress, "TackPad"))
                .to(Lists.newArrayList(new InternetAddress(emailAddress, companyName)))
                .subject("TackPad. Potwierdzanie adresu email")
                .body("")//Empty body
                .encoding(Charset.forName("UTF-8")).build();

                /*.body("Witamy. Niniejszy adres mail zostal uzyty przy rejestracji w serwisie.<br><br>\n" +
                        "Jesli nie rejestrowales(as) sie u nas, poprostu zignoruj tego maila.<br>\n" +
                        "Aby aktywowac zarejestrowane kont kliknij w ponizszy link<br>\n" +
                        "http://localhost:8080/tokens/value/" + tokenValue)
                .encoding(Charset.forName("UTF-8")).build();*/

        emailService.send(email, "complite_register_template.ftl", modelObject);
    }

    public void sendChangeEmailConfirm(String emailAddress, String companyName, String tokenValue) throws
            UnsupportedEncodingException, CannotSendEmailException {

        //Defining the model object for the given Freemarker template
        final Map<String, Object> modelObject = new HashMap<>();
        modelObject.put("email", emailAddress);
        modelObject.put("link", serverUrl + "/tokens/value/" + tokenValue);

        final Email email = EmailImpl.builder()
                .from(new InternetAddress("cicero@mala-tempora.currunt", "TackPad"))
                .to(Lists.newArrayList(new InternetAddress(emailAddress, companyName)))
                .subject("TackPad. Zmiana adresu email")
                .body("")//Empty body
                .encoding(Charset.forName("UTF-8")).build();

        emailService.send(email, "change_email_template.ftl", modelObject);
    }
}
