package com.miro.widgetapi;

import com.miro.widgetapi.dto.CreateBaseWidgetRequest;
import com.miro.widgetapi.dto.UpdateBaseWidgetRequest;
import com.miro.widgetapi.dto.WidgetDto;
import com.miro.widgetapi.model.Widget;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BaseWidgetTest {

    public static final String WIDGETCONTROLLERPATH = "/widget";
    public Clock clock;
    public static final String WIDGET_ID = "widget-id";

    public WidgetDto generateWidgetDto(Integer zIndex) {
        return WidgetDto.builder()
                .id("widget-id")
                .x(10)
                .y(10)
                .zIndex(zIndex)
                .width(10)
                .height(10)
                .lastModificationDate(getCurrentInstant())
                .build();
    }


    public List<WidgetDto> generateWidgetDtoList(Integer size) {
        return IntStream.range(0, size)
                .mapToObj(this::generateWidgetDto)
                .collect(Collectors.toList());
    }

    public Widget generateWidget(Integer zIndex) {
        return Widget.builder()
                .id("widget-id")
                .x(10)
                .y(10)
                .z(zIndex)
                .width(10)
                .height(10)
                .lastModificationDate(getCurrentInstant())
                .build();
    }

    public Widget generateWidgetToSave(Integer zIndex) {
        return Widget.builder()
                .x(10)
                .y(10)
                .z(zIndex)
                .width(10)
                .height(10)
                .lastModificationDate(getCurrentInstant())
                .build();
    }

    public List<Widget> generateWidgetList(Integer size){
        return IntStream.range(0, size)
                .mapToObj(i -> generateWidget(i))
                .collect(Collectors.toList());
    }

    public CreateBaseWidgetRequest generateCreateWidgetRequestZIndexOne() {
        return generateCreateWidgetRequest(1);
    }

    public CreateBaseWidgetRequest generateCreateWidgetRequest(Integer zIndex) {
        return new CreateBaseWidgetRequest(10, 10, zIndex, 10, 10);
    }

    public UpdateBaseWidgetRequest generateUpdateWidgetRequest(Integer zIndex) {
        return UpdateBaseWidgetRequest.builder()
                .widgetId(WIDGET_ID)
                .x(10)
                .y(10)
                .zIndex(zIndex)
                .width(10)
                .height(10)
                .build();
    }

    public Instant getCurrentInstant() {
        String instantExpected = "2020-12-22T10:15:30Z";
        Clock clock = Clock.fixed(Instant.parse(instantExpected), ZoneId.of("UTC"));

        Instant instant = Instant.now(clock);

        return instant;
    }
}
