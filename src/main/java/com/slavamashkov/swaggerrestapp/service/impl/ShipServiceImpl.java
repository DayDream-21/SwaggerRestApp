package com.slavamashkov.swaggerrestapp.service.impl;

import com.slavamashkov.swaggerrestapp.model.wrappers.NewShip;
import com.slavamashkov.swaggerrestapp.model.wrappers.ShipStatus;
import com.slavamashkov.swaggerrestapp.model.entity.CrewMember;
import com.slavamashkov.swaggerrestapp.model.entity.Port;
import com.slavamashkov.swaggerrestapp.model.entity.Ship;
import com.slavamashkov.swaggerrestapp.model.enums.Role;
import com.slavamashkov.swaggerrestapp.model.enums.ShipStatusType;
import com.slavamashkov.swaggerrestapp.repositories.PortRepository;
import com.slavamashkov.swaggerrestapp.repositories.ShipRepository;
import com.slavamashkov.swaggerrestapp.service.interfaces.ShipService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShipServiceImpl implements ShipService {
    private final ShipRepository shipRepository;
    private final PortRepository portRepository;

    @Autowired
    public ShipServiceImpl(ShipRepository shipRepository, PortRepository portRepository) {
        this.shipRepository = shipRepository;
        this.portRepository = portRepository;
    }

    // "Create" methods
    @Override
    public ResponseEntity<String> createShip(NewShip newShip) {
        if (newShip == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (newShip.getName() == null ||
            newShip.getMinCrewCapacity() == null ||
            newShip.getMaxCrewCapacity() == null ||
            newShip.getPortId() == null
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (isNameUnique(newShip)) {
            final Optional<Port> optionalPort = portRepository.findById(newShip.getPortId());

            if (optionalPort.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            final Port port = optionalPort.get();
            final int shipInPortCount = port.getShips().size();

            if (shipInPortCount < port.getCapacity()) {
                Ship ship = Ship.builder()
                        .name(newShip.getName())
                        .minCrewCapacity(newShip.getMinCrewCapacity())
                        .maxCrewCapacity(newShip.getMaxCrewCapacity())
                        .port(port)
                        .status(ShipStatusType.PORT)
                        .build();

                shipRepository.save(ship);

                return ResponseEntity.ok("New ship successfully created" +
                        " in port with id=" + ship.getPort().getId());
            } else {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
            }
        } else {
            // Not unique name
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    // "Read" methods
    @Override
    public ResponseEntity<List<Ship>> readAllShips(String status) {
        if (status == null) {
            return ResponseEntity.ok(shipRepository.findAll());
        }
        // Если был передан статус, тогда проверяем его правильность
        Optional<ShipStatusType> optionalShipStatusType =
                Optional.ofNullable(ShipStatusType.getStatusType(status));
        // Если статус правильный, тогда возвращаем все корабли с этим статусом
        return optionalShipStatusType
                .map(statusType -> ResponseEntity.ok(shipRepository.findAllByStatus(statusType)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @Override
    public ResponseEntity<ShipStatus> readShipStatus(Long id) {
        final Optional<Ship> optionalShip = shipRepository.findById(id);

        return optionalShip
                .map(ship -> ResponseEntity.ok(new ShipStatus(ship.getStatus().name())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<Set<CrewMember>> readAllCrewMembersById(Long id) {
        final Optional<Ship> optionalShip = shipRepository.findById(id);

        return optionalShip
                .map(ship -> ResponseEntity.ok(ship.getCrewMembers()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // "Update methods"
    @Override
    public ResponseEntity<String> updateShipStatus(Long id, Long portId, ShipStatus status) {
        final Optional<Ship> optionalShip = shipRepository.findById(id);
        // Если корабль не найден, то выдаем ошибку 404 Not Found
        if (optionalShip.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Ship with id=" + id + " not found");
        } else {
            final Optional<ShipStatusType> optionalShipStatusType =
                    Optional.ofNullable(ShipStatusType.getStatusType(status.getStatus()));
            final Ship ship = optionalShip.get();
            // Если был получен не правильный статус, тогда выдаем ошибку 400 Bad Request
            if (optionalShipStatusType.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Unsupportable status");
            } else {
                switch (optionalShipStatusType.get()) {
                    // Если хотим поменять статус на SEA
                    case SEA:
                        // Проверяем не находится ли корабль уже в море
                        if (ship.getStatus() != ShipStatusType.SEA) {
                            final Set<CrewMember> crewMembers = ship.getCrewMembers();

                            boolean isCapitanOnOnBoard = crewMembers
                                    .stream()
                                    .anyMatch(crewMember -> crewMember.getRole().equals(Role.CAPITAN));

                            // Проверяем есть ли на борту CAPITAN и удовлетворяет ли размер команды требованиям
                            if (isCapitanOnOnBoard &&
                                ship.getMinCrewCapacity() <= crewMembers.size() &&
                                ship.getMaxCrewCapacity() >= crewMembers.size()
                            ) {
                                ship.setStatus(ShipStatusType.SEA);
                                ship.setPort(null);

                                shipRepository.save(ship);

                                // Если были пройдены все соответствующие проверки выводим новый статус корабля
                                return ResponseEntity.ok(optionalShip.get().getStatus().name());
                            } else {
                                return ResponseEntity
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .body("There is no captain on board or the size of the crew " +
                                                "does not meet the requirements");
                            }
                        } else {
                            return ResponseEntity
                                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                    .body("The ship is already at sea");
                        }
                        // Если хотим поменять статус на PORT
                    case PORT:
                        // Проверяем не находится ли корабль уже в порту
                        if (ship.getStatus() != ShipStatusType.PORT) {
                            // Если portId не был передан, тогда выдаем ошибку 400 Bad Request
                            if (portId == null) {
                                return ResponseEntity
                                        .status(HttpStatus.BAD_REQUEST)
                                        .body("Port id is null");
                            }

                            final Optional<Port> optionalPort = portRepository.findById(portId);
                            // Если был передан не правильный portId, тогда выдаем ошибку 404 Not Found
                            if (optionalPort.isEmpty()) {
                                return ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .body("Port with id=" + portId + " not found");
                            }

                            final Port port = optionalPort.get();
                            final int shipsInPortCount = port.getShips().size();

                            // Проверяем есть ли место в порту
                            if (shipsInPortCount < port.getCapacity()) {
                                ship.setStatus(ShipStatusType.PORT);
                                ship.setPort(optionalPort.get());

                                shipRepository.save(ship);

                                // Если были пройдены все соответствующие проверки выводим новый статус корабля
                                return ResponseEntity.ok(optionalShip.get().getStatus().name());
                            } else {
                                // Если нет, тогда выдаем ошибку 422 Unprocessable Entity
                                return ResponseEntity
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .body("There is no space in the port with id=" + portId);
                            }
                        } else {
                            return ResponseEntity
                                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                    .body("The ship is already in port");
                        }
                    default:
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }
        }
    }

    // "Delete methods"
    @Override
    public ResponseEntity<String> deleteShip(Long id) {
        final Optional<Ship> optionalShip = shipRepository.findById(id);

        // Если корабль не найден, то выдаем ошибку 404 NotFound
        if (optionalShip.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Ship with id=" + id + " not found");
        } else {
            // Если найден, то проверяем, где находится корабль
            Ship ship = optionalShip.get();
            // Если в море, выдаем ошибку 422 Unprocessable Entity
            if (ship.getStatus() == ShipStatusType.SEA) {
                return ResponseEntity
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body("Ship with id=" + id + " is currently at sea");
            }
            // Если на корабле все еще есть люди, выдаем ошибку 422 Unprocessable Entity
            if (ship.getCrewMembers().size() > 0) {
                return ResponseEntity
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body("There are still people on the ship with id=" + id);
            } else {
                shipRepository.deleteById(id);
                return ResponseEntity.ok("Ship with id=" + id +
                        " was successfully deleted");
            }
        }
    }

    private boolean isNameUnique(NewShip newShip) {
        return shipRepository
                .findAll()
                .stream()
                .noneMatch(ship -> ship.getName().equals(newShip.getName()));
    }
}
