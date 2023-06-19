package com.example.airhockey.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StartGameDto extends Dto {
    private String message;
}
