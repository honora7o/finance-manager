package com.financemanager.financemanagerapp.routes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("health")
public class HealthResource {

    @GetMapping
    String health() {
        return "Application is running";
    }
}
