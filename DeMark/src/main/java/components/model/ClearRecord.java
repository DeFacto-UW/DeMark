package components.model;

import java.util.TreeMap;

/**
 * Storage component that Maps logical line numbers in the editor to
 * the text that is on that line.
 */
public class ClearRecord extends TreeMap<Integer, String> {
    public ClearRecord() {
    }

    /**
     * Adds a pair of line/text to the storage
     *
     * @param lineNum   the line number associated with the text
     * @param text      the text on the line number
     */
    public void addRecord(int lineNum, String text) {
        this.put(lineNum, text);
    }
}
