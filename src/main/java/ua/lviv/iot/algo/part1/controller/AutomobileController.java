package ua.lviv.iot.algo.part1.controller;

import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.algo.part1.mappers.AutomobileMapper;
import ua.lviv.iot.algo.part1.model.Automobile;
import ua.lviv.iot.algo.part1.modelDTO.AutomobileDTO;
import ua.lviv.iot.algo.part1.service.AutomobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;


import java.util.LinkedList;
import java.util.List;

@RequestMapping("/automobiles")
@RestController
public class AutomobileController {

    @Autowired
    private AutomobileService automobileService;

    @Autowired
    private AutomobileMapper automobileMapper;

    public static final ResponseEntity OK = ResponseEntity
            .status(HttpStatusCode.valueOf(200)).build();

    public static final ResponseEntity FAILURE = ResponseEntity
            .status(HttpStatusCode.valueOf(404)).build();

    @GetMapping
    public ResponseEntity getAllAutomobiles() {
        List<AutomobileDTO> response = new LinkedList<>();
        for (Automobile automobile : automobileService.giveAll()) {
            response.add(automobileMapper.map(automobile));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity getAutomobile(
            final @PathVariable("id") int automobileId) {
        if (!automobileService.hasAutomobileWith(automobileId)) {
            return FAILURE;
        } else {
            return ResponseEntity.ok(automobileMapper.map(automobileService
                    .giveAutomobile(automobileId)));
        }
    }

    @PostMapping
    public ResponseEntity createAutomobile(
            final @RequestBody AutomobileDTO automobile) {
        return ResponseEntity.ok(automobileMapper.map(
                automobileService.addAutomobile(
                        automobileMapper.map(automobile))));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteAutomobile(
            final @PathVariable("id") int automobileId) {
        if (!automobileService.hasAutomobileWith(automobileId)) {
            return FAILURE;
        } else {
           return ResponseEntity.ok(automobileMapper.map(
                   automobileService.deleteAutomobile(automobileId)));

        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity updateAutomobile(
            final @PathVariable("id") int automobileId,
            final @RequestBody AutomobileDTO automobile) {
        if (automobileService.hasAutomobileWith(automobileId)) {
            automobileService.replaceAutomobile(
                    automobileMapper.map(automobile), automobileId);
            return OK;
        } else {
            return FAILURE;
        }
    }
}
