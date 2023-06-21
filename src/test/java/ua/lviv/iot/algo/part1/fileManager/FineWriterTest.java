package ua.lviv.iot.algo.part1.fileManager;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.algo.part1.model.Fine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FineWriterTest {

    private final FineWriter problemWriter = new FineWriter(
            "src" + File.separator
                    + "test" + File.separator
                    + "resources" + File.separator);

    private final static String pathForProblems = "src" + File.separator
            + "test" + File.separator
            + "resources" + File.separator
            + "ProblemsDB" + File.separator
            + "problemPDR_" + new SimpleDateFormat("dd_MM_yy")
            .format(new Date()) + ".csv";

    private final static String pathForId = "src" + File.separator
            + "test" + File.separator
            + "resources"
            + File.separator
            + "ProblemsDB" + File.separator
            + "id.txt";

    private final static String forExpected = "src" + File.separator
            + "test" + File.separator
            + "resources" + File.separator + "expected"
            + File.separator;

    @AfterEach
    public void preDestroy() {
        File file1 = new File(pathForProblems);
        boolean res = file1.delete();
    }



    @Test
    void writeProblem() {
        problemWriter.writeFine(new Fine(
                1,1,"2005-01-04","speeding ticket",true
        ));
        problemWriter.writeFine(new Fine(
                2,6,"2015-11-05","driving drunk",false
        ));
        try {
            assertTrue(FileUtils.contentEquals(new File(pathForProblems),
                    new File(forExpected
                            + "writeProblems.csv")));
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
        problemWriter.savingLastId(10);
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
        problemWriter.savingLastId(20);
        assertEquals(20, problemWriter.getLastId());
    }

    @Test
    void readEntries() {
        HashMap<Integer, HashMap<Integer, Fine>> expMap = new HashMap<>();
        HashMap<Integer, Fine> tmp1 = new HashMap<>();
        HashMap<Integer, Fine> tmp2 = new HashMap<>();
        tmp1.put(1,new Fine(
                1,1,"2005-01-04","speeding ticket",true
        ));
        tmp2.put(2,new Fine(
                2,6,"2015-11-05","driving drunk",false
        ));
        expMap.put(1,tmp1);
        expMap.put(6,tmp2);
        HashMap<Integer, HashMap<Integer, Fine>> resMap =
                problemWriter.readEntries(forExpected + "writeProblems.csv");
        assertEquals(expMap.keySet(), resMap.keySet());
        for (Integer key : expMap.keySet()) {
            assertEquals(expMap.get(key).keySet(), resMap.get(key).keySet());
            for (Integer innerKey : expMap.get(key).keySet()){
                assertEquals(expMap.get(key).get(innerKey).toString(),
                        resMap.get(key).get(innerKey).toString());
            }
        }


    }

    @Test
    void getFilesFromDirectory() {
        List<String> expList = new LinkedList<>();
        expList.add("problemPDR_16_06_23.csv");
        expList.add("problemPDR_17_06_23.csv");
        assertEquals(expList, problemWriter.getFilesFromDirectory());

    }

    @Test
    void findId() {
        assertEquals("problemPDR_17_06_23.csv", problemWriter.findId(9));
    }

    @Test
    void rewriteFile() {
        HashMap<Integer, HashMap<Integer, Fine>> map = new HashMap<>();
        HashMap<Integer, Fine> tmp1 = new HashMap<>();
        HashMap<Integer, Fine> tmp2 = new HashMap<>();
        tmp1.put(1,new Fine(
                1,1,"2005-01-04","speeding ticket",true
        ));
        tmp2.put(2,new Fine(
                2,6,"2015-11-05","driving drunk",false
        ));
        map.put(1,tmp1);
        map.put(6,tmp2);
        problemWriter.writeFine(new Fine(
                1,1,"2005-01-04","speeding ticket",true
        ));
        problemWriter.writeFine(new Fine(
                10,10,"2015-01-04","speeding ticket",true
        ));
        problemWriter.writeFine(new Fine(
                100,100,"2025-01-04","speeding ticket",true
        ));
        problemWriter.rewriteFile(pathForProblems,map);
        try {
            assertTrue(FileUtils.contentEquals(new File(pathForProblems),
                    new File(forExpected
                            + "writeProblems.csv")));
        } catch (FileNotFoundException e) {
            assertEquals(false, "FileNotFoundException");
            throw new RuntimeException(e);
        } catch (IOException e) {
            assertEquals(false, "IOException");
            throw new RuntimeException(e);

        }

    }
}