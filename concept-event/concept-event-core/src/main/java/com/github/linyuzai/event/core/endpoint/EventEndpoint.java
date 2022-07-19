package com.github.linyuzai.event.core.endpoint;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.engine.EventEngine;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;

import java.lang.reflect.Type;
import java.util.Map;

public interface EventEndpoint {

    String getName();

    Map<Object, Object> getMetadata();

    void setMetadata(Map<Object, Object> metadata);

    EventEngine getEngine();

    void setEngine(EventEngine publisher);

    EventEncoder getEncoder();

    void setEncoder(EventEncoder encoder);

    EventDecoder getDecoder();

    void setDecoder(EventDecoder decoder);

    EventErrorHandler getErrorHandler();

    void setErrorHandler(EventErrorHandler errorHandler);

    EventPublisher getPublisher();

    void setPublisher(EventPublisher publisher);

    EventSubscriber getSubscriber();

    void setSubscriber(EventSubscriber subscriber);

    void publish(Object event, EventContext context);

    void subscribe(Type type, EventContext context);
}
