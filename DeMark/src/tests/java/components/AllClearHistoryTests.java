package components;

import components.model.AllClearHistory;
import components.model.ClearHistory;
import components.model.ClearRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class AllClearHistoryTests {
    AllClearHistory history;
    ClearHistory clearHistory;
    ClearRecord clearRecord;

    @Before
    public void init() {
        history = new AllClearHistory();

        clearHistory = new ClearHistory();
        clearRecord = new ClearRecord();

        clearRecord.addRecord(1, "Test Line of Testyness");
        clearHistory.addHistory(clearRecord);
    }

    @Test
    public void testAddNewSingleHistory() {
        String file = "src/file/thisFile.file";

        history.addSingleHistory(file, clearRecord);

        assertNotNull("Add was unsuccessful", history.getSingleHistory(file));
        assertEquals("Added wrong kind of object or object was changed during add", clearHistory, history.getSingleHistory(file));
    }

    @Test
    public void testAddToHistory() {
        String file = "src/file/thisFile.file";
        history.addSingleHistory(file, clearRecord);

        ClearRecord record = new ClearRecord();
        record.addRecord(3, "Test Line of Existence");

        history.addSingleHistory(file, record);
        assertNotNull("Add was unsuccessful", history.getSingleHistory(file));
        assertEquals("Record was not added properly", 2, history.getSingleHistory(file).size());
    }

    @Test
    public void testGetSingleHistory() {
        String file = "src/file/thisFile.file";

        history.put(file, clearHistory);

        ClearHistory singleHistory = history.getSingleHistory(file);
        assertNotNull("Single history is null", singleHistory);

        ClearRecord imAnIdiot = singleHistory.getHistory();     // it's a stack
        assertNotNull("Single record is null", imAnIdiot);
        assertEquals("Records do not match", clearRecord, imAnIdiot);
    }
}
