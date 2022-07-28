package com.slavamashkov.swaggerrestapp.service.impl;

import com.slavamashkov.swaggerrestapp.model.enums.Role;
import com.slavamashkov.swaggerrestapp.model.wrappers.CrewMemberStatus;
import com.slavamashkov.swaggerrestapp.model.entity.CrewMember;
import com.slavamashkov.swaggerrestapp.model.entity.Ship;
import com.slavamashkov.swaggerrestapp.model.enums.CrewMemberStatusType;
import com.slavamashkov.swaggerrestapp.model.enums.ShipStatusType;
import com.slavamashkov.swaggerrestapp.repositories.CrewMemberRepository;
import com.slavamashkov.swaggerrestapp.repositories.PortRepository;
import com.slavamashkov.swaggerrestapp.repositories.ShipRepository;
import com.slavamashkov.swaggerrestapp.service.interfaces.CrewMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CrewMemberServiceImpl implements CrewMemberService {
    private final CrewMemberRepository crewMemberRepository;
    private final ShipRepository shipRepository;

    @Autowired
    public CrewMemberServiceImpl(CrewMemberRepository crewMemberRepository,
                                 ShipRepository shipRepository) {
        this.crewMemberRepository = crewMemberRepository;
        this.shipRepository = shipRepository;
    }

    // "Create" methods

    // "Read" methods
    @Override
    public ResponseEntity<List<CrewMember>> readAllCrewMembers(String status) {
        // Если статус не был передан, тогда возвращаем всех моряков без фильтрации
        if (status == null) {
            return ResponseEntity.ok(crewMemberRepository.findAll());
        }

        // Если статус был передан, проверяем его правильность
        final Optional<CrewMemberStatusType> optionalCrewMemberStatusType =
                Optional.ofNullable(CrewMemberStatusType.getStatusType(status));

        // Если статус правильный, возвращаем всех моряков с этим статусом
        // Если нет, возвращаем ошибку 404 Not Found
        return optionalCrewMemberStatusType
                .map(statusType -> ResponseEntity.ok(crewMemberRepository.findAllByCrewMemberStatusType(statusType)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @Override
    public ResponseEntity<CrewMemberStatus> readCrewMemberStatus(Long id) {
        final Optional<CrewMember> optionalCrewMember = crewMemberRepository.findById(id);

        return optionalCrewMember
                .map(crewMember -> ResponseEntity.ok(new CrewMemberStatus(crewMember.getCrewMemberStatusType().name())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // "Update" methods
    @Override
    public ResponseEntity<String> updateCrewMemberStatus(Long id, Long shipId, CrewMemberStatus statusType) {
        final Optional<CrewMember> optionalCrewMember = crewMemberRepository.findById(id);
        // Если моряк не найден, тогда выдаем ошибку 404 Not Found
        if (optionalCrewMember.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Crew member with id=" + id + " not found");
        } else {
            final Optional<CrewMemberStatusType> optionalCrewMemberStatusType =
                    Optional.ofNullable(CrewMemberStatusType.getStatusType(statusType.getStatus()));
            final CrewMember crewMember = optionalCrewMember.get();
            // Если был получен не правильный статус, тогда выдаем ошибку 400 Bad Request
            if (optionalCrewMemberStatusType.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Unsupportable status");
            } else {
                switch (optionalCrewMemberStatusType.get()) {
                    // Если хотим поменять статут на ON_SHIP
                    case ON_SHIP:
                        // Проверяем не находится ли моряк уже на корабле
                        if (crewMember.getCrewMemberStatusType() != CrewMemberStatusType.ON_SHIP) {
                            // Проверяем на противоречие двух полей
                            // (у моряка на земле поле ship должно быть null)
                            if (crewMember.getShip() != null) {
                                return ResponseEntity
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("ОБНАРУЖЕНО ПРОТИВОРЕЧИЕ ПОЛЕЙ У МОРЯКА С ID=" + id + " !!!");
                            }

                            // Если shipId не был передан, тогда выдаем ошибку 400 Bad Request
                            if (shipId == null) {
                                return ResponseEntity
                                        .status(HttpStatus.BAD_REQUEST)
                                        .body("Ship id is null");
                            }

                            final Optional<Ship> optionalShip = shipRepository.findById(shipId);
                            // Если был передан не правильный shipID, тогда выдаем ошибку 404 Not Found
                            if (optionalShip.isEmpty()) {
                                return ResponseEntity
                                        .status(HttpStatus.NOT_FOUND)
                                        .body("Ship with id=" + shipId + " not found");
                            }

                            final Ship ship = optionalShip.get();
                            final Set<CrewMember> crewMembers = ship.getCrewMembers();
                            final boolean shipContainsCapitan = crewMembers
                                    .stream()
                                    .anyMatch(member -> member.getRole() == Role.CAPITAN);

                            // Если моряк является капитаном, то проверяем есть ли на корабле уже один капитан
                            if (crewMember.getRole() == Role.CAPITAN && shipContainsCapitan) {
                                return ResponseEntity
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .body("Capitan is already on the ship");
                            }
                            // Если на корабле есть место, корабль находится в порту и
                            // тогда назначаем моряка на корабль, меняем ему статус на ON_SHIP
                            if ((crewMembers.size() < ship.getMaxCrewCapacity()) &&
                                (ship.getStatus() == ShipStatusType.PORT)
                            ) {
                                crewMember.setShip(ship);
                                crewMember.setCrewMemberStatusType(CrewMemberStatusType.ON_SHIP);

                                crewMemberRepository.save(crewMember);
                            } else {
                                // Если одно из условий не выполняется, тогда выдаем ошибку 422 Unprocessable Entity
                                return ResponseEntity
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .body("There is no space on ship with id=" + shipId +
                                                " or this ship not in port");
                            }
                        } else {
                            return ResponseEntity
                                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                    .body("A crew member is already on the ship");
                        }
                        break;
                    // Если хотим поменять статус на ON_LAND
                    case ON_LAND:
                        // Проверяем не находится ли моряк уже на суше
                        if (crewMember.getCrewMemberStatusType() != CrewMemberStatusType.ON_LAND) {
                            // Проверяем на противоречие двух полей
                            // (у моряка на корабле поле ship не должно быть null)
                            if (crewMember.getShip() == null) {
                                return ResponseEntity
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("ОБНАРУЖЕНО ПРОТИВОРЕЧИЕ ПОЛЕЙ У МОРЯКА С ID=" + id + " !!!");
                            }

                            final Ship ship = crewMember.getShip();
                            // Проверяем, что моряк с указанным id находится на пришвартованном корабле
                            // Если находится, тогда снимаем моряка с корабля, меняем ему статус на ON_LAND
                            if (ship.getStatus() == ShipStatusType.PORT) {
                                crewMember.setShip(null);
                                crewMember.setCrewMemberStatusType(CrewMemberStatusType.ON_LAND);

                                crewMemberRepository.save(crewMember);
                            } else {
                                // Если корабль в море, тогда выдаем ошибку 422 Unprocessable Entity
                                return ResponseEntity
                                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                        .body("Crew member is on an unmoored ship");
                            }
                        } else {
                            return ResponseEntity
                                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                                    .body("A crew member is already on land");
                        }
                        break;
                    default:
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }
        }

        // Если были пройдены все соответствующие проверки выводим новый статус моряка
        return ResponseEntity.ok(optionalCrewMember.get().getCrewMemberStatusType().name());
    }

    @Override
    public ResponseEntity<String> updateCrewMemberRole(Long id, String role) {
        return null;
    }

    // "Delete" methods
    @Override
    public ResponseEntity<String> deleteCrewMember(Long id) {
        final Optional<CrewMember> optionalCrewMember = crewMemberRepository.findById(id);
        // Если моряк не найден, тогда выдаем ошибку 404 Not Found
        if (optionalCrewMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            // Если найден, то проверяем, где находится моряк
            CrewMember crewMember = optionalCrewMember.get();
            // Если в море, тогда мы не можем его удалить,
            // выдаем ошибку 422 Unprocessable Entity
            if (crewMember.getShip().getStatus() == ShipStatusType.SEA) {
                return ResponseEntity
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body("Crew member with id=" + id + " is currently at sea");
            } else {
                return ResponseEntity.ok("Crew member with id=" + id +
                        " was successfully deleted");
            }
        }
    }


}
