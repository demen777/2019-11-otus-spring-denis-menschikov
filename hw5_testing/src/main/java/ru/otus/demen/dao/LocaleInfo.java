package ru.otus.demen.dao;

import lombok.Getter;

import java.util.Locale;

@Getter
public class LocaleInfo {
    public final Locale locale;

    public LocaleInfo(String locale) {
        this.locale = new Locale(locale);
    }
}
