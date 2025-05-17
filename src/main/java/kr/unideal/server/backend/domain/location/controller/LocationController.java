package kr.unideal.server.backend.domain.location.controller;

import kr.unideal.server.backend.domain.location.entity.LocationType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @GetMapping
    public List<Map<String, String>> getAllLocations() {
        return Arrays.stream(LocationType.values())
                .map(loc -> Map.of("key", loc.name(), "value", loc.getLabel()))
                .collect(Collectors.toList());
    }
}