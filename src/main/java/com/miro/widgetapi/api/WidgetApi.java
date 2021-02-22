package com.miro.widgetapi.api;

import com.miro.widgetapi.dto.CreateBaseWidgetRequest;
import com.miro.widgetapi.dto.UpdateBaseWidgetRequest;
import com.miro.widgetapi.dto.WidgetDto;
import com.miro.widgetapi.dto.WidgetListDto;
import com.miro.widgetapi.service.WidgetService;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/widget")
@Api("This Widget API provides to CRUD apis widgets.")
public class WidgetApi {

    private final WidgetService widgetService;

    public WidgetApi(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @PostMapping
    @ApiOperation(value = "Create A Widget", produces = "Application/JSON", response = WidgetDto.class, httpMethod = "POST")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Widget Created Successfully"),
            @ApiResponse(code = 400, message = "Invalid Widget Request") })
    public ResponseEntity<WidgetDto> createWidget(
            @ApiParam(value = "A Widget creates by CreateWidgetRequest. It must has x, y, z(not required), width, height values.", required = true, type = "CreateWidgetRequest")
            @Valid @RequestBody CreateBaseWidgetRequest createWidgetRequest) {
        WidgetDto createdWidget = widgetService.createWidget(createWidgetRequest);
        URI location = URI.create(String.format("/widget/%s",createdWidget.getId()));
        return ResponseEntity.created(location).body(createdWidget);
    }

    @PutMapping
    @ApiOperation(value = "Update A Widget", produces = "Application/JSON", response = WidgetDto.class, httpMethod = "PUT")
    @ApiResponses(value = { @ApiResponse(code = 202, message = "Widget Updated Successfully"),
            @ApiResponse(code = 404, message = "Widget could not be found"),
            @ApiResponse(code = 400, message = "Invalid Update Widget Request") })
    public ResponseEntity<WidgetDto> updateWidget(
            @ApiParam(value = "A Widget updates by UpdateWidgetRequest. It must has widget-id, x, y, z(not required), width, height values.", required = true, type = "UpdateWidgetRequest")
            @Valid @RequestBody UpdateBaseWidgetRequest updateWidgetRequest) {
        return ResponseEntity.accepted().body(widgetService.updateWidget(updateWidgetRequest));
    }

    @DeleteMapping("/{widgetId}")
    @ApiOperation(value = "Delete A Widget by widgetId", produces = "Application/JSON", httpMethod = "DELETE")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "Widget Deleted Successfully"),
            @ApiResponse(code = 404, message = "Widget could not be found") })
    public ResponseEntity<Void> deleteWidget(
            @ApiParam(value = "A widget deletes by widgetId", required = true, type = "String")
            @PathVariable("widgetId") String widgetId) {
        widgetService.deleteWidget(widgetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{widgetId}")
    @ApiOperation(value = "Find A Widget by widgetId", produces = "Application/JSON", response = WidgetDto.class, httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Widget Found Successfully by id"),
            @ApiResponse(code = 404, message = "Widget could not be found") })
    public ResponseEntity<WidgetDto> getWidget(
            @ApiParam(value = "A widget finds by valid widget id", required = true, type = "String")
            @PathVariable("widgetId") String widgetId) {
        return ResponseEntity.ok(widgetService.getWidget(widgetId));
    }

    @GetMapping
    @ApiOperation(value = "Get all widgets", produces = "Application/JSON", response = WidgetDto.class, httpMethod = "GET")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "All Widgets Found Successfully") })
    public ResponseEntity<WidgetListDto> getAllWidgets(@RequestParam(defaultValue = "0") Integer page,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(widgetService.getAllWidgets(page, size));
    }

}
