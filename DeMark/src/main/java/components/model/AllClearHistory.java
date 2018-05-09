package components.model;

import java.util.HashMap;
import components.model.ClearHistory;
import components.model.ClearRecord;

public class AllClearHistory extends HashMap<String, ClearHistory> {
    public AllClearHistory() {
    }

    public ClearHistory getSingleHistory(String file) {
        return this.get(file);
    }

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
