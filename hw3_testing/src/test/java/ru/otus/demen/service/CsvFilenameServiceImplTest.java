package ru.otus.demen.service;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class CsvFilenameServiceImplTest {

    @Test
    void getFilename() {
        CsvFilenameServiceImpl csvFilenameService = new CsvFilenameServiceImpl(new Locale("ru_RU"), "tests");
        assertThat("tests_ru_RU.csv").isEqualToIgnoringCase(csvFilenameService.getFilename());
    }
}