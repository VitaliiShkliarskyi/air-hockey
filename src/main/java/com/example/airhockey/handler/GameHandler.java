package com.example.airhockey.handler;

import java.util.Map;
import com.example.airhockey.dto.Dto;
import com.example.airhockey.dto.MoveDto;
import com.example.airhockey.dto.StartGameDto;
import com.example.airhockey.model.Player;
import com.example.airhockey.service.GameService;
import com.example.airhockey.service.PlayerService;
import com.example.airhockey.service.SenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@ChannelHandler.Sharable
public class GameHandler extends SimpleChannelInboundHandler<String> {
    private final Map<String, BasicHandler> handlersMap;
    private final GameService gameService;
    private final PlayerService playerService;
    private final SenderService senderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameHandler(GameService gameService,
                       PlayerService playerService,
                       SenderService senderService,
                       @Qualifier(value = "handlersMap") Map<String, BasicHandler> handlersMap) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.senderService = senderService;
        this.handlersMap = handlersMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        playerService.getPlayers().add(channel);
        log.info("WebSocket Client connected channel={}", channel.id().asLongText());
        handlePlayerConnected(channel);
        log.info("Number of clients: {}", playerService.getPlayers().size());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String json) throws Exception {
        String clientId = ctx.channel().id().asLongText();
        Dto dto = objectMapper.readValue(json, Dto.class);
        dto.setClientId(clientId);
        BasicHandler basicHandler = handlersMap.get(dto.getClass().getSimpleName());
        basicHandler.handle(dto);
        if (dto instanceof StartGameDto startGameDto) {
            basicHandler.doHandle(startGameDto);
        } else if (dto instanceof MoveDto moveDto) {
            basicHandler.doHandle(moveDto);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        log.info("Client disconnected from channel {}", channel.id().asLongText());
        handlePlayerDisconnected(channel);
        log.info("Number of clients: {}", playerService.getPlayers().size());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        playerService.getPlayers().remove(ctx.channel());
        ctx.close();
    }

    private void handlePlayerConnected(Channel channel) {
        Player player = new Player();
        player.setId(channel.id().asLongText());
        gameService.addPlayerToTable(player);
        String out = "Player " + channel.id().asLongText() + "connected to server";
        senderService.broadcastMessage(out);
    }

    private void handlePlayerDisconnected(Channel channel) {
        String clientId = channel.id().asLongText();
        gameService.removePlayerFromTable(clientId);
        String out = "Player " + clientId + "disconnected from server";
        senderService.broadcastMessage(out);
    }
}
