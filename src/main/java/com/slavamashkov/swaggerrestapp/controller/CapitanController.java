package com.slavamashkov.swaggerrestapp.controller;

import com.slavamashkov.swaggerrestapp.model.entity.CrewMember;
import com.slavamashkov.swaggerrestapp.model.wrappers.CrewMemberStatus;
import com.slavamashkov.swaggerrestapp.model.wrappers.ShipStatus;
import com.slavamashkov.swaggerrestapp.service.impl.CrewMemberServiceImpl;
import com.slavamashkov.swaggerrestapp.service.impl.ShipServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "capitan", produces = "application/json")
@Api(value = "/capitan", tags = {"Капитаны"})
public class CapitanController {
    private final CrewMemberServiceImpl crewMemberService;
    private final ShipServiceImpl shipService;

    @Autowired
    public CapitanController(CrewMemberServiceImpl crewMemberService, ShipServiceImpl shipService) {
        this.crewMemberService = crewMemberService;
        this.shipService = shipService;
    }

    // Может назначить только матроса (MATE)
    @PutMapping("crew/{id}/status")
    @ApiOperation(
            value = "Назначить матроса на корабль / Снять матроса с корабля",
            httpMethod = "PUT",
            produces = "application/json",
            response = String.class
    )
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

    @GetMapping("/{id}/status")
    @ApiOperation(
            value = "Узнать статус корабля",
            httpMethod = "GET",
            produces = "application/json",
            response = ShipStatus.class
    )
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

    @PutMapping("ship/{id}/status")
    @ApiOperation(
            value = "Изменить местоположение корабля",
            httpMethod = "PUT",
            produces = "application/json",
            response = String.class
    )
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
