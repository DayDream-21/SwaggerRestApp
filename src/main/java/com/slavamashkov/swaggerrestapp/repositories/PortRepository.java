package com.slavamashkov.swaggerrestapp.repositories;

import com.slavamashkov.swaggerrestapp.model.entity.Port;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortRepository extends JpaRepository<Port, Long> {

}
