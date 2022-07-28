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
@RequestMapping(value = "capitan", produces = "application/json")
@Api(value = "/capitan", tags = {"Капитан"})
public class CapitanController {
    private final CrewMemberServiceImpl crewMemberService;

    @Autowired
    public CapitanController(CrewMemberServiceImpl crewMemberService) {
        this.crewMemberService = crewMemberService;
    }

    // Может назначить только матроса (MATE)
    @PutMapping("/{id}/status")
    @ApiOperation(
            value = "Назначить матроса на корабль / Снять матроса с корабля",
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
                    value = "id матроса (MATE)",
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
}
