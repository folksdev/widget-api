package com.miro.widgetapi.dto;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class CreateBaseWidgetRequest extends BaseWidgetRequest {

    public CreateBaseWidgetRequest(Integer x, Integer y, Integer zIndex, Integer width, Integer height) {
        super(x, y, zIndex, width, height);
    }

    public CreateBaseWidgetRequest() {
        super();
    }

}


