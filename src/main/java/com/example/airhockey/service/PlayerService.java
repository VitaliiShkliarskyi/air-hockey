package com.example.airhockey.service;

import io.netty.channel.Channel;
import java.util.List;

public interface PlayerService {
    List<Channel> getPlayers();
}
