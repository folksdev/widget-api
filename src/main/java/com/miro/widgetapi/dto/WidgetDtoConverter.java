package com.miro.widgetapi.dto;

import com.miro.widgetapi.model.Widget;
import org.springframework.stereotype.Component;

@Component
public class WidgetDtoConverter implements DtoConverter<Widget, WidgetDto> {

    @Override
    public WidgetDto convert(final Widget model) {
        return WidgetDto.builder()
                .id(model.getId())
                .x(model.getX())
                .y(model.getY())
                .zIndex(model.getZ())
                .width(model.getHeight())
                .height(model.getHeight())
                .lastModificationDate(model.getLastModificationDate())
                .build();
    }
}
