package actions.model;

import java.util.TreeMap;

public class ClearRecord extends TreeMap<Integer, String> {
    public ClearRecord() {
    }

    public void addRecord(int lineNum, String text) {
        this.put(lineNum, text);
    }
}
