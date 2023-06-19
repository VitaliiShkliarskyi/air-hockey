package com.example.airhockey.config;

import com.example.airhockey.handler.GameHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    private final GameHandler gameHandler;

    @Override
    public void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast(new StringDecoder())
                .addLast(gameHandler);
    }
}
