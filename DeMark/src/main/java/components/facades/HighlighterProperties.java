package components.facades;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Need to have a @Deprecated default constructor
// Maybe need to implement PersistentStateComponent
public class HighlighterProperties {
    private int lineOffset;
    private int highlightLayer;
    private int colorOne;
    private int colorTwo;

    public HighlighterProperties(final int line, final int highlightLayer, final int colorOne, final int colorTwo) {
        this.lineOffset = line;
        this.highlightLayer = highlightLayer;
        this.colorOne = colorOne;
        this.colorTwo = colorTwo;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (o instanceof HighlighterProperties) {
//            HighlighterProperties other = (HighlighterProperties) o;
//
//            return this.lineOffset == other.lineOffset;
//        }
//        return false;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = 37;
//        result = 31 * result + this.lineOffset * 3;
//
//        return result;
//    }

    @Override
    public String toString() {
        return "los: " + lineOffset;
    }

    @Deprecated
    public HighlighterProperties() {
    }

//    @Override
//    public int compareTo(@NotNull HighlighterProperties o) {
//        return this.lineOffset - o.lineOffset;
//    }
}
