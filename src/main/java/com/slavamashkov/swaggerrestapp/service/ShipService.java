package com.slavamashkov.swaggerrestapp.service;

import com.slavamashkov.swaggerrestapp.model.entity.Port;
import com.slavamashkov.swaggerrestapp.model.entity.Ship;
import com.slavamashkov.swaggerrestapp.model.entity.ShipStatus;
import com.slavamashkov.swaggerrestapp.model.enums.ShipStatusType;
import com.slavamashkov.swaggerrestapp.repositories.PortRepository;
import com.slavamashkov.swaggerrestapp.repositories.ShipRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
// todo describe errors
@Service
public class ShipService {
    private final ShipRepository shipRepository;
    private final PortRepository portRepository;

    @Autowired
    public ShipService(ShipRepository shipRepository, PortRepository portRepository) {
        this.shipRepository = shipRepository;
        this.portRepository = portRepository;
    }

    public ResponseEntity<List<Ship>> readAllShips(String status) {
        if (status == null) {
            return ResponseEntity.ok(shipRepository.findAll());
        }

        Optional<ShipStatusType> optionalShipStatusType = Optional.ofNullable(ShipStatusType.getStatusType(status));

        if (optionalShipStatusType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.ok(shipRepository.findAllByStatus(optionalShipStatusType.get()));
        }
    }

    public ResponseEntity<String> createShip(Ship ship) {
        if (ship == null || ship.getName() == null || ship.getPort() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        final Optional<Port> optionalPort = portRepository.findById(ship.getPort().getId());

        if (optionalPort.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        final int shipInPortCount = shipRepository.countAllByPortId(ship.getPort().getId());

        if (shipInPortCount < optionalPort.get().getCapacity() && ship.getStatus().equals(ShipStatusType.PORT)) {
            shipRepository.save(ship);

            Optional<Ship> optionalLastInsertedShip = shipRepository.findAll()
                    .stream()
                    .max(Comparator.comparing(Ship::getId));

            if (optionalLastInsertedShip.isPresent()) {
                final JSONObject jsonObject = new JSONObject();

                jsonObject.put("id", optionalLastInsertedShip.get().getId());

                return ResponseEntity.ok(jsonObject.toString());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    public ResponseEntity<String> deleteShip(Long id) {
        final Optional<Ship> optionalShip = shipRepository.findById(id);

        if (optionalShip.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            shipRepository.deleteById(id);
            return ResponseEntity.ok("Ship with id=" + id + " successfully deleted");
        }
    }

    public ResponseEntity<ShipStatus> readShipStatus(Long id) {
        final Optional<Ship> optionalShip = shipRepository.findById(id);

        if (optionalShip.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            final ShipStatus shipStatus = new ShipStatus();
            shipStatus.setStatus(optionalShip.get().getStatus().name());
            return ResponseEntity.ok(shipStatus);
        }
    }

    public ResponseEntity<ShipStatus> updateShipStatus(Long id, Long portId, ShipStatus status) {
        final Optional<Ship> optionalShip = shipRepository.findById(id);

        if (optionalShip.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            Optional<ShipStatusType> optionalShipStatusType = Optional.ofNullable(ShipStatusType.getStatusType(status.getStatus()));
            Ship ship = optionalShip.get();

            if (optionalShipStatusType.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                switch (optionalShipStatusType.get()) {
                    case SEA:
                        if (ship.getStatus() != ShipStatusType.SEA) {
                            ship.setStatus(ShipStatusType.SEA);
                            ship.setPort(null);

                            shipRepository.save(ship);
                        }
                        break;
                    case PORT:
                        if (ship.getStatus() != ShipStatusType.PORT) {
                            if (portId == null) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                            } else {
                                final Optional<Port> optionalPort = portRepository.findById(portId);

                                if (optionalPort.isEmpty()) {
                                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                                }

                                final int shipsInPortCount = shipRepository.countAllByPortId(id);

                                if (shipsInPortCount < optionalPort.get().getCapacity()) {
                                    ship.setStatus(ShipStatusType.PORT);
                                    ship.setPort(optionalPort.get());

                                    shipRepository.save(ship);
                                } else {
                                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
                                }
                            }
                        }
                        break;
                    default:
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }
        }
        ShipStatus newShipStatus = new ShipStatus();
        newShipStatus.setStatus(optionalShip.get().getStatus().name());

        return ResponseEntity.ok(newShipStatus);
    }
}
