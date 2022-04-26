package com.github.linyuzai.connection.loadbalance.websocket.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.decode.TextMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.JacksonTextMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionFactory;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.NonNull;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;

import java.util.Map;

public class ReactiveWebSocketConnectionFactory extends WebSocketConnectionFactory<ReactiveWebSocketConnection> {

    @Override
    public boolean support(Object o, Map<Object, Object> metadata) {
        return o instanceof Object[] &&
                ((Object[]) o).length == 2 &&
                ((Object[]) o)[0] instanceof WebSocketSession &&
                ((Object[]) o)[1] instanceof FluxSink;
    }

    @Override
    public ReactiveWebSocketConnection doCreate(Object o, Map<Object, Object> metadata) {
        WebSocketSession session = (WebSocketSession) ((Object[]) o)[0];
        @SuppressWarnings("unchecked")
        FluxSink<WebSocketMessage> sender = (FluxSink<WebSocketMessage>) ((Object[]) o)[1];
        return new ReactiveWebSocketConnection(session, sender, Connection.Type.CLIENT, metadata);
    }
}
