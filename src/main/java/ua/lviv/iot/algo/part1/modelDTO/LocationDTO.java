package ua.lviv.iot.algo.part1.modelDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    private int id;
    private String name;
    private List<Integer> carsId;
}
