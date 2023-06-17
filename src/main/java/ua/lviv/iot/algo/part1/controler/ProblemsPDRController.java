package ua.lviv.iot.algo.part1.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.algo.part1.model.ProblemPDR;
import ua.lviv.iot.algo.part1.modelDTO.ProblemsDTO;
import ua.lviv.iot.algo.part1.service.AutomobileService;
import ua.lviv.iot.algo.part1.service.ProblemPDRService;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@RequestMapping("/problems")
@RestController
public class ProblemsPDRController {
    @Autowired
    private ProblemPDRService problemService;

    @Autowired
    private AutomobileService automobileService;

    private ProblemsDTO formatProblem(final ProblemPDR problem) {
        return new ProblemsDTO(problem.getId(), problem.getIdOfCar(),
                problem.getDateOfProblem(), problem.getDescription(),
                problem.isPaidFine());
    }

    private List<ProblemsDTO> formatProblems(
            final Collection<ProblemPDR> data) {
        List<ProblemsDTO> response = new LinkedList<>();
        for (ProblemPDR problem : data) {
            response.add(formatProblem(problem));
        }
        return response;
    }

    public static final ResponseEntity OK = ResponseEntity
            .status(HttpStatusCode.valueOf(200)).build();

    public static final ResponseEntity FAILURE = ResponseEntity
            .status(HttpStatusCode.valueOf(404)).build();

    @GetMapping
    public ResponseEntity getAllProblems() {
        return ResponseEntity.ok(formatProblems(problemService.giveAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity getProblems(
            final @PathVariable("id") int automobileId) {
        if (!problemService.hasProblemWith(automobileId)) {
            return FAILURE;
        } else {
            return ResponseEntity.ok(formatProblems(problemService
                    .giveProblems(automobileId)));
        }
    }

    @PostMapping
    public ResponseEntity createProblem(
            final @RequestBody ProblemPDR problem) {
        if (!automobileService.hasAutomobileWith(problem.getIdOfCar())) {
            return FAILURE;
        }
        return ResponseEntity.ok(formatProblem(
                problemService.addProblem(problem)));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteProblem(
            final @PathVariable("id") int problemId) {
        if (!problemService.hasIdWith(problemId)) {
            return FAILURE;
        } else {
            return ResponseEntity.ok(formatProblem(
                    problemService.deleteProblem(problemId)));
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity updateProblem(
            final @PathVariable("id") int problemId,
            final @RequestBody ProblemPDR problem) {
        if (!automobileService.hasAutomobileWith(problem.getIdOfCar())) {
            return FAILURE;
        }
        if (problemService.hasIdWith(problemId)) {
            problemService.replaceProblem(problem, problemId);
            return OK;
        } else {
            return FAILURE;
        }
    }

}
