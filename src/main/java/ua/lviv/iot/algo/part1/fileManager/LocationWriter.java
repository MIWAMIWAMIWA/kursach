package ua.lviv.iot.algo.part1.fileManager;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import ua.lviv.iot.algo.part1.model.Location;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class LocationWriter {
    private final String directory;

    public LocationWriter(final String path) {
        directory = path;

    }

    public void writeLocation(final Location location) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yy");
        Date date = new Date();
        String file = directory + "LocationsDB" + File.separator
                + "location_" + dateFormat.format(date) + ".csv";
        boolean exists = new File(file).exists();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(
                new FileOutputStream(file, true),
                StandardCharsets.UTF_8))) {
            if (!exists) {
                writer.writeNext(location.getHeaders().split(";"));
            }
            writer.writeNext(location.toCSV().split(";"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savingLastId(final int id) {
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(
                new FileOutputStream(directory + "LocationsDB"
                        + File.separator + "id.txt", false),
                StandardCharsets.UTF_8))) {
            writer.writeNext(String.valueOf(id).split(","));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLastId() {
        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(
                new FileInputStream(directory + "LocationsDB"
                        + File.separator + "id.txt"),
                StandardCharsets.UTF_8)).build()) {
            List<String[]> allData = csvReader.readAll();
            return Integer.parseInt(allData.get(0)[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public HashMap<Integer, Location> readEntries(
            final String file) {
        HashMap<Integer, Location> data = new HashMap<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))
                .withSkipLines(1).build()) {
            List<String[]> allData = csvReader.readAll();
            for (String[] row : allData) {
                data.put(Integer.parseInt(row[0]), new Location(row));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public List<String> getFilesFromDirectory() {
        File folder = new File(directory + "LocationsDB" + File.separator);
        String[] fileNames = folder.list();
        assert fileNames != null : "no files was found";
        List<String> files = new LinkedList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM_yy");
        Date date = new Date();
        String pattern = "location_" + "\\d{2}"
                + "_" + dateFormat.format(date) + ".csv";
        for (String file : fileNames) {
            if (file.matches(pattern)) {
                files.add(file);
            }
        }
        return files;
    }

    public HashMap<Integer, Location> getAllEntries() {
        HashMap<Integer, Location> finalMap = new HashMap<>();
        for (String file : getFilesFromDirectory()) {
            HashMap<Integer, Location> tmpMap = readEntries(
                    directory + "LocationsDB" + File.separator + file);
            finalMap.putAll(tmpMap);
        }
        return finalMap;
    }

    public String findId(final int id) {
        String idPath = null;
        for (String file : getFilesFromDirectory()) {
            HashMap<Integer, Location> tmpMap = readEntries(
                    directory + "LocationsDB" + File.separator + file);
            if (tmpMap.containsKey(id)) {
                idPath = file;
                break;
            }
        }
        return idPath;
    }

    public void rewriteFile(final String fullPath,
                            final HashMap<Integer, Location> data) {
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
            boolean forHeaders = false;
            for (Location location : data.values()) {
                if (!forHeaders) {
                    writer.writeNext(location.getHeaders().split(";"));
                    forHeaders = true;
                }
                writer.writeNext(location.toCSV().split(";"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEntry(final int id) {
        String fileName = findId(id);
        String fullPath = directory + "LocationsDB" + File.separator
                + fileName;
        HashMap<Integer, Location> tmpMap = readEntries(fullPath);
        tmpMap.remove(id);
        if (tmpMap.isEmpty()) {
            try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(
                    new FileOutputStream(fullPath, true),
                    StandardCharsets.UTF_8))) {
                Location location = new Location();
                writer.writeNext(location.getHeaders().split(";"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        rewriteFile(fullPath, tmpMap);
    }

    public void modifyEntry(final int id,
                            final Location newLocation) {
        String fileName = findId(id);
        String fullPath = directory + "LocationsDB"
                + File.separator + fileName;
        HashMap<Integer, Location> tmpMap = readEntries(fullPath);
        tmpMap.put(id, newLocation);
        rewriteFile(fullPath, tmpMap);
    }
}
