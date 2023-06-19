package ua.lviv.iot.algo.part1.fileManager;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import ua.lviv.iot.algo.part1.model.ProblemPDR;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ProblemPDRWriter {
    private final String directory;

    public ProblemPDRWriter(final String path) {
        directory = path;

    }

    public void writeProblem(final ProblemPDR problemPDR) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yy");
        Date date = new Date();
        String file = directory + "ProblemsDB" + File.separator
                + "problemPDR_" + dateFormat.format(date) + ".csv";
        boolean exists = new File(file).exists();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(
                new FileOutputStream(file, true),
                StandardCharsets.UTF_8))) {
            if (!exists) {
                writer.writeNext(problemPDR.getHeaders().split(";"));
            }
            writer.writeNext(problemPDR.toCSV().split(";"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savingLastId(final int id) {
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(
                new FileOutputStream(directory + "ProblemsDB"
                        + File.separator + "id.txt", false),
                StandardCharsets.UTF_8))) {
            writer.writeNext(String.valueOf(id).split(","));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLastId() {
        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(
                new FileInputStream(directory + "ProblemsDB"
                        + File.separator + "id.txt"),
                StandardCharsets.UTF_8)).build()) {
            List<String[]> allData = csvReader.readAll();
            return Integer.parseInt(allData.get(0)[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public HashMap<Integer, HashMap<Integer, ProblemPDR>> readEntries(
            final String file) {
        HashMap<Integer, HashMap<Integer, ProblemPDR>> data = new HashMap<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))
                .withSkipLines(1).build()) {
            List<String[]> allData = csvReader.readAll();
            for (String[] row : allData) {
                if (data.containsKey(Integer.parseInt(row[1]))) {
                    HashMap<Integer, ProblemPDR> tmpMap
                            = data.get(Integer.parseInt(row[1]));
                    tmpMap.put(Integer.parseInt(row[0]), new ProblemPDR(row));
                    data.put(Integer.parseInt(row[1]), tmpMap);
                } else {
                    HashMap<Integer, ProblemPDR> tmpMap = new HashMap<>();
                    tmpMap.put(Integer.parseInt(row[0]), new ProblemPDR(row));
                    data.put(Integer.parseInt(row[1]), tmpMap);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public List<String> getFilesFromDirectory() {
        File folder = new File(directory + "ProblemsDB" + File.separator);
        String[] fileNames = folder.list();
        assert fileNames != null : "no files was found";
        List<String> files = new LinkedList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM_yy");
        Date date = new Date();
        String pattern = "problemPDR_" + "\\d{2}"
                + "_" + dateFormat.format(date) + ".csv";
        for (String file : fileNames) {
            if (file.matches(pattern)) {
                files.add(file);
            }
        }
        return files;
    }

    public HashMap<Integer, HashMap<Integer, ProblemPDR>>
    getAllEntries() {
        HashMap<Integer, HashMap<Integer, ProblemPDR>>
                finalMap = new HashMap<>();
        for (String file : getFilesFromDirectory()) {
            HashMap<Integer, HashMap<Integer, ProblemPDR>> tmpMap = readEntries(
                    directory + "ProblemsDB" + File.separator + file);
            finalMap.putAll(tmpMap);
        }
        return finalMap;
    }

    public String findId(final int id) {
        String idPath = null;
        for (String file : getFilesFromDirectory()) {
            HashMap<Integer, HashMap<Integer, ProblemPDR>> tmpData
                    = readEntries(
                    directory + "ProblemsDB" + File.separator + file);
            for (HashMap<Integer, ProblemPDR> tmpMap : tmpData.values()) {
                if (tmpMap.containsKey(id)) {
                    idPath = file;
                    break;
                }
            }
        }
        return idPath;
    }

    public void rewriteFile(final String fullPath,
                            final HashMap<Integer,
                                    HashMap<Integer, ProblemPDR>> data) {
        try {
            File file = new File(fullPath);
            boolean res = file.delete();
            assert res : "file was not deleted";
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(
                new FileOutputStream(fullPath, true),
                StandardCharsets.UTF_8))) {
            ProblemPDR prob = new ProblemPDR();
            writer.writeNext(prob.getHeaders().split(";"));
            for (HashMap<Integer, ProblemPDR> tmpMap : data.values()) {
                for (ProblemPDR problem : tmpMap.values()) {
                    writer.writeNext(problem.toCSV().split(";"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEntry(final int id) {
        String fileName = findId(id);
        String fullPath = directory
                + "ProblemsDB" + File.separator
                + fileName;
        HashMap<Integer, HashMap<Integer, ProblemPDR>> tmpMap
                = readEntries(fullPath);
        for (Integer key : tmpMap.keySet()) {
            if (tmpMap.get(key).containsKey(id)) {
                tmpMap.get(key).remove(id);
                break;
            }
        }

        rewriteFile(fullPath, tmpMap);
    }

    public void modifyEntry(final int id,
                            final ProblemPDR problem) {
        String fileName = findId(id);
        String fullPath = directory + "ProblemsDB"
                + File.separator + fileName;
        HashMap<Integer, HashMap<Integer, ProblemPDR>>
                tmpMap = readEntries(fullPath);
        if (!tmpMap.containsKey(problem.getIdOfCar())) {
            tmpMap.put(problem.getIdOfCar(), new HashMap<>());
        }
        for (HashMap<Integer, ProblemPDR> map : tmpMap.values()) {
            if (map.containsKey(id)) {
                map.remove(id);
            }
        }
        tmpMap.get(problem.getIdOfCar()).put(id, problem);
        rewriteFile(fullPath, tmpMap);
    }
}
