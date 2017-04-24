package com.tackpad.services;

import com.sun.jersey.spi.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageByLocaleServiceImpl implements MessageByLocaleService {

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getMessage(String id) {
        return messageSource.getMessage(id,null, LocaleContextHolder.getLocale());
    }
}
