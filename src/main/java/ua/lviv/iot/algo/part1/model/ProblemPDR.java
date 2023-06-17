package ua.lviv.iot.algo.part1.model;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemPDR {

    public static final String HEADERS = "id;idOfCar;"
            + "dateOfProblem;description;paidFine";

    private int id;
    private int idOfCar;
    private String dateOfProblem;

    private String description;

    private boolean paidFine;

    public String getHeaders() {
        return HEADERS;
    }

    public String toCSV() {
        String result =
                getId() + ";"
                        + getIdOfCar() + ";" + dateOfProblem + ";"
                        + description + ";";
        if (paidFine) {
            result = result + "1";
        } else {
            result = result + "0";
        }
        return result;
    }

    public ProblemPDR(final String[] data) {
        id = Integer.parseInt(data[0]);
        idOfCar = Integer.parseInt(data[1]);
        dateOfProblem = data[2];
        description = data[3];
        if (Integer.parseInt(data[4]) == 1) {
            paidFine = true;
        } else {
            paidFine = false;
        }
    }
}
