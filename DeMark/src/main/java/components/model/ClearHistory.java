package components.model;

import java.util.Stack;
import components.model.ClearRecord;

public class ClearHistory extends Stack<ClearRecord> {
    public ClearHistory() {
    }

    public ClearRecord getHistory() {
        if (this.size() > 0) {
            return this.pop();
        }
        return null;
    }

    public void addHistory(ClearRecord cr) {
        this.push(cr);
    }
}
