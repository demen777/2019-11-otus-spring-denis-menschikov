package ru.otus.demen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public class LocalizedMessageServiceImpl implements LocalizedMessageService {
    private final Locale locale;
    private final MessageSource messageSource;

    @Override
    public String getMessage(String key) {
        return messageSource.getMessage(key, null, locale);
    }

    @Override
    public String getMessage(String key, Object[] params) {
        return messageSource.getMessage(key, params, locale);
    }
}
