package com.example.airhockey.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        visible = true,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MoveDto.class, name = "MoveDto"),
        @JsonSubTypes.Type(value = StartGameDto.class, name = "StartGameDto"),
})
public abstract class Dto {
    protected String type;
    protected String clientId;
}
