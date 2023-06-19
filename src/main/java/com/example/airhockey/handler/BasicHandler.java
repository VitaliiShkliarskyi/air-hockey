package com.example.airhockey.handler;

import com.example.airhockey.dto.Dto;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class BasicHandler<T extends Dto> {
    public void handle(T request) {
        log.info("Handling={}", request);
        doHandle(request);
    }
    public abstract void doHandle(T request);
}
