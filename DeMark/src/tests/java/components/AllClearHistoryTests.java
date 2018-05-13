package components;

import components.model.AllClearHistory;
import components.model.ClearHistory;
import components.model.ClearRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class AllClearHistoryTests {
    AllClearHistory history;
    ClearHistory clearHistory;
    ClearRecord clearRecord;

    @Before
    public void init() {
        history = new AllClearHistory();
        clearHistory = new ClearHistory();
        clearRecord = new ClearRecord();
    }

    @Test
    public void testAddSingleHistory() {
        String file = "src/file/thisFile.file";
        clearRecord.addRecord(1, "This is a line");

        history.computeIfAbsent(file, k -> new ClearHistory());
        history.get(file).addHistory(clearRecord);
    }
}
