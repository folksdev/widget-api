package com.miro.widgetapi.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Getter
@SuperBuilder
public class UpdateBaseWidgetRequest extends BaseWidgetRequest {

    @NotNull(message = "WidgetId must not be empty")
    private String widgetId;

    public UpdateBaseWidgetRequest(String widgetId, Integer x, Integer y, Integer zIndex, Integer width, Integer height) {
        super(x, y, zIndex, width, height);
        this.widgetId = widgetId;
    }

    public UpdateBaseWidgetRequest(String widgetId) {
        super();
        this.widgetId = widgetId;
    }
}
