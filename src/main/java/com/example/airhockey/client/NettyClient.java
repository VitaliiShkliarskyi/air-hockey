package com.example.airhockey.client;

import com.example.airhockey.dto.MoveDto;
import com.example.airhockey.dto.StartGameDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NettyClient {
    private final String host;
    private final int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void sendMessage() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                // ----Enter MoveDto -> 10,12 -> it will parse it to 10 and 12 (x=10, y=12)---
                System.out.print("Enter a message: ");
                String message = reader.readLine();
                String json = "";
                if (message.startsWith("start")) {
                    json = generateStartGameDto(message);
                } else {
                    String[] strings = message.split(",");
                    int x = Integer.parseInt(strings[0]);
                    int y = Integer.parseInt(strings[1]);
                    json = generateMoveDto(x, y);
                }
                // Send the message
                ByteBuf buf = future.channel().alloc().buffer();
                buf.writeBytes(json.getBytes());
                future.channel().writeAndFlush(buf);
                // ------------------------------------------------------------------
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }
    }

    private class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
            byte[] responseBytes = new byte[msg.readableBytes()];
            msg.readBytes(responseBytes);
            String response = new String(responseBytes);
            System.out.println("Received response from server: " + response);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int port = 8089;
        NettyClient client = new NettyClient(host, port);
        client.sendMessage();
    }

    public static String generateMoveDto(int x, int y) throws JsonProcessingException {
        MoveDto dto = new MoveDto();
        dto.setType("MoveDto");
        dto.setY(y);
        dto.setX(x);
        return new ObjectMapper().writeValueAsString(dto);
    }

    public static String generateStartGameDto(String message) throws JsonProcessingException {
        StartGameDto dto = new StartGameDto();
        dto.setType("StartGameDto");
        dto.setMessage(message);
        return new ObjectMapper().writeValueAsString(dto);
    }
}
