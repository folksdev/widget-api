package com.miro.widgetapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WidgetListDto {

    private List<WidgetDto> widgets;
    private int currentPage;
    private long totalWidgets;
    private int totalPages;
}
