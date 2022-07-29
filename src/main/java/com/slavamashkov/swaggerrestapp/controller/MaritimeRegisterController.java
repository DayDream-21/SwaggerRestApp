package com.slavamashkov.swaggerrestapp.controller;

import com.slavamashkov.swaggerrestapp.model.entity.Ship;
import com.slavamashkov.swaggerrestapp.model.wrappers.CrewMemberStatus;
import com.slavamashkov.swaggerrestapp.model.entity.CrewMember;
import com.slavamashkov.swaggerrestapp.model.wrappers.NewCrewMember;
import com.slavamashkov.swaggerrestapp.model.wrappers.NewShip;
import com.slavamashkov.swaggerrestapp.service.impl.CrewMemberServiceImpl;
import com.slavamashkov.swaggerrestapp.service.impl.ShipServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "register", produces = "application/json")
@Api(value = "/register", tags = {"Морской регистр"})
public class MaritimeRegisterController {
    private final CrewMemberServiceImpl crewMemberService;
    private final ShipServiceImpl shipService;

    @Autowired
    public MaritimeRegisterController(CrewMemberServiceImpl crewMemberService,
                                      ShipServiceImpl shipService
    ) {
        this.crewMemberService = crewMemberService;
        this.shipService = shipService;
    }

    @GetMapping("/crew")
    @ApiOperation(
            value = "Получить всех моряков (с возможностью фильтрации)",
            httpMethod = "GET",
            produces = "application/json",
            response = CrewMember.class,
            responseContainer = "List"
    )
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

    @GetMapping("/ships")
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

    // Может назначить только капитана (CAPITAN)
    @PutMapping("/{id}/status")
    @ApiOperation(
            value = "Назначить капитана на корабль / Снять капитана с корабля",
            httpMethod = "PUT",
            produces = "application/json",
            response = String.class
    )
    public ResponseEntity<String> putCrewMemberStatus(
            @ApiParam(
                    value = "id капитана (CAPITAN)",
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

    @PostMapping("/newShip")
    @ApiOperation(
            value = "Создать новый корабль",
            httpMethod = "POST",
            produces = "application/json",
            response = String.class
    )
    public ResponseEntity<String> postShip(
            @ApiParam(
                    value = "JSON-структура проекта корабля",
                    name = "ship",
                    required = true
            )
            @RequestBody final NewShip newShip
    ) {
        return shipService.createShip(newShip);
    }

    @DeleteMapping("/deleteShip/{id}")
    @ApiOperation(
            value = "Утилизировать корабль",
            httpMethod = "DELETE",
            produces = "application/json",
            response = String.class
    )
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

    @PostMapping("/newCrewMember")
    @ApiOperation(
            value = "Зарегистрировать нового моряка",
            httpMethod = "POST",
            produces = "application/json",
            response = String.class
    )
    public ResponseEntity<String> postCrewMember(
            @ApiParam(
                    value = "JSON-структура нового моряка",
                    name = "crew member",
                    required = true
            )
            @RequestBody final NewCrewMember newCrewMember) {
        return crewMemberService.createCrewMember(newCrewMember);
    }

    @DeleteMapping("/deleteCrewMember/{id}")
    @ApiOperation(
            value = "Уволить моряка",
            httpMethod = "DELETE",
            produces = "application/json",
            response = String.class
    )
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
