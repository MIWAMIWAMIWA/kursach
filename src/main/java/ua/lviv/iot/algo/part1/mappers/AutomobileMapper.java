package ua.lviv.iot.algo.part1.mappers;

import org.springframework.stereotype.Component;
import ua.lviv.iot.algo.part1.model.Automobile;
import ua.lviv.iot.algo.part1.modelDTO.AutomobileDTO;
@Component
public class AutomobileMapper {
    public AutomobileDTO map(final Automobile automobile) {
        return new AutomobileDTO(automobile.getId(),
                automobile.getModel(),
                automobile.getStartOfRent(),
                automobile.getEndOfRent());
    }

    public Automobile map(final AutomobileDTO automobileDTO){
        return new Automobile(automobileDTO.getId(),
                automobileDTO.getModel(),
                automobileDTO.getStartOfRent(),
                automobileDTO.getEndOfRent());
    }
}
