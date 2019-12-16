package ru.otus.demen.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import ru.otus.demen.service.*;

@Configuration
@PropertySource("classpath:application.yml")
public class ServiceConfiguration {
    @Bean
    LocaleInfo localeInfo(@Value("${locale}") String locale) {
        return new LocaleInfo(locale);
    }

    @Bean
    public CsvFilenameService csvResourceService(LocaleInfo localeInfo,
                                                 @Value("${csv-basename}") String basename)
    {
        return new CsvFilenameServiceImpl(localeInfo.getLocale(), basename);
    }

    @Bean
    public TestProvider csvTestProvider(CsvFilenameService csvFilenameService) {
        return new CsvTestProvider(new ClassPathResource(csvFilenameService.getFilename()));
    }

    @Bean
    public LocalizedMessageService localizedMessageService(LocaleInfo localeInfo,
                                                           MessageSource messageSource)
    {
        return new LocalizedMessageServiceImpl(localeInfo.getLocale(), messageSource);
    }

    @Bean
    public IOService streamIOService() {
        return new StreamIOService(System.in, System.out);
    }
}
