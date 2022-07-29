package com.slavamashkov.swaggerrestapp.controller;

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
@RequestMapping(value = "guard", produces = "application/json")
@Api(value = "/guard", tags = {"Береговая охрана"})
public class CoastGuardController {
    private final ShipServiceImpl shipService;

    @Autowired
    public CoastGuardController(ShipServiceImpl shipService) {
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
