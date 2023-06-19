package com.example.airhockey.config;

import com.example.airhockey.server.AirHockeyServer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServerCommandLineRunner implements CommandLineRunner {
    private final AirHockeyServer server;

    @Override
    public void run(String... args) throws Exception {
        server.start();
    }
}
