package main.java.utils;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.DocumentUtil;

import java.util.HashSet;
import java.util.Set;

public class HighlightUtil {
    private Editor editor;
    private Set<RangeHighlighter> highlighters;

    public HighlightUtil(Editor editor) {
        this.editor = editor;
        this.highlighters = new HashSet<>();
    }

    // NOTES ON HIGHLIGHTING:
    // There are multiple highlighting layers. This can be found in HighlighterLayer.java in the SDK.
    // We are using the layer directly below the selection layer, which is SELECTION - 1 or LAST - 1
    // We don't want to remove the implicit highlighters cause that might break things.
    // We don't need to use highlight manager to do things, just straight up use the editor markup model
    // Currently we are only using the line numbers and the list of highliters that we added to determine
    // TODO: figure out a good way to distinguish our Highlighter from other people's (including implicit ones)
    // TODO: Fix highlighting to persist through multiple actions to be able to remove

    // removes a highlight on the current line
    public void removeHighlight(int currentLine) {
        // Offset of a line
        int offset = DocumentUtil.getFirstNonSpaceCharOffset(editor.getMarkupModel().getDocument(), currentLine);
        for (RangeHighlighter highlighter : highlighters) {
            if (highlighter.getStartOffset() == offset &&
                    highlighter.getEndOffset() == offset) {
                editor.getMarkupModel().removeHighlighter(highlighter);
                break;
            }
        }
    }

    // adds highlight on current line.
    public void addHighlight(int currentLine) {
        TextAttributes ta = new TextAttributes();
        ta.setBackgroundColor(new JBColor(Gray._222, Gray._220));

        RangeHighlighter highlighter = editor.getMarkupModel().addLineHighlighter(currentLine, HighlighterLayer.LAST - 1, ta);
        highlighters.add(highlighter);
    }

}
