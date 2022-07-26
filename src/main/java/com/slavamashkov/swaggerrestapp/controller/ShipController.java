package com.slavamashkov.swaggerrestapp.controller;

import com.slavamashkov.swaggerrestapp.model.wrappers.ShipStatus;
import com.slavamashkov.swaggerrestapp.model.entity.CrewMember;
import com.slavamashkov.swaggerrestapp.model.entity.Ship;
import com.slavamashkov.swaggerrestapp.service.impl.ShipServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "ships", produces = "application/json")
@Api(value = "/ships", tags = {"Корабли"})
public class ShipController {
    private final ShipServiceImpl shipService;

    @Autowired
    public ShipController(ShipServiceImpl shipService) {
        this.shipService = shipService;
    }

    @GetMapping
    @ApiOperation(
            value = "Получить текущие корабли (с возможностью фильтрации)",
            httpMethod = "GET",
            produces = "application/json",
            response = Ship.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Некорректный статус корабля"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка")})
    public ResponseEntity<List<Ship>> getAllShips(
            @ApiParam(
                    value = "Статус (местоположение) корабля",
                    name = "status",
                    allowableValues = "SEA, PORT",
                    example = "PORT"
            )
            @RequestParam(value = "status", required = false) final String status
    ) {
        return shipService.readAllShips(status);
    }

    @GetMapping("/{id}/status")
    @ApiOperation(
            value = "Узнать статус корабля",
            httpMethod = "GET",
            produces = "application/json",
            response = ShipStatus.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Корабль не найден"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка")})
    public ResponseEntity<ShipStatus> getShipStatus(
            @ApiParam(
                    value = "id корабля",
                    name = "id",
                    required = true,
                    example = "1"
            )
            @PathVariable(value = "id") final Long id
    ) {
        return shipService.readShipStatus(id);
    }

    @PostMapping
    @ApiOperation(
            value = "Создать новый корабль",
            httpMethod = "POST",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Неверное имя корабля или id порта"),
            @ApiResponse(code = 404, message = "Порт не найден"),
            @ApiResponse(code = 422, message = "Порт заполнен"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка")})
    public ResponseEntity<String> postShip(
            @ApiParam(
                    value = "JSON-структура корабля",
                    name = "ship",
                    required = true
            )
            @RequestBody final Ship ship
    ) {
        return shipService.createShip(ship);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(
            value = "Утилизировать корабль",
            httpMethod = "DELETE",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Корабль не найден"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка")})
    public ResponseEntity<String> deleteShip(
            @ApiParam(
                    value = "id корабля",
                    name = "id",
                    required = true,
                    example = "12"
            )
            @PathVariable(value = "id") final Long id
    ) {
        return shipService.deleteShip(id);
    }

    @PutMapping("/{id}/status")
    @ApiOperation(
            value = "Изменить местоположение корабля",
            httpMethod = "PUT",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Неверный статус"),
            @ApiResponse(code = 404, message = "Корабль или порт не найдены"),
            @ApiResponse(code = 422, message = "Порт заполнен"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка")})
    public ResponseEntity<String> putShipStatus(
            @ApiParam(
                    value = "id корабля",
                    name = "id",
                    required = true,
                    example = "12"
            )
            @PathVariable(value = "id") final Long id,
            @ApiParam(
                    value = "id порта (необходимо передавать при заходе корабля в порт)",
                    name = "port_id",
                    example = "3"
            )
            @RequestParam(value = "port_id", required = false) final Long portId,
            @ApiParam(
                    value = "Новый статус корабля",
                    name = "new_status",
                    allowableValues = "SEA, PORT",
                    example = "SEA",
                    required = true
            )
            @RequestParam(value = "new_status", required = true) final ShipStatus status
    ) {
        return shipService.updateShipStatus(id, portId, status);
    }

    @ApiOperation(
            value = "Узнать состав команды корабля",
            httpMethod = "GET",
            produces = "application/json",
            response = CrewMember.class,
            responseContainer = "Set"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Корабль не найден"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка")})
    @GetMapping(value = "/{id}/crew")
    public ResponseEntity<Set<CrewMember>> getCrewMembers(
            @ApiParam(
                    value = "id корабля",
                    name = "id",
                    required = true,
                    example = "12"
            )
            @PathVariable(value = "id") final Long id
    ) {
        return shipService.readAllCrewMembersById(id);
    }
}
