package com.miro.widgetapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseWidgetRequest {

    @NotNull(message = "X must not be empty")
    private Integer x;

    @NotNull(message = "Y must not be empty")
    private Integer y;

    private Integer zIndex;

    @NotNull(message = "Width must not be empty")
    @Min(value = 0, message = "Width  must be greater than 0")
    private Integer width;

    @NotNull(message = "Height must not be empty")
    @Min(value = 0, message = "Height value must be greater than 0")
    private Integer height;
}
