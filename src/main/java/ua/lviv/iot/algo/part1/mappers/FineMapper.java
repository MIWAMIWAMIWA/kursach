package ua.lviv.iot.algo.part1.mappers;

import org.springframework.stereotype.Component;
import ua.lviv.iot.algo.part1.model.Fine;
import ua.lviv.iot.algo.part1.modelDTO.FineDTO;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Component
public class FineMapper {

    public FineDTO map(final Fine problem) {
        return new FineDTO(problem.getId(), problem.getIdOfCar(),
                problem.getDateOfProblem(), problem.getDescription(),
                problem.isPaidFine());
    }

    public Fine map(final FineDTO problem) {
        return new Fine(problem.getId(), problem.getIdOfCar(),
                problem.getDateOfProblem(), problem.getDescription(),
                problem.isPaidFine());
    }

    public List<FineDTO> map(
            final Collection<Fine> data) {
        List<FineDTO> response = new LinkedList<>();
        for (Fine problem : data) {
            response.add(map(problem));
        }
        return response;
    }
}
