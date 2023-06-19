package com.example.airhockey.handler;

import com.example.airhockey.dto.StartGameDto;
import com.example.airhockey.model.Table;
import com.example.airhockey.service.GameService;
import com.example.airhockey.service.SenderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class GameStartedHandler extends BasicHandler<StartGameDto> {
    private final GameService gameService;
    private final SenderService senderService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Table table = new Table();

    @Override
    public void doHandle(StartGameDto startGameDto) {
        String clientId = startGameDto.getClientId();
        String message = startGameDto.getMessage();
        log.info("Client {} started the game with message: {}", clientId, message);
        gameService.startGame();
        try {
            senderService.broadcastMessage(objectMapper.writeValueAsString(table));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can't covert " + table + "to JSON", e);
        }
    }
}
