package com.slavamashkov.swaggerrestapp.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.slavamashkov.swaggerrestapp.model.enums.ShipStatusType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ships", schema = "navy")
@Getter
@Setter
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipStatusType status;

    @Column(name = "min_crew_capacity", nullable = false)
    private int minCrewCapacity;

    @Column(name = "max_crew_capacity", nullable = false)
    private int maxCrewCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "port_id")
    @JsonBackReference
    private Port port;

    @OneToMany(mappedBy = "ship", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<CrewMember> crewMembers = new HashSet<>();
}
