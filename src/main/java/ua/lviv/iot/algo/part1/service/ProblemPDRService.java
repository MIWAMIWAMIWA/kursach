package ua.lviv.iot.algo.part1.service;

import org.springframework.stereotype.Service;
import ua.lviv.iot.algo.part1.fileManager.ProblemPDRWriter;
import ua.lviv.iot.algo.part1.model.ProblemPDR;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProblemPDRService {
    private final ProblemPDRWriter problemPDRWriter = new ProblemPDRWriter(
            "src" + File.separator
                    + "main" + File.separator
                    + "resources" + File.separator);

    private HashMap<Integer, HashMap<Integer, ProblemPDR>> problems
            = problemPDRWriter.getAllEntries();

    private AtomicInteger lastId
            = new AtomicInteger(problemPDRWriter.getLastId());

    public ProblemPDR deleteProblem(final int id) {
        problemPDRWriter.deleteEntry(id);
        for (Integer key : problems.keySet()) {
            if (problems.get(key).containsKey(id)) {
                return problems.get(key).remove(id);
            }
        }
        return null;
    }

    public void replaceProblem(final ProblemPDR problem, final int id) {
        problem.setId(id);
        problemPDRWriter.modifyEntry(id, problem);
        for (HashMap<Integer, ProblemPDR> map : problems.values()) {
            if (map.containsKey(id)) {
                map.remove(id);
            }
        }
        if (!problems.containsKey(problem.getIdOfCar())) {
            problems.put(problem.getIdOfCar(), new HashMap<>());
        }
        problems.get(problem.getIdOfCar()).put(id, problem);
    }

    public ProblemPDR addProblem(final ProblemPDR problem) {
        problem.setId(lastId.incrementAndGet());
        problemPDRWriter.writeProblem(problem);
        if (problems.containsKey(problem.getIdOfCar())) {
            problems.get(problem.getIdOfCar()).put(problem.getId(), problem);
        } else {
            HashMap<Integer, ProblemPDR> tmpMap = new HashMap<>();
            tmpMap.put(problem.getId(), problem);
            problems.put(problem.getIdOfCar(), tmpMap);
        }
        return problem;
    }

    public Collection<ProblemPDR> giveAll() {
        Collection<ProblemPDR> data = new LinkedList<>();
        for (HashMap<Integer, ProblemPDR> map : problems.values()) {
            for (ProblemPDR problem : map.values()) {
                data.add(problem);
            }
        }
        return data;
    }

    public Collection<ProblemPDR> giveProblems(final int carId) {
        if (!problems.containsKey(carId)) {
            return new LinkedList<>();
        } else {
            return problems.get(carId).values();
        }
    }

    public boolean hasIdWith(final int id) {
        boolean result = false;
        for (HashMap<Integer, ProblemPDR> tmpMap : problems.values()) {
            if (tmpMap.containsKey(id)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean hasProblemWith(final int carId) {
        return problems.containsKey(carId);
    }

    @PreDestroy
    public void onApplicationShutdown() {
        problemPDRWriter.savingLastId(lastId.intValue());
    }
}
