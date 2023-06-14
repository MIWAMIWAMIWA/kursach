package ua.lviv.iot.algo.part1.model;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Automobile {
    public static final String HEADERS =
            "id; model; address;endOfRent;Problems";
    private Integer id;
    private String model;
    private String address;
    private String endOfRent;
    private String problems;

    public String getHeaders() {
        return HEADERS;
    }

    public String toSCV() {
        return getId() + ";"
                + getModel() + ";"
                + getAddress() + ";"
                + getEndOfRent() + ";"
                + getProblems();

    }

    public Automobile(final String[] data) {
        id = Integer.parseInt(data[0]);
        model = data[1];
        address = data[2];
        endOfRent = data[3];
        problems = data[4];
    }
}
