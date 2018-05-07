package main.java.utils;

import org.jetbrains.annotations.NotNull;

// Need to have a @Deprecated default constructor
// Maybe need to implement PersistentStateComponent
public class HighlighterProperties implements Comparable<HighlighterProperties> {
    private int lineOffset;
    private int highlightLayer;
    private int colorOne;
    private int colorTwo;

    public HighlighterProperties(@NotNull int line, @NotNull int highlightLayer,@NotNull int colorOne,@NotNull int colorTwo) {
        this.lineOffset = line;
        this.highlightLayer = highlightLayer;
        this.colorOne = colorOne;
        this.colorTwo = colorTwo;
    }

    @Override
    public boolean equals(Object o) {
       if (o instanceof HighlighterProperties) {
           HighlighterProperties other = (HighlighterProperties) o;
          return this.lineOffset == other.lineOffset
                  && this.highlightLayer == other.highlightLayer
                  && this.colorOne == other.colorOne
                  && this.colorTwo == other.colorTwo;
       }

       return false;
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 31 * result + lineOffset * 11;
        result = 31 * result + highlightLayer * 3;

        result = 31 * result + colorOne * 5;
        result = 31 * result + colorTwo * 7;

        return result;
    }

    @Override
    public int compareTo(@NotNull HighlighterProperties highlighterProperties) {
        return this.lineOffset - highlighterProperties.lineOffset;
    }
}
