package com.slavamashkov.swaggerrestapp.service.interfaces;

import com.slavamashkov.swaggerrestapp.model.wrappers.CrewMemberStatus;
import com.slavamashkov.swaggerrestapp.model.entity.CrewMember;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CrewMemberService {
    // "Create" methods

    // "Read" methods
    ResponseEntity<List<CrewMember>> readAllCrewMembers(String status);
    ResponseEntity<CrewMemberStatus> readCrewMemberStatus(Long id);

    // "Update" methods
    ResponseEntity<String> updateCrewMemberStatus(Long id, Long shipId, CrewMemberStatus status);
    ResponseEntity<String> updateCrewMemberRole(Long id, String role);

    // "Delete" methods
    ResponseEntity<String> deleteCrewMember(Long id);
}
