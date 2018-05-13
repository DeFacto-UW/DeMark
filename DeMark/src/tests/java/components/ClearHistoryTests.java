package components;

import components.model.ClearHistory;
import components.model.ClearRecord;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClearHistoryTests {
    ClearHistory clearHistory;
    ClearRecord clearRecord;

    @Before
    public void init() {
        clearHistory = new ClearHistory();
        clearRecord = new ClearRecord();

        clearRecord.addRecord(1, "Test Test McTestFace");
        clearRecord.addRecord(3, "Sir Testalot");
        clearRecord.addRecord(5, "Est, Tee");
        clearRecord.addRecord(7, "Succest Tess Sr.");
        clearRecord.addRecord(11, "Succest Tess Jr.");
    }

    @Test
    public void testAddHistoryNotNull() {
        clearHistory.addHistory(clearRecord);

        ClearRecord record = clearHistory.getHistory();
        assertNotNull("Record is null", record);
    }

    @Test
    public void testAddHistoryCorrect() {
        clearHistory.addHistory(clearRecord);

        ClearRecord record = clearHistory.getHistory();
        assertEquals("Record returned was different", clearRecord, record);

    }

    @Test
    public void testGetHistoryNotNull() {
        clearHistory.addHistory(clearRecord);

        ClearRecord record = clearHistory.getHistory();
        assertNotNull("Record is null", record);
    }

    @Test
    public void testGetHistoryCorrect() {
        clearHistory.addHistory(clearRecord);

        ClearRecord record = clearHistory.getHistory();
        assertEquals("Records are not the same", clearRecord, record);
    }
}
