package ua.lviv.iot.algo.part1.service;

import org.springframework.stereotype.Service;
import ua.lviv.iot.algo.part1.fileManager.LocationWriter;
import ua.lviv.iot.algo.part1.model.Location;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LocationService {
    private final LocationWriter locationWriter = new LocationWriter(
            "src" + File.separator
                    + "main" + File.separator
                    + "resources" + File.separator);

    private HashMap<Integer, Location> locations
            = locationWriter.getAllEntries();

    private AtomicInteger lastId
            = new AtomicInteger(locationWriter.getLastId());

    public Location deleteLocation(final int id) {
        locationWriter.deleteEntry(id);
        return locations.remove(id);
    }

    public void replaceLocation(final Location location, final int id) {
        location.setId(id);
        locationWriter.modifyEntry(id, location);
        locations.put(id, location);
    }

    public Location addLocation(final Location location) {
        location.setId(lastId.incrementAndGet());
        locationWriter.writeLocation(location);
        locations.put(location.getId(), location);
        return location;
    }

    public Collection<Location> giveAll() {
        return locations.values();
    }

    public Location giveLocation(final int id) {
        return locations.get(id);
    }

    public boolean hasLocationWith(final int id) {
        return locations.containsKey(id);
    }

    @PreDestroy
    public void onApplicationShutdown() {
        locationWriter.savingLastId(lastId.intValue());
    }
}
