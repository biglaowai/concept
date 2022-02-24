package com.github.linyuzai.connection.loadbalance.core.discovery;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

@Getter
@AllArgsConstructor
public class DiscoveryMessage extends AbstractMessage {

    private final String host;

    private final int port;

    @Override
    public byte[] bytes() {
        return (host + ":" + port).getBytes(StandardCharsets.UTF_8);
    }
}
