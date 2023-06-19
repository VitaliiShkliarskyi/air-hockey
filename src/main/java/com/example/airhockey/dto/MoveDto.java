package com.example.airhockey.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MoveDto extends Dto {
    private double x;
    private double y;
}
