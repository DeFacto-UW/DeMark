package components.model;

import java.util.Stack;
import components.model.ClearRecord;

/**
 * A stack that contains {@link ClearRecord}.
 * The top of the stack is the most recent record, the bottom is the least recent
 *
 * Uses: {@link ClearRecord}
 */
public class ClearHistory extends Stack<ClearRecord> {
    public ClearHistory() {
    }

    /**
     * Gets the most recent record
     *
     * @return {@link ClearRecord} that is the most recent
     */
    public ClearRecord getHistory() {
        if (this.size() > 0) {
            return this.pop();
        }
        return null;
    }

    /**
     * Adds a record to the top of the stack
     * @param cr    {@link ClearRecord} to be added
     */
    public void addHistory(ClearRecord cr) {
        this.push(cr);
    }
}
