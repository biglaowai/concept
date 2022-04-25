package com.github.linyuzai.connection.loadbalance.websocket.javax;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.function.Consumer;

@Getter
@NoArgsConstructor
public class JavaxWebSocketConnectionSubscriber extends WebSocketConnectionSubscriber {

    private Class<?> clientClass = JavaxWebSocketSubscriberEndpoint.class;

    public JavaxWebSocketConnectionSubscriber(String protocol) {
        super(protocol);
    }

    public JavaxWebSocketConnectionSubscriber(Class<?> clientClass) {
        this.clientClass = clientClass;
    }

    public JavaxWebSocketConnectionSubscriber(String protocol, Class<?> clientClass) {
        super(protocol);
        this.clientClass = clientClass;
    }

    @SneakyThrows
    @Override
    public void doSubscribe(ConnectionServer server, WebSocketLoadBalanceConcept concept, Consumer<Connection> consumer) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = getUri(server);
        Session session = container.connectToServer(clientClass, uri);
        JavaxWebSocketConnection connection = new JavaxWebSocketConnection(session, Connection.Type.SUBSCRIBER);
        connection.getMetadata().put(ConnectionServer.class, server);
        setDefaultMessageEncoder(connection);
        setDefaultMessageDecoder(connection);
        consumer.accept(connection);
    }

    @Override
    public String getType() {
        return "javax";
    }
}