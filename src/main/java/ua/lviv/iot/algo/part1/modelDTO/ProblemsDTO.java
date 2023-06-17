package ua.lviv.iot.algo.part1.modelDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemsDTO {
    private int id;
    private int idOfCar;
    private String dateOfProblem;

    private String description;

    private boolean paidFine;
}
