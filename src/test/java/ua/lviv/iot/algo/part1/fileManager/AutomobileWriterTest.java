package ua.lviv.iot.algo.part1.fileManager;

import org.apache.commons.io.FileUtils;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.algo.part1.model.Automobile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class AutomobileWriterTest {

    private final AutomobileWriter automobileWriter = new AutomobileWriter(
            "src" + File.separator
                    + "test" + File.separator
                    + "resources" + File.separator);

    private final static String pathForAutomobiles = "src" + File.separator
            + "test" + File.separator
            + "resources" + File.separator
            + "AutomobilesDB" + File.separator
            + "automobile_" + new SimpleDateFormat("dd_MM_yy")
            .format(new Date()) + ".csv";

    private final static String pathForId = "src" + File.separator
            + "test" + File.separator
            + "resources" + File.separator + "id.txt";

    @AfterEach
    public void preDestroy() {
        File file1 = new File(pathForAutomobiles);
        boolean res = file1.delete();
    }

    @Test
    void writeAutomobile() {
        automobileWriter.writeAutomobile(new Automobile(
                1, "BMW", "st.Zelena", "2022-06-07", "speeding ticket"
        ));
        automobileWriter.writeAutomobile(new Automobile(
                2, "Miwa", "st.Central", "2026-10-07", "none"
        ));
        try {
            assertTrue(FileUtils.contentEquals(new File(pathForAutomobiles),
                    new File(automobileWriter.getDirectory()
                            + File.separator + "expected" + File.separator
                            + "writeAutomobiles.csv")));
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
        automobileWriter.savingLastId(10);
        try {
            assertTrue(FileUtils.contentEquals(new File(pathForId),
                    new File(automobileWriter.getDirectory()
                            + File.separator + "expected" + File.separator
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
        automobileWriter.savingLastId(20);
        assertEquals(20, automobileWriter.getLastId());
    }


    @Test
    void readEntries() {
        HashMap<Integer, Automobile> expMap = new HashMap<>();
        expMap.put(1, new Automobile(1, "BMW", "st.Zelena"
                , "2022-06-07", "speeding ticket"));
        expMap.put(2, new Automobile(2, "Miwa",
                "st.Central", "2026-10-07", "none"));
        HashMap<Integer, Automobile> resMap = automobileWriter.readEntries(
                automobileWriter.getDirectory() + "expected"
                        + File.separator + "writeAutomobiles.csv");
        assertEquals(expMap.keySet(), resMap.keySet());
        for (Integer key : expMap.keySet()) {
            assertEquals(expMap.get(key).toString(), resMap.get(key).toString());
        }
    }

    @Test
    void getFilesFromDirectory() {
        List<String> expList = new LinkedList<>();
        expList.add("automobile_11_06_23.csv");
        expList.add("automobile_13_06_23.csv");
        assertEquals(expList, automobileWriter.getFilesFromDirectory());
    }

    @Test
    void findId() {
        assertEquals("automobile_13_06_23.csv", automobileWriter.findId(9));
    }

    @Test
    void rewriteFile() {
        automobileWriter.writeAutomobile(new Automobile(
                1, "BMW", "st.Zelena", "2022-06-07", "speeding ticket"
        ));
        automobileWriter.writeAutomobile(new Automobile(
                2, "Miwa", "st.Central", "2026-10-07", "none"
        ));
        automobileWriter.writeAutomobile(new Automobile(
                3, "Audi", "st.South", "2023-11-17", "none"
        ));
        HashMap<Integer, Automobile> tmpMap = new HashMap<>();
        tmpMap.put(1, new Automobile(1, "BMW", "st.Zelena"
                , "2022-06-07", "speeding ticket"));
        tmpMap.put(2, new Automobile(2, "Miwa",
                "st.Central", "2026-10-07", "none"));
        automobileWriter.rewriteFile(pathForAutomobiles, tmpMap);
        try {
            assertTrue(FileUtils.contentEquals(new File(pathForAutomobiles),
                    new File(automobileWriter.getDirectory()
                            + File.separator + "expected" + File.separator
                            + "writeAutomobiles.csv")));
        } catch (FileNotFoundException e) {
            assertEquals(false, "FileNotFoundException");
            throw new RuntimeException(e);
        } catch (IOException e) {
            assertEquals(false, "IOException");
            throw new RuntimeException(e);

        }


    }
}