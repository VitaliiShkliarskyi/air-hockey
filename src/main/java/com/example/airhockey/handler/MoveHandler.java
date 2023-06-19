package com.example.airhockey.handler;

import com.example.airhockey.dto.MoveDto;
import com.example.airhockey.model.GameStatus;
import com.example.airhockey.service.GameEngine;
import com.example.airhockey.service.GameService;
import com.example.airhockey.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class MoveHandler extends BasicHandler<MoveDto> {
    private final GameService gameService;
    private final GameEngine gameEngine;
    private final SenderService senderService;

    @Override
    public void doHandle(MoveDto moveDto) {
        String clientId = moveDto.getClientId();
        log.info("Client {} changed position on X:{}, Y:{}",
                clientId, moveDto.getX(), moveDto.getY());
        if (gameService.getGameSession().getStatus() == GameStatus.IN_PROCESS) {
            gameEngine.updatePlayerPosition(moveDto, clientId);
            senderService.broadcastMessage(gameService.getGameSession());
            gameEngine.checkCollisionWithPuck(clientId);
            senderService.broadcastMessage(gameService.getGameSession());
            gameEngine.checkCollisionWithWall(clientId);
            senderService.broadcastMessage(gameService.getGameSession());
        }
    }
}
