package com.slavamashkov.swaggerrestapp.service.interfaces;

import com.slavamashkov.swaggerrestapp.model.entity.Port;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PortService {
    // "Create" methods

    // "Read" methods
    ResponseEntity<List<Port>> readAllPorts();
    ResponseEntity<String> readPortCapacityInfo(Long id);

    // "Update" methods

    // "Delete" methods
}
