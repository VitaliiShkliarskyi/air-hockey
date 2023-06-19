package com.example.airhockey.service.impl;

import com.example.airhockey.service.PlayerService;
import com.example.airhockey.service.SenderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@Data
public class SenderServiceImpl implements SenderService {

    private final PlayerService playerService;
    @Override
    public void broadcastMessage(Object msg) {
        playerService.getPlayers().forEach(channel -> channel.writeAndFlush(msg));
    }
}
