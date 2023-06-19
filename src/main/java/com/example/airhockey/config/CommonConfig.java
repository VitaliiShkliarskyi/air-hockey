package com.example.airhockey.config;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import com.example.airhockey.handler.BasicHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CommonConfig {
    private final Map<String, BasicHandler> basicHandlerMap;

    @Bean
    public Map<String, BasicHandler> handlersMap() {
        Map<String, BasicHandler> subscriberMap = new HashMap<>();
        for (BasicHandler handler : basicHandlerMap.values()) {
            Type[] actualTypeArguments = ((ParameterizedType) handler.getClass().getGenericSuperclass()).getActualTypeArguments();
            if (actualTypeArguments.length == 1) {
                Class actualTypeArgument = (Class) actualTypeArguments[0];
                subscriberMap.putIfAbsent(actualTypeArgument.getSimpleName(), handler);
            }
        }
        return subscriberMap;
    }
}
