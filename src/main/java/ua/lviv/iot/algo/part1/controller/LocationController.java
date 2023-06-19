package ua.lviv.iot.algo.part1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.algo.part1.mappers.LocationMapper;
import ua.lviv.iot.algo.part1.model.Location;
import ua.lviv.iot.algo.part1.modelDTO.LocationDTO;
import ua.lviv.iot.algo.part1.service.AutomobileService;
import ua.lviv.iot.algo.part1.service.LocationService;

import java.util.LinkedList;
import java.util.List;

@RequestMapping("/locations")
@RestController
public class LocationController {
    @Autowired
    private LocationService locationService;

    @Autowired
    private AutomobileService automobileService;

    @Autowired
    private LocationMapper locationMapper;

    public static final ResponseEntity OK = ResponseEntity
            .status(HttpStatusCode.valueOf(200)).build();

    public static final ResponseEntity FAILURE = ResponseEntity
            .status(HttpStatusCode.valueOf(404)).build();

    @GetMapping
    public ResponseEntity getAllLocations() {
        List<LocationDTO> response = new LinkedList<>();
        for (Location location : locationService.giveAll()) {
            response.add(locationMapper.map(location));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity getLocation(
            final @PathVariable("id") int locationId) {
        if (!locationService.hasLocationWith(locationId)) {
            return FAILURE;
        } else {
            return ResponseEntity.ok(locationMapper.map(locationService
                    .giveLocation(locationId)));
        }
    }

    @PostMapping
    public ResponseEntity createLocation(
            final @RequestBody LocationDTO locationDTO) {
        for (Integer id : locationMapper.map(locationDTO)
                .getCarsId()) {
            if (!automobileService.hasAutomobileWith(id)) {
                return FAILURE;
            }
        }
        return ResponseEntity.ok(locationMapper.map(
                locationService.addLocation(
                        locationMapper.map(locationDTO))));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteLocation(
            final @PathVariable("id") int locationId) {
        if (!locationService.hasLocationWith(locationId)) {
            return FAILURE;
        } else {
            return ResponseEntity.ok(locationMapper.map(
                    locationService.deleteLocation(locationId)));

        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity updateLocation(
            final @PathVariable("id") int locationId,
            final @RequestBody LocationDTO location) {
        for (Integer id : locationMapper.map(location).getCarsId()) {
            if (!automobileService.hasAutomobileWith(id)) {
                return FAILURE;
            }
        }
        if (locationService.hasLocationWith(locationId)) {
            locationService.replaceLocation(locationMapper
                    .map(location), locationId);
            return OK;
        } else {
            return FAILURE;
        }
    }

}
