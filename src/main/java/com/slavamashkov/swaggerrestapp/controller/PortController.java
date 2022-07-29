package com.slavamashkov.swaggerrestapp.controller;

import com.slavamashkov.swaggerrestapp.model.entity.Port;
import com.slavamashkov.swaggerrestapp.service.impl.PortServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/ports", produces = "application/json")
@Api(value = "/ports", tags = {"Службы портов"})
public class PortController {
    private final PortServiceImpl portService;

    @Autowired
    public PortController(PortServiceImpl portService) {
        this.portService = portService;
    }

    @GetMapping
    @ApiOperation(
            value = "Получить сведения о портах",
            httpMethod = "GET",
            produces = "application/json",
            response = Port.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка")})
    public ResponseEntity<List<Port>> getAllPorts() {
        return portService.readAllPorts();
    }

    @GetMapping(value = "/{id}/capacity")
    @ApiOperation(
            value = "Получить текущую загрузку порта",
            httpMethod = "GET",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Порт не найден"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка")})
    public ResponseEntity<String> getPortCapacityInfo(@PathVariable(value = "id") final Long id) {
        return portService.readPortCapacityInfo(id);

    }
}
