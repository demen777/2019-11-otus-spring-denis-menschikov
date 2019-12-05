package ru.otus.demen.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;

import java.util.Locale;

@Configuration
@PropertySource("classpath:application.properties")
public class ServiceConfiguration {
    @Bean
    public CsvFilenameService csvResourceService(@Value("${locale}") String locale,
                                                 @Value("${csv.basename}") String basename)
    {
        return new CsvFilenameServiceImpl(new Locale(locale), basename);
    }

    @Bean
    public TestProvider csvTestProvider(CsvFilenameService csvFilenameService) {
        return new CsvTestProvider(new ClassPathResource(csvFilenameService.getFilename()));
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

    @Bean
    IOService streamIOService() {
        return new StreamIOService(System.in, System.out);
    }
}
