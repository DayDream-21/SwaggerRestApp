package com.slavamashkov.swaggerrestapp.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ports", schema = "navy")
@Getter
@Setter
public class Port {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @OneToMany(mappedBy = "port", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Ship> ships;
}
