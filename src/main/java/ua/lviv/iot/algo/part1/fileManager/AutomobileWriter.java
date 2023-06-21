package ua.lviv.iot.algo.part1.fileManager;


import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import lombok.Getter;
import ua.lviv.iot.algo.part1.model.Automobile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter
public class AutomobileWriter {
    private final String directory;

    public AutomobileWriter(final String path) {
        directory = path;

    }

    public void writeAutomobile(final Automobile automobile) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yy");
        Date date = new Date();
        String file = directory + "AutomobilesDB" + File.separator
                + "automobile_" + dateFormat.format(date) + ".csv";
        boolean exists = new File(file).exists();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(
                new FileOutputStream(file, true),
                StandardCharsets.UTF_8))) {
            if (!exists) {
                writer.writeNext(automobile.getHeaders().split(";"));
            }
            writer.writeNext(automobile.toCSV().split(";"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savingLastId(final int id) {
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(
                new FileOutputStream(directory + "AutomobilesDB"
                        + File.separator + "id.txt", false),
                StandardCharsets.UTF_8))) {
            writer.writeNext(String.valueOf(id).split(","));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLastId() {
        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(
                new FileInputStream(directory
                        + "AutomobilesDB" + File.separator + "id.txt"),
                StandardCharsets.UTF_8)).build()) {
            List<String[]> allData = csvReader.readAll();
            return Integer.parseInt(allData.get(0)[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public HashMap<Integer, Automobile> readEntries(
            final String file) {
        HashMap<Integer, Automobile> data = new HashMap<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))
                .withSkipLines(1).build()) {
            List<String[]> allData = csvReader.readAll();
            for (String[] row : allData) {
                data.put(Integer.parseInt(row[0]), new Automobile(row));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public List<String> getFilesFromDirectory() {
        File folder = new File(directory + "AutomobilesDB" + File.separator);
        String[] fileNames = folder.list();
        assert fileNames != null : "no files was found";
        List<String> files = new LinkedList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM_yy");
        Date date = new Date();
        String pattern = "automobile_" + "\\d{2}"
                + "_" + dateFormat.format(date) + ".csv";
        for (String file : fileNames) {
            if (file.matches(pattern)) {
                files.add(file);
            }
        }
        return files;
    }

    public HashMap<Integer, Automobile> getAllEntries() {
        HashMap<Integer, Automobile> finalMap = new HashMap<>();
        for (String file : getFilesFromDirectory()) {
            HashMap<Integer, Automobile> tmpMap = readEntries(
                    directory + "AutomobilesDB" + File.separator + file);
            finalMap.putAll(tmpMap);
        }
        return finalMap;
    }

    public String findId(final int id) {
        String idLocation = null;
        for (String file : getFilesFromDirectory()) {
            HashMap<Integer, Automobile> tmpMap = readEntries(
                    directory + "AutomobilesDB" + File.separator + file);
            if (tmpMap.containsKey(id)) {
                idLocation = file;
                break;
            }
        }
        return idLocation;
    }

    public void rewriteFile(final String fullPath,
                            final HashMap<Integer, Automobile> data) {
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
            Automobile auto = new Automobile();
            writer.writeNext(auto.getHeaders().split(";"));
            for (Automobile automobile : data.values()) {
                writer.writeNext(automobile.toCSV().split(";"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEntry(final int id) {
        String fileName = findId(id);
        String fullPath = directory + "AutomobilesDB" + File.separator
                + fileName;
        HashMap<Integer, Automobile> tmpMap = readEntries(fullPath);
        tmpMap.remove(id);
        rewriteFile(fullPath, tmpMap);
    }

    public void modifyEntry(final int id,
                            final Automobile newAutomobile) {
        String fileName = findId(id);
        String fullPath = directory + "AutomobilesDB"
                + File.separator + fileName;
        HashMap<Integer, Automobile> tmpMap = readEntries(fullPath);
        tmpMap.put(id, newAutomobile);
        rewriteFile(fullPath, tmpMap);
    }
}

