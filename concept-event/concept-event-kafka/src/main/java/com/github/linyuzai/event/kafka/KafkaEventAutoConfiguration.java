package com.github.linyuzai.event.kafka;

import lombok.SneakyThrows;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.security.jaas.KafkaJaasLoginModuleInitializer;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(KafkaEventProperties.class)
@AutoConfigureBefore(KafkaAutoConfiguration.class)
public class KafkaEventAutoConfiguration {

    @Bean
    public KafkaEventEngine kafkaEventPublisherGroup(ConfigurableBeanFactory beanFactory,
                                                     KafkaEventProperties properties,
                                                     ObjectProvider<RecordMessageConverter> messageConverter,
                                                     List<KafkaEventConfigurer> configurers) {
        KafkaEventEngine engine = new KafkaEventEngine();
        for (Map.Entry<String, KafkaEventProperties.KafkaProperties> entry : properties.getKafka().entrySet()) {
            String key = entry.getKey();
            KafkaEventProperties.KafkaProperties value = entry.getValue();

            ProducerFactory<Object, Object> producerFactory = createProducerFactory(value);

            ProducerListener<Object, Object> producerListener = createProducerListener();

            KafkaTemplate<Object, Object> kafkaTemplate =
                    createKafkaTemplate(value, messageConverter, producerFactory, producerListener);

            ConsumerFactory<Object, Object> consumerFactory = createConsumerFactory(value);

            KafkaTransactionManager<Object, Object> kafkaTransactionManager =
                    registerKafkaTransactionManager(value, producerFactory);

            KafkaListenerContainerFactory<? extends MessageListenerContainer> kafkaListenerContainerFactory =
                    createKafkaListenerContainerFactory(value, consumerFactory, kafkaTransactionManager);

            KafkaAdmin kafkaAdmin = createKafkaAdmin(value);

            //registerKafkaJaasLoginModuleInitializer(key, value, beanFactory);

            KafkaEventEndpoint endpoint = new KafkaEventEndpoint();
            endpoint.setProducerFactory(producerFactory);
            endpoint.setProducerListener(producerListener);
            endpoint.setTemplate(kafkaTemplate);
            endpoint.setConsumerFactory(consumerFactory);
            endpoint.setTransactionManager(kafkaTransactionManager);
            endpoint.setListenerContainerFactory(kafkaListenerContainerFactory);
            endpoint.setAdmin(kafkaAdmin);
            endpoint.setEngine(engine);
            for (KafkaEventConfigurer configurer : configurers) {
                configurer.configureEndpoint(endpoint);
            }

            if (endpoint.getProducerFactory() != null) {
                beanFactory.registerSingleton(key + "ProducerFactory", endpoint.getProducerFactory());
            }
            if (endpoint.getProducerListener() != null) {
                beanFactory.registerSingleton(key + "ProducerListener", endpoint.getProducerListener());
            }
            if (endpoint.getTemplate() != null) {
                beanFactory.registerSingleton(key + "KafkaTemplate", endpoint.getTemplate());
            }
            if (endpoint.getConsumerFactory() != null) {
                beanFactory.registerSingleton(key + "ConsumerFactory", endpoint.getConsumerFactory());
            }
            if (endpoint.getTransactionManager() != null) {
                beanFactory.registerSingleton(key + "KafkaTransactionManager", endpoint.getTransactionManager());
            }
            if (endpoint.getListenerContainerFactory() != null) {
                beanFactory.registerSingleton(key + "KafkaListenerContainerFactory", endpoint.getListenerContainerFactory());
            }
            if (endpoint.getAdmin() != null) {
                beanFactory.registerSingleton(key + "KafkaAdmin", endpoint.getAdmin());
            }

            engine.add(endpoint);
            beanFactory.registerSingleton(key + "KafkaEventPublishEndpoint", endpoint);

        }
        for (KafkaEventConfigurer configurer : configurers) {
            configurer.configureEngine(engine);
        }
        return engine;
    }

    private ProducerFactory<Object, Object> createProducerFactory(
            KafkaEventProperties.KafkaProperties properties) {
        DefaultKafkaProducerFactory<Object, Object> producerFactory = new DefaultKafkaProducerFactory<>(
                properties.buildProducerProperties());
        String transactionIdPrefix = properties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            producerFactory.setTransactionIdPrefix(transactionIdPrefix);
        }
        return producerFactory;
    }

    private ProducerListener<Object, Object> createProducerListener() {
        return new LoggingProducerListener<>();
    }

    private KafkaTemplate<Object, Object> createKafkaTemplate(KafkaEventProperties.KafkaProperties properties,
                                                              ObjectProvider<RecordMessageConverter> messageConverter,
                                                              ProducerFactory<Object, Object> producerFactory,
                                                              ProducerListener<Object, Object> producerListener) {
        KafkaTemplate<Object, Object> template = new KafkaTemplate<>(producerFactory);
        messageConverter.ifUnique(template::setMessageConverter);
        template.setProducerListener(producerListener);
        template.setDefaultTopic(properties.getTemplate().getDefaultTopic());
        return template;
    }

    private ConsumerFactory<Object, Object> createConsumerFactory(
            KafkaEventProperties.KafkaProperties properties) {
        return new DefaultKafkaConsumerFactory<>(properties.buildConsumerProperties());
    }

    private KafkaListenerContainerFactory<? extends MessageListenerContainer> createKafkaListenerContainerFactory(
            KafkaEventProperties.KafkaProperties properties,
            ConsumerFactory<Object, Object> consumerFactory,
            KafkaTransactionManager<Object, Object> transactionManager) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> listenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        listenerContainerFactory.setConsumerFactory(consumerFactory);
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        KafkaEventProperties.KafkaProperties.Listener listenerProperties = properties.getListener();
        map.from(listenerProperties::getConcurrency).to(listenerContainerFactory::setConcurrency);
        ContainerProperties container = listenerContainerFactory.getContainerProperties();
        container.setTransactionManager(transactionManager);
        map.from(listenerProperties::getAckMode).to(container::setAckMode);
        map.from(listenerProperties::getClientId).to(container::setClientId);
        map.from(listenerProperties::getAckCount).to(container::setAckCount);
        map.from(listenerProperties::getAckTime).as(Duration::toMillis).to(container::setAckTime);
        map.from(listenerProperties::getPollTimeout).as(Duration::toMillis).to(container::setPollTimeout);
        map.from(listenerProperties::getNoPollThreshold).to(container::setNoPollThreshold);
        map.from(listenerProperties.getIdleBetweenPolls()).as(Duration::toMillis).to(container::setIdleBetweenPolls);
        map.from(listenerProperties::getIdleEventInterval).as(Duration::toMillis).to(container::setIdleEventInterval);
        map.from(listenerProperties::getMonitorInterval).as(Duration::getSeconds).as(Number::intValue)
                .to(container::setMonitorInterval);
        map.from(listenerProperties::getLogContainerConfig).to(container::setLogContainerConfig);
        map.from(listenerProperties::isOnlyLogRecordMetadata).to(container::setOnlyLogRecordMetadata);
        map.from(listenerProperties::isMissingTopicsFatal).to(container::setMissingTopicsFatal);
        return listenerContainerFactory;
    }

    private KafkaTransactionManager<Object, Object> registerKafkaTransactionManager(
            KafkaEventProperties.KafkaProperties properties,
            ProducerFactory<Object, Object> producerFactory) {
        if (StringUtils.hasText(properties.getProducer().getTransactionIdPrefix())) {
            return new KafkaTransactionManager<>(producerFactory);
        }
        return null;
    }

    private KafkaAdmin createKafkaAdmin(KafkaEventProperties.KafkaProperties properties) {
        KafkaAdmin admin = new KafkaAdmin(properties.buildAdminProperties());
        admin.setFatalIfBrokerNotAvailable(properties.getAdmin().isFailFast());
        return admin;
    }

    @SneakyThrows
    private void registerKafkaJaasLoginModuleInitializer(String key,
                                                         KafkaEventProperties.KafkaProperties properties,
                                                         ConfigurableBeanFactory beanFactory) {
        if (properties.getJaas().isEnabled()) {
            KafkaJaasLoginModuleInitializer jaas = new KafkaJaasLoginModuleInitializer();
            KafkaEventProperties.KafkaProperties.Jaas jaasProperties = properties.getJaas();
            if (jaasProperties.getControlFlag() != null) {
                jaas.setControlFlag(jaasProperties.getControlFlag());
            }
            if (jaasProperties.getLoginModule() != null) {
                jaas.setLoginModule(jaasProperties.getLoginModule());
            }
            jaas.setOptions(jaasProperties.getOptions());
            beanFactory.registerSingleton(key + "KafkaJaasLoginModuleInitializer", jaas);
        }
    }

    //ChainedKafkaTransactionManager
}
