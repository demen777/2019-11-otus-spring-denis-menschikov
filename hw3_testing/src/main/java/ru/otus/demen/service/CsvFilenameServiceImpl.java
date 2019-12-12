package ru.otus.demen.service;

import lombok.RequiredArgsConstructor;

import java.util.Locale;

@RequiredArgsConstructor
public class CsvFilenameServiceImpl implements CsvFilenameService {
    private final Locale locale;
    private final String basename;

    @Override
    public String getFilename() {
        return basename + "_" + locale.toString() + ".csv";
    }
}
