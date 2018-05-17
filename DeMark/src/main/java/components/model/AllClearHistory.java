package components.model;

import java.util.HashMap;
import components.model.ClearHistory;
import components.model.ClearRecord;

/**
 * Storage class for that Maps files to their clear history. Allows storing clear histories from separate files
 * in the same project.
 *
 * Uses: {@link ClearHistory} {@link ClearRecord}
 */
public class AllClearHistory extends HashMap<String, ClearHistory> {
    public AllClearHistory() {
    }

    /**
     * Gets the clear history of a file
     *
     * @param file the file to get the history for
     * @return {@link ClearHistory} that contains the history of a file
     */
    public ClearHistory getSingleHistory(String file) {
        return this.get(file);
    }

    /**
     * Adds a {@link ClearRecord} to the history of a file, allows for multiple clears in a row followed by
     * multiple unclears
     *
     * @param file  the file to add the history to
     * @param cr    the most recent history to the file
     */
    public void addSingleHistory(String file, ClearRecord cr) {
        ClearHistory ch = this.getSingleHistory(file);

        if (ch != null) {
            ch.addHistory(cr);
        } else {
            this.put(file, new ClearHistory());
            this.get(file).addHistory(cr);
        }
    }
}
