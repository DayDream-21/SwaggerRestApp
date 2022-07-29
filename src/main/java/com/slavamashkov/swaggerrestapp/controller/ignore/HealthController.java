package com.slavamashkov.swaggerrestapp.controller.ignore;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(value = "/health", tags = {"Проверка состояния сервиса"})
@ApiIgnore
public class HealthController {
    @GetMapping(value = "/health")
    @ApiOperation(
            value = "Получить состояние сервиса",
            httpMethod = "GET",
            produces = "application/json",
            response = String.class
    )
    public String getHealth() {
        final JSONObject jsonObject = new JSONObject();

        jsonObject.put("started", true);

        return jsonObject.toString();
    }
}
