package ua.lviv.iot.algo.part1.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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



    private LocationDTO formatLocation(final Location location) {
        return new LocationDTO(location.getId(),
                location.getName(), location.getCarsId());
    }

    public static final ResponseEntity OK = ResponseEntity
            .status(HttpStatusCode.valueOf(200)).build();

    public static final ResponseEntity FAILURE = ResponseEntity
            .status(HttpStatusCode.valueOf(404)).build();

    @GetMapping
    public ResponseEntity getAllLocations() {
        List<LocationDTO> response = new LinkedList<>();
        for (Location location : locationService.giveAll()) {
            response.add(formatLocation(location));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity getLocation(
            final @PathVariable("id") int locationId) {
        if (!locationService.hasLocationWith(locationId)) {
            return FAILURE;
        } else {
            return ResponseEntity.ok(formatLocation(locationService
                    .giveLocation(locationId)));
        }
    }

    @PostMapping
    public ResponseEntity createLocation(
            final @RequestBody Location location) {
        for (Integer id : location.getCarsId()) {
            if (!automobileService.hasAutomobileWith(id)) {
                return FAILURE;
            }
        }

        return ResponseEntity.ok(formatLocation(
                locationService.addLocation(location)));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteLocation(
            final @PathVariable("id") int locationId) {
        if (!locationService.hasLocationWith(locationId)) {
            return FAILURE;
        } else {
            return ResponseEntity.ok(formatLocation(
                    locationService.deleteLocation(locationId)));

        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity updateLocation(
            final @PathVariable("id") int locationId,
            final @RequestBody Location location) {
        for (Integer id : location.getCarsId()) {
            if (!automobileService.hasAutomobileWith(id)) {
                return FAILURE;
            }
        }
        if (locationService.hasLocationWith(locationId)) {
            locationService.replaceLocation(location, locationId);
            return OK;
        } else {
            return FAILURE;
        }
    }

}
