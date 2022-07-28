package com.slavamashkov.swaggerrestapp.model.wrappers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewShip {
    private String name;
    private Integer minCrewCapacity;
    private Integer maxCrewCapacity;
    private Long portId;
}
