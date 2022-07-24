package com.slavamashkov.swaggerrestapp.repositories;

import com.slavamashkov.swaggerrestapp.model.entity.Ship;
import com.slavamashkov.swaggerrestapp.model.enums.ShipStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {
    int countAllByPortId(Long portId);
    List<Ship> findAllByStatus(ShipStatusType status);
}
