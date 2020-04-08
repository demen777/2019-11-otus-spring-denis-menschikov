package ru.otus.demen.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;


@SpringBootTest
@DirtiesContext
class SecurityPriceConfigurationTest {
    @Autowired
    @Qualifier("currentPriceInputChannel")
    DirectChannel currentPriceInputChannel;

    @Autowired
    @Qualifier("currentPriceOutputChannel")
    DirectChannel currentPriceOutputChannel;

    @Autowired
    @Qualifier("inputYahooPriceProviderChannel")
    DirectChannel inputYahooPriceProviderChannel;

    @Test
    void router_toYahoo() {
        currentPriceOutputChannel.subscribe(mock(MessageHandler.class));
        inputYahooPriceProviderChannel.addInterceptor(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                assertThat(message).isNotNull();
                assertThat(message.getHeaders()).containsEntry("securityClassCode", "SPBXM");
                assertThat(message.getPayload()).isEqualTo("T");
                return message;
            }
        });
        currentPriceInputChannel.send(MessageBuilder.withPayload("T").build());
    }
}