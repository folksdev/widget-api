package com.miro.widgetapi.service;

import com.miro.widgetapi.dto.*;
import com.miro.widgetapi.exception.WidgetNotFoundException;
import com.miro.widgetapi.model.Widget;
import com.miro.widgetapi.repository.WidgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
public class WidgetService {

    private static final Logger logger = LoggerFactory.getLogger(WidgetService.class);

    private final WidgetRepository widgetRepository;
    private final DtoConverter<Widget, WidgetDto> widgetDtoConverter;
    private final DtoConverter<Page<Widget>, WidgetListDto> widgetListDtoConverter;
    private final Clock clock;

    public WidgetService(WidgetRepository widgetRepository,
                         DtoConverter<Widget, WidgetDto> widgetDtoConverter,
                         DtoConverter<Page<Widget>, WidgetListDto> widgetListDtoConverter,
                         Clock clock) {
        this.widgetRepository = widgetRepository;
        this.widgetDtoConverter = widgetDtoConverter;
        this.widgetListDtoConverter = widgetListDtoConverter;
        this.clock = clock;
    }

    public WidgetDto createWidget(final CreateBaseWidgetRequest createWidgetRequest) {
        final Widget widget = Widget.builder()
                .x(createWidgetRequest.getX())
                .y(createWidgetRequest.getY())
                .z(findPossibleZIndex(createWidgetRequest.getZIndex()))
                .width(createWidgetRequest.getWidth())
                .height(createWidgetRequest.getHeight())
                .lastModificationDate(getCurrentInstant())
                .build();

        return widgetDtoConverter.convert(widgetRepository.save(widget));
    }

    @Transactional
    public WidgetDto updateWidget(final UpdateBaseWidgetRequest updateWidgetRequest) {
        if(!widgetRepository.existsById(updateWidgetRequest.getWidgetId())){
            logger.warn("Widget could not be found by requested widgetId: {}", updateWidgetRequest.getWidgetId());
            throw new WidgetNotFoundException("Widget could not be found by requested widgetId: " +
                    updateWidgetRequest.getWidgetId());
        }
        final Widget widget = Widget.builder()
                .id(updateWidgetRequest.getWidgetId())
                .height(updateWidgetRequest.getHeight())
                .width(updateWidgetRequest.getWidth())
                .x(updateWidgetRequest.getX())
                .y(updateWidgetRequest.getY())
                .z(findPossibleZIndex(updateWidgetRequest.getZIndex()))
                .lastModificationDate(getCurrentInstant())
                .build();
        return widgetDtoConverter.convert(widgetRepository.save(widget));
    }

    public void deleteWidget(final String widgetId) {
        Widget widget = getWidgetById(widgetId);
        widgetRepository.delete(widget);
    }

    public WidgetDto getWidget(final String widgetId) {
        return widgetDtoConverter.convert(getWidgetById(widgetId));
    }

    public WidgetListDto getAllWidgets(final Integer page, final Integer size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Widget> widgetList = widgetRepository.findAllByOrderByZDesc(paging);
        return widgetListDtoConverter.convert(widgetList);
    }

    private Widget getWidgetById(final String widgetId) {
        return widgetRepository.findById(widgetId)
                .orElseThrow(() -> {
                    logger.warn("Widget could not be found by requested widgetId: {}", widgetId);
                    return new WidgetNotFoundException("Widget could not be found by requested widgetId: " + widgetId);
                });
    }

    private Integer findPossibleZIndex(final Integer requestedZIndex) {
        List<Integer> zIndexList = widgetRepository.findAllZIndex();

        if (requestedZIndex == null) {
            if (zIndexList.isEmpty()){
                return 0;
            }
            return zIndexList.get(zIndexList.size() - 1) + 1;
        }

        return findNextZIndex(zIndexList, requestedZIndex);
    }

    private Integer findNextZIndex(List<Integer> zIndexList, Integer requestedZIndex){
        int newZIndex = requestedZIndex;
        while (zIndexList.contains(requestedZIndex)) {
            newZIndex = requestedZIndex + 1;
            requestedZIndex = newZIndex;
        }
        return newZIndex;
    }

    private Instant getCurrentInstant(){
        return clock.instant();
    }
}
