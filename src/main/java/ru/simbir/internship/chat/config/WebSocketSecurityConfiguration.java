package ru.simbir.internship.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;

@Configuration
public class WebSocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/app", "/chat").authenticated()
                .simpMessageDestMatchers("/app", "/chat").authenticated()
                .simpSubscribeDestMatchers("/app", "/chat").authenticated()
                .nullDestMatcher().authenticated()
                .simpTypeMatchers(MESSAGE, SUBSCRIBE).denyAll();
    }
}