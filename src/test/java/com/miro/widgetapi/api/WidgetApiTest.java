package com.miro.widgetapi.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miro.widgetapi.BaseWidgetTest;
import com.miro.widgetapi.dto.CreateBaseWidgetRequest;
import com.miro.widgetapi.dto.UpdateBaseWidgetRequest;
import com.miro.widgetapi.dto.WidgetDto;
import com.miro.widgetapi.dto.WidgetListDto;
import com.miro.widgetapi.exception.WidgetNotFoundException;
import com.miro.widgetapi.service.WidgetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(value = WidgetApi.class)
@DirtiesContext
@AutoConfigureMockMvc
public class WidgetApiTest extends BaseWidgetTest {

    @MockBean
    private WidgetService widgetService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateWidget_whenCreateWidgetRequestIsValid_shouldReturnCreatedWidgetWith201Created() throws Exception {

        CreateBaseWidgetRequest createWidgetRequest = generateCreateWidgetRequest(1);
        WidgetDto expectedWidgetDto = generateWidgetDto(1);

        when(widgetService.createWidget(createWidgetRequest)).thenReturn(expectedWidgetDto);

        ObjectMapper obj = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        obj.registerModule(module);

        this.mockMvc
                .perform(post(WIDGETCONTROLLERPATH).contentType(MediaType.APPLICATION_JSON).content(
                        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(createWidgetRequest)))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(obj.writeValueAsString(expectedWidgetDto), false)).andReturn();
    }

    @Test
    public void testCreateWidget_whenCreateWidgetRequestIsInvalid_shouldReturnBadRequestResponse() throws Exception{
        CreateBaseWidgetRequest createWidgetRequest = new CreateBaseWidgetRequest();

        this.mockMvc
                .perform(post(WIDGETCONTROLLERPATH).contentType(MediaType.APPLICATION_JSON).content(
                        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(createWidgetRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void testCreateWidget_whenWidthAndHeightHasNegativeValue_shouldReturnBadRequestResponse() throws Exception{
        CreateBaseWidgetRequest createWidgetRequest = new CreateBaseWidgetRequest(10,10,0,-10,-10);

        this.mockMvc
                .perform(post(WIDGETCONTROLLERPATH).contentType(MediaType.APPLICATION_JSON).content(
                        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(createWidgetRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void testUpdateWidget_whenUpdateWidgetRequestIsValid_shouldReturnUpdatedWidgetJsonWith202Accepted() throws Exception {
        UpdateBaseWidgetRequest updateWidgetRequest = generateUpdateWidgetRequest(1);
        WidgetDto expectedWidgetDto = generateWidgetDto(0);

        when(widgetService.updateWidget(updateWidgetRequest)).thenReturn(expectedWidgetDto);

        ObjectMapper obj = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        obj.registerModule(module);

        this.mockMvc
                .perform(put(WIDGETCONTROLLERPATH).contentType(MediaType.APPLICATION_JSON).content(
                        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(updateWidgetRequest)))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(obj.writeValueAsString(expectedWidgetDto), false)).andReturn();
    }

    @Test
    public void testUpdateWidget_whenProvidedWidgetIdDoesNotExist_shouldReturnNotFoundResponse() throws Exception {
        UpdateBaseWidgetRequest updateWidgetRequest = generateUpdateWidgetRequest(1);
        when(widgetService.updateWidget(updateWidgetRequest)).thenThrow(new WidgetNotFoundException("message"));

        ObjectMapper obj = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        obj.registerModule(module);

        this.mockMvc
                .perform(put(WIDGETCONTROLLERPATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(updateWidgetRequest)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testUpdateWidget_whenUpdateWidgetRequestIsInvalid_shouldReturnBadRequestResponse() throws Exception{
        UpdateBaseWidgetRequest updateWidgetRequest = new UpdateBaseWidgetRequest("widget-id");

        this.mockMvc
                .perform(put(WIDGETCONTROLLERPATH).contentType(MediaType.APPLICATION_JSON).content(
                        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(updateWidgetRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void testUpdateWidget_whenWidthAndHeightHasNegativeValue_itShouldReturnBadRequestResponse() throws Exception{
        UpdateBaseWidgetRequest updateWidgetRequest = new UpdateBaseWidgetRequest("widget-id", 10,10,0,-10,-10);

        this.mockMvc
                .perform(put(WIDGETCONTROLLERPATH).contentType(MediaType.APPLICATION_JSON).content(
                        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(updateWidgetRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void testGetWidget_whenProvidedWidgetIdExist_shouldReturnWidgetDto() throws Exception {
        WidgetDto expectedWidgetDto = generateWidgetDto(0);

        when(widgetService.getWidget("widget-id")).thenReturn(expectedWidgetDto);

        ObjectMapper obj = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        obj.registerModule(module);

        this.mockMvc
                .perform(get(WIDGETCONTROLLERPATH+ "/widget-id"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(obj.writeValueAsString(expectedWidgetDto), false))
                .andReturn();
    }

    @Test
    public void testGetWidget_whenProvidedWidgetIdDoesNotExist_shouldReturnNotFoundResponse() throws Exception {
        when(widgetService.getWidget("widget-id")).thenThrow(new WidgetNotFoundException("message"));

        this.mockMvc
                .perform(get(WIDGETCONTROLLERPATH + "/widget-id"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testGetAllWidget_shouldReturnAllWidgetListDto() throws Exception {
        List<WidgetDto> widgetDtoList = generateWidgetDtoList(10);
        WidgetListDto expectedResponse = WidgetListDto.builder()
                .widgets(widgetDtoList)
                .totalPages(1)
                .currentPage(0)
                .totalWidgets(10)
                .build();

        when(widgetService.getAllWidgets(0, 10)).thenReturn(expectedResponse);

        ObjectMapper obj = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        obj.registerModule(module);

        this.mockMvc
                .perform(get(WIDGETCONTROLLERPATH))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(obj.writeValueAsString(expectedResponse), false))
                .andReturn();
    }

    @Test
    public void testDeleteWidget_whenProvidedWidgetIdExist_shouldReturn204NoContent() throws Exception {
        doNothing().when(widgetService).deleteWidget("widget-id");

        ObjectMapper obj = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        obj.registerModule(module);

        this.mockMvc
                .perform(delete(WIDGETCONTROLLERPATH + "/widget-id"))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andReturn();
    }

    @Test
    public void testDeleteWidget_whenProvidedWidgetIdDoesNotExist_shouldReturnNotFoundResponse() throws Exception {

        doThrow(new WidgetNotFoundException("message")).when(widgetService).deleteWidget("widget-id");

        this.mockMvc
                .perform(delete(WIDGETCONTROLLERPATH + "/widget-id"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
