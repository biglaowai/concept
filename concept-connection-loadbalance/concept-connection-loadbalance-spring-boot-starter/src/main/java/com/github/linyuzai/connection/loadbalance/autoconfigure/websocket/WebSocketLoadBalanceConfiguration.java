package com.github.linyuzai.connection.loadbalance.autoconfigure.websocket;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventPublisher;
import com.github.linyuzai.connection.loadbalance.core.message.MessageFactory;
import com.github.linyuzai.connection.loadbalance.core.proxy.ConnectionProxy;
import com.github.linyuzai.connection.loadbalance.core.select.ConnectionSelector;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServerProvider;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.websocket.proxy.WebSocketConnectionProxy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class WebSocketLoadBalanceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ConnectionProxy connectionProxy() {
        return new WebSocketConnectionProxy();
    }

    @Bean
    @ConditionalOnMissingBean
    public ConnectionEventPublisher connectionEventPublisher(ApplicationEventPublisher publisher) {
        return new ApplicationConnectionEventPublisher(publisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public WebSocketLoadBalanceConcept webSocketLoadBalanceConcept(
            ConnectionServerProvider provider,
            ConnectionProxy proxy,
            List<ConnectionFactory> connectionFactories,
            List<ConnectionSelector> connectionSelectors,
            List<MessageFactory> messageFactories,
            ConnectionEventPublisher publisher) {
        return new WebSocketLoadBalanceConcept.Builder()
                .connectionServerProvider(provider)
                .connectionProxy(proxy)
                .addConnectionFactories(connectionFactories)
                .addConnectionSelectors(connectionSelectors)
                .addMessageFactories(messageFactories)
                .eventPublisher(publisher)
                .build();
    }
}
