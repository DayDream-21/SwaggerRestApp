package com.slavamashkov.swaggerrestapp.controller.ignore;

import com.slavamashkov.swaggerrestapp.model.wrappers.NewShip;
import com.slavamashkov.swaggerrestapp.model.wrappers.ShipStatus;
import com.slavamashkov.swaggerrestapp.model.entity.CrewMember;
import com.slavamashkov.swaggerrestapp.model.entity.Ship;
import com.slavamashkov.swaggerrestapp.service.impl.ShipServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "ships", produces = "application/json")
@Api(value = "/ships", tags = {"Корабли"})
@ApiIgnore
public class ShipController {
    private final ShipServiceImpl shipService;

    @Autowired
    public ShipController(ShipServiceImpl shipService) {
        this.shipService = shipService;
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

    @PutMapping("/{id}/status")
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
}
