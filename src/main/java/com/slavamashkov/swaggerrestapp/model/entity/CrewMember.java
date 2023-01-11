package com.slavamashkov.swaggerrestapp.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.slavamashkov.swaggerrestapp.model.enums.CrewMemberStatusType;
import com.slavamashkov.swaggerrestapp.model.enums.Role;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "crew_members")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrewMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CrewMemberStatusType crewMemberStatusType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id", nullable = true)
    @JsonBackReference
    private Ship ship;
}
