package com.slavamashkov.swaggerrestapp.controller;

import com.slavamashkov.swaggerrestapp.model.wrappers.CrewMemberStatus;
import com.slavamashkov.swaggerrestapp.service.impl.CrewMemberServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "crew", produces = "application/json")
@Api(value = "/crew", tags = {"Команда"})
@ApiIgnore
public class CrewController {
    private final CrewMemberServiceImpl crewMemberService;

    @Autowired
    public CrewController(CrewMemberServiceImpl crewMemberService) {
        this.crewMemberService = crewMemberService;
    }

    @GetMapping("/{id}/status")
    @ApiOperation(
            value = "Узнать статус моряка",
            httpMethod = "GET",
            produces = "application/json",
            response = CrewMemberStatus.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Моряк не найден"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка")})
    public ResponseEntity<CrewMemberStatus> getCrewMemberStatus(
            @ApiParam(
                    value = "id моряка",
                    name = "id",
                    required = true,
                    example = "1"
            )
            @PathVariable(value = "id") final Long id
    ) {
        return crewMemberService.readCrewMemberStatus(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(
            value = "Уволить моряка",
            httpMethod = "DELETE",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Моряк не найден"),
            @ApiResponse(code = 500, message = "Внутренняя ошибка")})
    public ResponseEntity<String> deleteCrewMember(
            @ApiParam(
                    value = "id моряка",
                    name = "id",
                    required = true,
                    example = "1"
            )
            @PathVariable(value = "id") final Long id
    ) {
        return crewMemberService.deleteCrewMember(id);
    }
}
