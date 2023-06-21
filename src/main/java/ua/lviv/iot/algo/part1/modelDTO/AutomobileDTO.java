package ua.lviv.iot.algo.part1.modelDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AutomobileDTO {
    private int id;
    private String model;
    private String startOfRent;
    private String endOfRent;

}
