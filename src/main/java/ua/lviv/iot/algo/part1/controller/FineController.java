package ua.lviv.iot.algo.part1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.algo.part1.mappers.FineMapper;
import ua.lviv.iot.algo.part1.modelDTO.FineDTO;
import ua.lviv.iot.algo.part1.service.AutomobileService;
import ua.lviv.iot.algo.part1.service.FineService;

@RequestMapping("/fines")
@RestController
public class FineController {
    @Autowired
    private FineService fineService;

    @Autowired
    private FineMapper fineMapper;

    @Autowired
    private AutomobileService automobileService;


    public static final ResponseEntity OK = ResponseEntity
            .status(HttpStatusCode.valueOf(200)).build();

    public static final ResponseEntity FAILURE = ResponseEntity
            .status(HttpStatusCode.valueOf(404)).build();

    @GetMapping
    public ResponseEntity getAllFines() {
        return ResponseEntity.ok(
                fineMapper.map(fineService.giveAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity getFine(
            final @PathVariable("id") int automobileId) {
        if (!fineService.hasFineWith(automobileId)) {
            return FAILURE;
        } else {
            return ResponseEntity.ok(fineMapper.map(fineService
                    .giveFine(automobileId)));
        }
    }

    @PostMapping
    public ResponseEntity createFine(
            final @RequestBody FineDTO fineDTO) {
        if (!automobileService.hasAutomobileWith(
                fineDTO.getIdOfCar())) {
            return FAILURE;
        }
        return ResponseEntity.ok(
                fineService.addFine(fineMapper.map(fineDTO)));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteFine(
            final @PathVariable("id") int fineId) {
        if (!fineService.hasIdWith(fineId)) {
            return FAILURE;
        } else {
            return ResponseEntity.ok(fineMapper.map(
                    fineService.deleteFine(fineId)));
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity updateFine(
            final @PathVariable("id") int fineId,
            final @RequestBody FineDTO fineDTO) {
        if (!automobileService.hasAutomobileWith(fineDTO.getIdOfCar())) {
            return FAILURE;
        }
        if (fineService.hasIdWith(fineId)) {
            fineService.replaceFine(
                    fineMapper.map(fineDTO), fineId);
            return OK;
        } else {
            return FAILURE;
        }
    }
}
