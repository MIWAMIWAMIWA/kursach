package ua.lviv.iot.algo.part1.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.lviv.iot.algo.part1.model.Location;
import ua.lviv.iot.algo.part1.modelDTO.AutomobileDTO;
import ua.lviv.iot.algo.part1.modelDTO.LocationDTO;
import ua.lviv.iot.algo.part1.service.AutomobileService;

import java.util.ArrayList;
import java.util.List;

@Component
public class LocationMapper{

    @Autowired
    private AutomobileMapper automobileMapper;

    @Autowired
    private AutomobileService automobileService;

    public LocationDTO map(Location location){
        List<AutomobileDTO> tmpList = new ArrayList<>();
        for (Integer id : location.getCarsId()){
            tmpList.add(automobileMapper.map(
                    automobileService.giveAutomobile(id)));
        }
        return new LocationDTO(location.getId(),
                location.getName(),tmpList);
    }

    public Location map(LocationDTO locationDTO){
        List<Integer> tmpList = new ArrayList<>();
        for (AutomobileDTO auto : locationDTO.getCarsId()){
            tmpList.add(auto.getId());
        }
        return new Location(locationDTO.getId(),
                locationDTO.getName(),tmpList);
    }


}
