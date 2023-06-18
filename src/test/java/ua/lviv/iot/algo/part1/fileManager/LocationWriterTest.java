package ua.lviv.iot.algo.part1.fileManager;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.algo.part1.model.Location;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocationWriterTest {

    private final LocationWriter locationWriter = new LocationWriter(
            "src" + File.separator
                    + "test" + File.separator
                    + "resources" + File.separator);

    private final static String pathForLocations = "src" + File.separator
            + "test" + File.separator
            + "resources" + File.separator
            + "LocationsDB" + File.separator
            + "location_" + new SimpleDateFormat("dd_MM_yy")
            .format(new Date()) + ".csv";

    private final static String pathForId = "src" + File.separator
            + "test" + File.separator
            + "resources"
            + File.separator
            + "LocationsDB" + File.separator
            + "id.txt";

    private final static String forExpected = "src" + File.separator
            + "test" + File.separator
            + "resources" + File.separator + "expected"
            + File.separator;


    @AfterEach
    public void preDestroy() {
        File file1 = new File(pathForLocations);
        boolean res = file1.delete();
    }

    @Test
    void writeLocation() {
        List<Integer> tmp1 = new LinkedList<>();
        tmp1.add(1);
        tmp1.add(2);
        tmp1.add(3);
        List<Integer> tmp2 = new LinkedList<>();
        tmp2.add(4);
        tmp2.add(5);
        locationWriter.writeLocation(new Location(1, "Lviv", tmp1));
        locationWriter.writeLocation(new Location(2, "Kyiv", tmp2));
        try {
            assertTrue(FileUtils.contentEquals(new File(pathForLocations),
                    new File(forExpected
                            + "writeLocations.csv")));
        } catch (FileNotFoundException e) {
            assertEquals(false, "FileNotFoundException");
            throw new RuntimeException(e);
        } catch (IOException e) {
            assertEquals(false, "IOException");
            throw new RuntimeException(e);

        }
    }

    @Test
    void savingLastId() {
        locationWriter.savingLastId(10);
        try {
            assertTrue(FileUtils.contentEquals(new File(pathForId),
                    new File(forExpected
                            + "savingLastId.txt")));
        } catch (FileNotFoundException e) {
            assertEquals(false, "FileNotFoundException");
            throw new RuntimeException(e);
        } catch (IOException e) {
            assertEquals(false, "IOException");
            throw new RuntimeException(e);

        }
    }

    @Test
    void getLastId() {
        locationWriter.savingLastId(20);
        assertEquals(20, locationWriter.getLastId());

    }

    @Test
    void readEntries() {
        HashMap<Integer, Location> expMap = new HashMap<>();
        List<Integer> tmp1 = new LinkedList<>();
        tmp1.add(1);
        tmp1.add(2);
        tmp1.add(3);
        List<Integer> tmp2 = new LinkedList<>();
        tmp2.add(4);
        tmp2.add(5);
        expMap.put(1, new Location(1, "Lviv", tmp1));
        expMap.put(2, new Location(2, "Kyiv", tmp2));
        HashMap<Integer, Location> resMap = locationWriter.readEntries(
                forExpected + "writeLocations.csv"
        );
        assertEquals(expMap.keySet(), resMap.keySet());
        for (Integer key : expMap.keySet()) {
            assertEquals(expMap.get(key).toString(), resMap.get(key).toString());
        }

    }

    @Test
    void getFilesFromDirectory() {
        List<String> expList = new LinkedList<>();
        expList.add("location_16_06_23.csv");
        expList.add("location_17_06_23.csv");
        assertEquals(expList, locationWriter.getFilesFromDirectory());
    }

    @Test
    void findId() {
        assertEquals("location_17_06_23.csv", locationWriter.findId(1));

    }

    @Test
    void rewriteFile() {
        HashMap<Integer, Location> map = new HashMap<>();
        List<Integer> tmp1 = new LinkedList<>();
        tmp1.add(1);
        tmp1.add(2);
        tmp1.add(3);
        List<Integer> tmp2 = new LinkedList<>();
        tmp2.add(4);
        tmp2.add(5);
        map.put(1, new Location(1, "Lviv", tmp1));
        map.put(2, new Location(2, "Kyiv", tmp2));
        locationWriter.writeLocation(new Location(20, "Bakhmut", tmp2));
        locationWriter.writeLocation(new Location(43, "Struj", tmp1));
        locationWriter.writeLocation(new Location(11, "Vinnutsya", tmp2));
        locationWriter.rewriteFile(pathForLocations, map);
        try {
            assertTrue(FileUtils.contentEquals(new File(pathForLocations),
                    new File(forExpected
                            + "writeLocations.csv")));
        } catch (FileNotFoundException e) {
            assertEquals(false, "FileNotFoundException");
            throw new RuntimeException(e);
        } catch (IOException e) {
            assertEquals(false, "IOException");
            throw new RuntimeException(e);

        }

    }
}