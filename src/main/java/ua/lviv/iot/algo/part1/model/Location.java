package ua.lviv.iot.algo.part1.model;

import lombok.*;

import java.util.LinkedList;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    public static final String HEADERS = "id;name;carsID";
    private int id;
    private String name;
    private List<Integer> carsId;

    public String getHeaders() {
        return HEADERS;
    }

    public String toCSV() {
        StringBuilder result = new StringBuilder(getId() + ";"
                + getName() + ";");
        for (Integer value : getCarsId()) {
            result.append(value).append(";");
        }
        result = new StringBuilder(result.substring(0, result.length() - 1));
        return result.toString();
    }

    public Location(final String[] data) {
        id = Integer.parseInt(data[0]);
        name = data[1];
        List<Integer> result = new LinkedList<>();
        for (int i = 2; i < data.length; i++) {
            result.add(Integer.parseInt(data[i]));
        }
        carsId = result;
    }


}
