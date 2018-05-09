package actions.model;

import java.util.HashMap;

public class ClearRecord extends HashMap<Integer, String> {
    public ClearRecord() {
    }

    public void addRecord(int lineNum, String text) {
        this.put(lineNum, text);
    }
}
