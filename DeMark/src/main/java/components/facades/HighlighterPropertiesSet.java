package components.facades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HighlighterPropertiesSet extends ArrayList<HighlighterProperties> {

    public List<HighlighterProperties> getHighlighterProperties() {
        return this;
    }

    /**
     * We need to have this setter for the xml deserialization to work
     *
     * @param highlighterProperties
     */
    public void setHighlighterProperties(List<HighlighterProperties> highlighterProperties) {
        if (this == highlighterProperties) return;

        this.clear();
        this.addAll(highlighterProperties);
    }

    @Override
    public HighlighterPropertiesSet clone() {
        return (HighlighterPropertiesSet) super.clone();
    }
}
