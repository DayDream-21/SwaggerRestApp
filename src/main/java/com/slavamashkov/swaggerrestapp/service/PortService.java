package com.slavamashkov.swaggerrestapp.service;

import com.slavamashkov.swaggerrestapp.model.entity.Port;
import com.slavamashkov.swaggerrestapp.repositories.PortRepository;
import com.slavamashkov.swaggerrestapp.repositories.ShipRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class PortService {
    private final PortRepository portRepository;
    private final ShipRepository shipRepository;

    @Autowired
    public PortService(PortRepository portRepository, ShipRepository shipRepository) {
        this.portRepository = portRepository;
        this.shipRepository = shipRepository;
    }

    // Possible response codes 200, 500
    public ResponseEntity<List<Port>> readAllPorts() {
        return ResponseEntity.ok(portRepository.findAll());
    }

    // Possible response codes 200, 404, 500
    public ResponseEntity<String> readPortCapacityInfo(Long id) {
        final Optional<Port> optionalPort = portRepository.findById(id);

        if (optionalPort.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        final int totalPortCapacity = optionalPort.get().getCapacity();
        final int shipsInPort = shipRepository.countAllByPortId(id);

        final JSONObject jsonObject = new JSONObject();

        jsonObject.put("total", totalPortCapacity);
        jsonObject.put("used", shipsInPort);
        jsonObject.put("remain", totalPortCapacity - shipsInPort);

        return ResponseEntity.ok(jsonObject.toString());
    }
}
