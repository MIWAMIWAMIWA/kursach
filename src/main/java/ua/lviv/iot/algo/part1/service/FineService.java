package ua.lviv.iot.algo.part1.service;

import org.springframework.stereotype.Service;
import ua.lviv.iot.algo.part1.fileManager.FineWriter;
import ua.lviv.iot.algo.part1.model.Fine;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FineService {
    private final FineWriter fineWriter = new FineWriter(
            "src" + File.separator
                    + "main" + File.separator
                    + "resources" + File.separator);

    private HashMap<Integer, HashMap<Integer, Fine>> fines
            = fineWriter.getAllEntries();

    private AtomicInteger lastId
            = new AtomicInteger(fineWriter.getLastId());

    public Fine deleteFine(final int id) {
        fineWriter.deleteEntry(id);
        for (Integer key : fines.keySet()) {
            if (fines.get(key).containsKey(id)) {
                return fines.get(key).remove(id);
            }
        }
        return null;
    }

    public void replaceFine(final Fine fine, final int id) {
        fine.setId(id);
        fineWriter.modifyEntry(id, fine);
        for (HashMap<Integer, Fine> map : fines.values()) {
            if (map.containsKey(id)) {
                map.remove(id);
            }
        }
        if (!fines.containsKey(fine.getIdOfCar())) {
            fines.put(fine.getIdOfCar(), new HashMap<>());
        }
        fines.get(fine.getIdOfCar()).put(id, fine);
    }

    public Fine addFine(final Fine fine) {
        fine.setId(lastId.incrementAndGet());
        fineWriter.writeFine(fine);
        if (fines.containsKey(fine.getIdOfCar())) {
            fines.get(fine.getIdOfCar()).put(fine.getId(), fine);
        } else {
            HashMap<Integer, Fine> tmpMap = new HashMap<>();
            tmpMap.put(fine.getId(), fine);
            fines.put(fine.getIdOfCar(), tmpMap);
        }
        return fine;
    }

    public Collection<Fine> giveAll() {
        Collection<Fine> data = new LinkedList<>();
        for (HashMap<Integer, Fine> map : fines.values()) {
            for (Fine problem : map.values()) {
                data.add(problem);
            }
        }
        return data;
    }

    public Collection<Fine> giveFine(final int carId) {
        if (!fines.containsKey(carId)) {
            return new LinkedList<>();
        } else {
            return fines.get(carId).values();
        }
    }

    public boolean hasIdWith(final int fineId) {
        boolean result = false;
        for (HashMap<Integer, Fine> tmpMap : fines.values()) {
            if (tmpMap.containsKey(fineId)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean hasFineWith(final int carId) {
        return fines.containsKey(carId);
    }

    @PreDestroy
    public void onApplicationShutdown() {
        fineWriter.savingLastId(lastId.intValue());
    }
}
