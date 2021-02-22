package com.miro.widgetapi.service;

import com.miro.widgetapi.BaseWidgetTest;
import com.miro.widgetapi.dto.*;
import com.miro.widgetapi.exception.WidgetNotFoundException;
import com.miro.widgetapi.model.Widget;
import com.miro.widgetapi.repository.WidgetRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WidgetServiceTest extends BaseWidgetTest {

    private WidgetService widgetService;

    private WidgetRepository widgetRepository;
    private DtoConverter<Widget, WidgetDto> widgetDtoConverter;
    private DtoConverter<Page<Widget>, WidgetListDto> widgetListDtoConverter;

    private final Instant now = getCurrentInstant();

    @Before
    public void setUp() {
        widgetRepository = mock(WidgetRepository.class);
        widgetDtoConverter = mock(WidgetDtoConverter.class);
        widgetListDtoConverter = mock(WidgetListDtoConverter.class);
        clock = mock(Clock.class);

        widgetService = new WidgetService(widgetRepository, widgetDtoConverter, widgetListDtoConverter, clock);
        when(clock.instant()).thenReturn(now);
    }

    @Test
    public void testCreateWidget_whenZIndexIsNullAndZIndexListIsEmpty_shouldReturnFirstZIndexedWidgetDto() {
        CreateBaseWidgetRequest createWidgetRequest = generateCreateWidgetRequest(null);
        Widget widget = generateWidget(0);
        WidgetDto expectedWidgetDto = generateWidgetDto(0);
        Widget widgetToSave = generateWidgetToSave(0);

        when(widgetRepository.findAllZIndex()).thenReturn(Lists.emptyList());
        when(widgetDtoConverter.convert(widget)).thenReturn(expectedWidgetDto);
        when(widgetRepository.save(widgetToSave)).thenReturn(widget);

        WidgetDto result = widgetService.createWidget(createWidgetRequest);

        assertEquals(result, expectedWidgetDto);

        verify(widgetRepository).findAllZIndex();
        verify(widgetDtoConverter).convert(widget);
        verify(widgetRepository).save(widgetToSave);
    }

    @Test
    public void testCreateWidget_whenZIndexIsNullAndZIndexListIsNotEmpty_shouldReturnNextPossibleZIndexedWidgetDto() {
        CreateBaseWidgetRequest createWidgetRequest = generateCreateWidgetRequest(null);
        Widget widget = generateWidget(2);
        WidgetDto expectedWidgetDto = generateWidgetDto(2);
        Widget widgetToSave = generateWidgetToSave(2);

        when(widgetRepository.findAllZIndex()).thenReturn(List.of(0, 1));
        when(widgetDtoConverter.convert(widget)).thenReturn(expectedWidgetDto);
        when(widgetRepository.save(widgetToSave)).thenReturn(widget);

        WidgetDto result = widgetService.createWidget(createWidgetRequest);

        assertEquals(result, expectedWidgetDto);

        verify(widgetRepository).findAllZIndex();
        verify(widgetDtoConverter).convert(widget);
        verify(widgetRepository).save(widgetToSave);
    }

    @Test
    public void testCreateWidget_whenZIndexNotContainsZIndexList_shouldReturnZIndexedWidgetDto() {
        CreateBaseWidgetRequest createWidgetRequest = generateCreateWidgetRequest(3);
        Widget widget = generateWidget(3);
        Widget widgetToSave = generateWidgetToSave(3);
        WidgetDto expectedWidgetDto = generateWidgetDto(3);

        when(widgetRepository.findAllZIndex()).thenReturn(List.of(0, 1, 2));
        when(widgetRepository.save(widgetToSave)).thenReturn(widget);
        when(widgetDtoConverter.convert(widget)).thenReturn(expectedWidgetDto);

        WidgetDto result = widgetService.createWidget(createWidgetRequest);

        assertEquals(result, expectedWidgetDto);

        verify(widgetRepository).findAllZIndex();
        verify(widgetDtoConverter).convert(widget);
        verify(widgetRepository).save(widgetToSave);
    }

    @Test
    public void testCreateWidget_whenZIndexContains_shouldReturnShiftedZIndexWidgetDto() {
        CreateBaseWidgetRequest createWidgetRequest = generateCreateWidgetRequest(1);
        Widget widget = generateWidget(3);
        Widget widgetToSave = generateWidgetToSave(3);
        WidgetDto expectedWidgetDto = generateWidgetDto(3);

        when(widgetRepository.findAllZIndex()).thenReturn(List.of(0, 1, 2));
        when(widgetRepository.save(widgetToSave)).thenReturn(widget);
        when(widgetDtoConverter.convert(widget)).thenReturn(expectedWidgetDto);

        WidgetDto result = widgetService.createWidget(createWidgetRequest);

        assertEquals(result, expectedWidgetDto);

        verify(widgetRepository).findAllZIndex();
        verify(widgetDtoConverter).convert(widget);
        verify(widgetRepository).save(widgetToSave);
    }

    @Test
    public void testUpdateWidget_whenZIndexIsNullAndZIndexListIsEmpty_shouldReturnFirstZIndexedWidgetDto() {
        UpdateBaseWidgetRequest updateWidgetRequest = generateUpdateWidgetRequest(null);
        Widget widget = generateWidget(0);
        WidgetDto expectedWidgetDto = generateWidgetDto(0);

        when(widgetRepository.existsById(WIDGET_ID)).thenReturn(Boolean.TRUE);
        when(widgetRepository.findAllZIndex()).thenReturn(Lists.emptyList());
        when(widgetRepository.save(widget)).thenReturn(widget);
        when(widgetDtoConverter.convert(widget)).thenReturn(expectedWidgetDto);


        WidgetDto result = widgetService.updateWidget(updateWidgetRequest);

        assertEquals(result, expectedWidgetDto);
        verify(widgetRepository).existsById(WIDGET_ID);
        verify(widgetRepository).findAllZIndex();
        verify(widgetRepository).save(widget);
        verify(widgetDtoConverter).convert(widget);
    }

    @Test
    public void testUpdateWidget_whenZIndexIsNullAndZIndexListIsNotEmpty_shouldReturnNextZIndexedWidgetDto() {
        UpdateBaseWidgetRequest updateWidgetRequest = generateUpdateWidgetRequest(null);
        Widget widget = generateWidget(1);
        WidgetDto expectedWidgetDto = generateWidgetDto(1);

        when(widgetRepository.existsById(WIDGET_ID)).thenReturn(Boolean.TRUE);
        when(widgetRepository.findAllZIndex()).thenReturn(List.of(0));
        when(widgetRepository.save(widget)).thenReturn(widget);
        when(widgetDtoConverter.convert(widget)).thenReturn(expectedWidgetDto);


        WidgetDto result = widgetService.updateWidget(updateWidgetRequest);

        assertEquals(result, expectedWidgetDto);
        verify(widgetRepository).existsById(WIDGET_ID);
        verify(widgetRepository).findAllZIndex();
        verify(widgetRepository).save(widget);
        verify(widgetDtoConverter).convert(widget);
    }

    @Test
    public void testUpdateWidget_whenZIndexIsNotNullAndNotContainZIndexList_shouldReturnNextZIndexedWidgetDto() {
        UpdateBaseWidgetRequest updateWidgetRequest = generateUpdateWidgetRequest(1);
        Widget widget = generateWidget(1);
        WidgetDto expectedWidgetDto = generateWidgetDto(1);

        when(widgetRepository.existsById(WIDGET_ID)).thenReturn(Boolean.TRUE);
        when(widgetRepository.findAllZIndex()).thenReturn(List.of(0));
        when(widgetRepository.save(widget)).thenReturn(widget);
        when(widgetDtoConverter.convert(widget)).thenReturn(expectedWidgetDto);


        WidgetDto result = widgetService.updateWidget(updateWidgetRequest);

        assertEquals(result, expectedWidgetDto);
        verify(widgetRepository).existsById(WIDGET_ID);
        verify(widgetRepository).findAllZIndex();
        verify(widgetRepository).save(widget);
        verify(widgetDtoConverter).convert(widget);
    }

    @Test
    public void testUpdateWidget_whenZIndexIsNotNullAndContainsZIndexList_shouldReturnNextZIndexedWidgetDto() {
        UpdateBaseWidgetRequest updateWidgetRequest = generateUpdateWidgetRequest(1);
        Widget widget = generateWidget(3);
        WidgetDto expectedWidgetDto = generateWidgetDto(3);

        when(widgetRepository.existsById(WIDGET_ID)).thenReturn(Boolean.TRUE);
        when(widgetRepository.findAllZIndex()).thenReturn(List.of(0, 1, 2));
        when(widgetRepository.save(widget)).thenReturn(widget);
        when(widgetDtoConverter.convert(widget)).thenReturn(expectedWidgetDto);


        WidgetDto result = widgetService.updateWidget(updateWidgetRequest);

        assertEquals(result, expectedWidgetDto);
        verify(widgetRepository).existsById(WIDGET_ID);
        verify(widgetRepository).findAllZIndex();
        verify(widgetRepository).save(widget);
        verify(widgetDtoConverter).convert(widget);
    }

    @Test(expected = WidgetNotFoundException.class)
    public void testUpdateWidget_whenWidgetIdDoesNotContain_shouldThrowWidgetNotFoundException() {
        UpdateBaseWidgetRequest updateWidgetRequest = generateUpdateWidgetRequest(1);

        when(widgetRepository.existsById(WIDGET_ID)).thenReturn(Boolean.FALSE);
        widgetService.updateWidget(updateWidgetRequest);

        verify(widgetRepository).existsById(WIDGET_ID);
        verifyNoInteractions(widgetRepository.save(any()));
    }

    @Test
    public void testGetWidget_whenWidgetIdExist_shouldReturnWidgetDto() {
        Widget widget = generateWidget(1);
        WidgetDto expectedWidgetDto = generateWidgetDto(1);

        when(widgetRepository.findById(WIDGET_ID)).thenReturn(Optional.of(widget));
        when(widgetDtoConverter.convert(widget)).thenReturn(expectedWidgetDto);

        WidgetDto result = widgetService.getWidget(WIDGET_ID);

        assertEquals(result, expectedWidgetDto);
        verify(widgetRepository).findById(WIDGET_ID);
        verify(widgetDtoConverter).convert(widget);
    }

    @Test(expected = WidgetNotFoundException.class)
    public void testGetWidget_whenWidgetIdDoesNotExist_shouldThrowWidgetNotFoundException() {
        when(widgetRepository.findById(WIDGET_ID)).thenReturn(Optional.empty());

        widgetService.getWidget(WIDGET_ID);

        verifyNoInteractions(widgetDtoConverter.convert(any()));
    }


    @Test
    public void testGetWidget_whenWidgetIdDoesNotExist_shouldReturnPaginatedWidgetList() {
        Pageable paging = PageRequest.of(0, 10);
        List<Widget> widgetList = generateWidgetList(20);

        Page<Widget> page = new PageImpl<>(widgetList.subList(0, 10), paging, widgetList.size());
        List<WidgetDto> widgetDtoList = generateWidgetDtoList(10);

        WidgetListDto expectedWidgetListDto = WidgetListDto.builder()
                .widgets(widgetDtoList)
                .currentPage(page.getNumber())
                .totalWidgets(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        when(widgetRepository.findAllByOrderByZDesc(paging)).thenReturn(page);
        when(widgetListDtoConverter.convert(page)).thenReturn(expectedWidgetListDto);

        WidgetListDto result = widgetService.getAllWidgets(0, 10);

        assertEquals(result, expectedWidgetListDto);

    }

}