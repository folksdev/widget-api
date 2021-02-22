package com.miro.widgetapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class WidgetDto {

    private String id;

    private Integer x;

    private Integer y;

    private Integer zIndex;

    private Integer width;

    private Integer height;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant lastModificationDate;

}