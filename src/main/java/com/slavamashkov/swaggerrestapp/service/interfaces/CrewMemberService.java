package com.slavamashkov.swaggerrestapp.service.interfaces;

import com.slavamashkov.swaggerrestapp.model.entity.CrewMember;
import com.slavamashkov.swaggerrestapp.model.wrappers.CrewMemberStatus;
import com.slavamashkov.swaggerrestapp.model.wrappers.NewCrewMember;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CrewMemberService {
    // "Create" methods
    ResponseEntity<String> createCrewMember(NewCrewMember newCrewMember);

    // "Read" methods
    ResponseEntity<List<CrewMember>> readAllCrewMembers(String status);
    ResponseEntity<CrewMemberStatus> readCrewMemberStatus(Long id);

    // "Update" methods
    ResponseEntity<String> updateCrewMemberStatus(Long id, Long shipId, CrewMemberStatus status);

    // "Delete" methods
    ResponseEntity<String> deleteCrewMember(Long id);
}
