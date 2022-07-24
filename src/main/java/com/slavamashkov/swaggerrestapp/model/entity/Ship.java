package com.slavamashkov.swaggerrestapp.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.slavamashkov.swaggerrestapp.model.enums.ShipStatusType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ships", schema = "navy")
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "port_id")
    private Port port;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipStatusType status;
}