package ru.otus.demen.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

@Configuration
@PropertySource("classpath:application-prod.properties")
public class ServiceConfiguration {
    @Bean
    public CsvResourceService csvResourceService(@Value("${locale}") String locale,
                                                 @Value("${csv.basename}") String basename)
    {
        return new CsvResourceServiceImpl(new Locale(locale), basename);
    }

    @Bean
    public TestProvider csvTestProvider(CsvResourceService csvResourceService) {
        return new CsvTestProvider(csvResourceService.getResource());
    }

    @Bean
    public MessageSource messageSource(@Value("${message.basename}") String basename) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(basename);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalizedMessageService localizedMessageService(@Value("${locale}") String locale,
                                                           MessageSource messageSource)
    {
        return new LocalizedMessageServiceImpl(new Locale(locale), messageSource);
    }
}
