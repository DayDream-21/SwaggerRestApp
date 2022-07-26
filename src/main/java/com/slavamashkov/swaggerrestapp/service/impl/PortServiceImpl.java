package com.slavamashkov.swaggerrestapp.service.impl;

import com.slavamashkov.swaggerrestapp.model.entity.Port;
import com.slavamashkov.swaggerrestapp.repositories.PortRepository;
import com.slavamashkov.swaggerrestapp.repositories.ShipRepository;
import com.slavamashkov.swaggerrestapp.service.interfaces.PortService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PortServiceImpl implements PortService {
    private final PortRepository portRepository;

    @Autowired
    public PortServiceImpl(PortRepository portRepository) {
        this.portRepository = portRepository;
    }

    public ResponseEntity<List<Port>> readAllPorts() {
        return ResponseEntity.ok(portRepository.findAll());
    }

    public ResponseEntity<String> readPortCapacityInfo(Long id) {
        final Optional<Port> optionalPort = portRepository.findById(id);

        return optionalPort
                .map(port -> {
                    final int totalPortCapacity = port.getCapacity();
                    final int shipsInPort = port.getShips().size();

                    final JSONObject jsonObject = new JSONObject();

                    jsonObject.put("total", totalPortCapacity);
                    jsonObject.put("used", shipsInPort);
                    jsonObject.put("remain", totalPortCapacity - shipsInPort);

                    return ResponseEntity.ok(jsonObject.toString());
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
