package com.slavamashkov.swaggerrestapp.service.interfaces;

import com.slavamashkov.swaggerrestapp.model.wrappers.NewShip;
import com.slavamashkov.swaggerrestapp.model.wrappers.ShipStatus;
import com.slavamashkov.swaggerrestapp.model.entity.CrewMember;
import com.slavamashkov.swaggerrestapp.model.entity.Ship;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface ShipService {
    // "Create" methods
    ResponseEntity<String> createShip(NewShip newShip);

    // "Read" methods
    ResponseEntity<List<Ship>> readAllShips(String status);
    ResponseEntity<ShipStatus> readShipStatus(Long id);
    ResponseEntity<Set<CrewMember>> readAllCrewMembersById(Long id);

    // "Update" methods
    ResponseEntity<String> updateShipStatus(Long id, Long portId, ShipStatus status);

    // "Delete" methods
    ResponseEntity<String> deleteShip(Long id);
}
