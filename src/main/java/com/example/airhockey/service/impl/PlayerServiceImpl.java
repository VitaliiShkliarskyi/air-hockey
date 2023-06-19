package com.example.airhockey.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.example.airhockey.service.PlayerService;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class PlayerServiceImpl implements PlayerService {
    private final List<Channel> players = new ArrayList<>();
}
