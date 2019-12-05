package ru.otus.demen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Locale;

@RequiredArgsConstructor
public class CsvResourceServiceImpl implements CsvResourceService {
    private final Locale locale;
    private final String basename;

    @Override
    public Resource getResource() {
        return new ClassPathResource(basename + "_" + locale.toString() + ".csv");
    }
}
