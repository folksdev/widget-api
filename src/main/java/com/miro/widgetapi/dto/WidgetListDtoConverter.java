package com.miro.widgetapi.dto;

import com.miro.widgetapi.model.Widget;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WidgetListDtoConverter implements DtoConverter<Page<Widget>, WidgetListDto> {

    private final DtoConverter<Widget, WidgetDto> widgetDtoConverter;

    public WidgetListDtoConverter(DtoConverter<Widget, WidgetDto> widgetDtoConverter) {
        this.widgetDtoConverter = widgetDtoConverter;
    }

    @Override
    public WidgetListDto convert(Page<Widget> model) {

        List<WidgetDto> widgetDtoList = model.stream()
                .map(widgetDtoConverter::convert)
                .collect(Collectors.toList());

        return WidgetListDto.builder()
                .widgets(widgetDtoList)
                .currentPage(model.getNumber())
                .totalWidgets(model.getTotalElements())
                .totalPages(model.getTotalPages())
                .build();
    }
}
