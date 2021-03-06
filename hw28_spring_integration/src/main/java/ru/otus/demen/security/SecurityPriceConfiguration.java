package ru.otus.demen.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import ru.otus.demen.security.model.Security;

@Configuration
public class SecurityPriceConfiguration {

    @Bean
    public IntegrationFlow toPriceProvider() {
        return IntegrationFlows.from("currentPriceInputChannel")
                .handle("securityService", "get")
                .transform(Message.class, message -> {
                    Security security = ((Security)(message.getPayload()));
                    return MessageBuilder.withPayload(security.getCode())
                            .copyHeaders(message.getHeaders())
                            .setHeader("securityClassCode", security.getSecurityClass().getCode())
                        .build();
                })
                .log()
                .route(routeToPriceProvider())
                .get();
    }

    @Bean
    public HeaderValueRouter routeToPriceProvider() {
        HeaderValueRouter router = new HeaderValueRouter("securityClassCode");
        router.setChannelMapping("SPBXM", "inputYahooPriceProviderChannel");
        router.setChannelMapping("TQBR", "inputFinamPriceProviderChannel");
        return router;
    }

    @Bean
    public IntegrationFlow yahooPriceProviderFlow() {
        return IntegrationFlows.from("inputYahooPriceProviderChannel")
                .handle("yahooPriceProvider", "getCurrentPrice")
                .channel("currentPriceOutputChannel")
                .get();
    }

    @Bean
    public IntegrationFlow finamPriceProviderFlow() {
        return IntegrationFlows.from("inputFinamPriceProviderChannel")
                .handle("finamPriceProvider", "getCurrentPrice")
                .channel("currentPriceOutputChannel")
                .get();
    }
}
