package com.slavamashkov.swaggerrestapp.controller;

import com.slavamashkov.swaggerrestapp.model.wrappers.CrewMemberStatus;
import com.slavamashkov.swaggerrestapp.model.entity.CrewMember;
import com.slavamashkov.swaggerrestapp.service.impl.CrewMemberServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "guard", produces = "application/json")
@Api(value = "/guard", tags = {"Береговая охрана"})
public class CoastGuardController {
}
