package com.slavamashkov.swaggerrestapp.controller;

import com.slavamashkov.swaggerrestapp.model.wrappers.CrewMemberStatus;
import com.slavamashkov.swaggerrestapp.model.entity.CrewMember;
import com.slavamashkov.swaggerrestapp.service.impl.CrewMemberServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "crew", produces = "application/json")
@Api(value = "/crew", tags = {"Команда"})
public class CrewController {
    private final CrewMemberServiceImpl crewMemberService;

    @Autowired
    public CrewController(CrewMemberServiceImpl crewMemberService) {
        this.crewMemberService = crewMemberService;
    }

    @GetMapping
    @ApiOperation(
            value = "Получить всех моряков (с возможностью фильтрации)",
            httpMethod = "GET",
            produces = "application/json",
            response = CrewMember.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Некорректный статус моряка"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка")})
    public ResponseEntity<List<CrewMember>> getAllCrewMembers(
            @ApiParam(
                    value = "Статус (местоположение) моряка",
                    name = "status",
                    allowableValues = "ON_SHIP, ON_LAND",
                    example = "ON_SHIP"
            )
            @RequestParam(value = "status", required = false) final String status
    ) {
        return crewMemberService.readAllCrewMembers(status);
    }

    @GetMapping("/{id}/status")
    @ApiOperation(
            value = "Узнать статус моряка",
            httpMethod = "GET",
            produces = "application/json",
            response = CrewMemberStatus.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Моряк не найден"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка")})
    public ResponseEntity<CrewMemberStatus> getCrewMemberStatus(
            @ApiParam(
                    value = "id моряка",
                    name = "id",
                    required = true,
                    example = "1"
            )
            @PathVariable(value = "id") final Long id
    ) {
        return crewMemberService.readCrewMemberStatus(id);
    }

    @PutMapping("/{id}/status")
    @ApiOperation(
            value = "Назначить моряка на корабль / Снять моряка с корабля",
            httpMethod = "PUT",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Неверный статус"),
            @ApiResponse(code = 404, message = "Моряк или корабль не найдены"),
            @ApiResponse(code = 422, message = "На корабле нет места"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка")})
    public ResponseEntity<String> putCrewMemberStatus(
            @ApiParam(
                    value = "id моряка",
                    name = "id",
                    required = true,
                    example = "1"
            )
            @PathVariable(value = "id") final Long id,
            @ApiParam(
                    value = "id корабля (необходимо передавать при назначении моряка на корабль)",
                    name = "ship_id",
                    example = "3"
            )
            @RequestParam(value = "ship_id", required = false) final Long shipId,
            @ApiParam(
                    value = "Новый статус моряка",
                    name = "new_status",
                    allowableValues = "ON_SHIP, ON_LAND",
                    example = "ON_SHIP",
                    required = true
            )
            @RequestParam(value = "new_status", required = true) final CrewMemberStatus status
    ) {
        return crewMemberService.updateCrewMemberStatus(id, shipId, status);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(
            value = "Уволить моряка",
            httpMethod = "DELETE",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Моряк не найден"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка")})
    public ResponseEntity<String> deleteCrewMember(
            @ApiParam(
                    value = "id моряка",
                    name = "id",
                    required = true,
                    example = "1"
            )
            @PathVariable(value = "id") final Long id
    ) {
        return crewMemberService.deleteCrewMember(id);
    }
}
