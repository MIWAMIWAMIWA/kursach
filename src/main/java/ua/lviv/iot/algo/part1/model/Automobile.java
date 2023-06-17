package ua.lviv.iot.algo.part1.model;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Automobile {
    public static final String HEADERS =
            "id; model; startOfRent;endOfRent";
    private Integer id;
    private String model;
    private String startOfRent;
    private String endOfRent;

    public String getHeaders() {
        return HEADERS;
    }

    public String toCSV() {
        return getId() + ";"
                + getModel() + ";"
                + getStartOfRent() + ";"
                + getEndOfRent();

    }

    public Automobile(final String[] data) {
        id = Integer.parseInt(data[0]);
        model = data[1];
        startOfRent = data[2];
        endOfRent = data[3];
    }
}
