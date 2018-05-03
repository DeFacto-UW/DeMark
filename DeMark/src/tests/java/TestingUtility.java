package tests.java;

import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.DocumentUtil;
import main.java.utils.DemarkUtil;
import main.java.utils.SelectionUtil;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to assist in writing tests for a plugin developed in the IntelliJ Platform SDK
 * This class adds a level of abstraction between the actual tests and several needed functions that
 * needs to be performed on the TestFixture.
 *
 * This allows getting the lines, moving around in the in-memory editor, and so on
 *
 * @author Andrew
 */
public class TestingUtility {

    /**
     * Get the line number the caret is currently on
     *
     * @param fixture the current TestFixture we are looking at
     * @return the line number the caret is currently on
     */
    public static int getCurrentCaretLine(@Nonnull JavaCodeInsightTestFixture fixture) {
        Editor editor = fixture.getEditor();
        Document document = editor.getDocument();

        return document.getLineNumber(editor.getCaretModel().getVisualLineStart());
    }

    /**
     * Returns the current line's offset in the file
     * @param fixture
     * @param currentLine
     * @return the offset of the current line
     */
    public static int getLineOffset(@Nonnull JavaCodeInsightTestFixture fixture, int currentLine) {
        return DocumentUtil.getFirstNonSpaceCharOffset(fixture.getEditor().getMarkupModel().getDocument(), currentLine);
    }

    /**
     * Find the start positions of each line currently selected in the editor of the Fixture
     *
     * @param fixture
     * @return List that is the start positions of each line currently selected
     */
    public static List<Integer> getSelectionStarts(@Nonnull JavaCodeInsightTestFixture fixture) {
        SelectionUtil selectionUtil =
                new SelectionUtil(fixture.getEditor(), fixture.getProject(), fixture.getEditor().getDocument());

        return selectionUtil.getSelectionStarts();
    }

    /**
     * Gets the list of bookmarks specifically tied to DeMark that are currently available in the fixture
     *
     * @param fixture
     * @return a list of available DeMark bookmarks, can be empty
     */
    public static List<Bookmark> getDeMarkBookmarks(@Nonnull JavaCodeInsightTestFixture fixture) {
        BookmarkManager bookmarkManager = BookmarkManager.getInstance(fixture.getProject());
        List<Bookmark> validBookmarks = bookmarkManager.getValidBookmarks();

        List<Bookmark> result = new ArrayList<Bookmark>();
        for (Bookmark currBookmark : validBookmarks) {
            if (DemarkUtil.DEMARK_INDICATOR.equals(currBookmark.getDescription())) {
                result.add(currBookmark);
            }
        }
        return result;
    }

    /**
     * Gets the list of highlighters specifically tied to DeMark that are currently in the fixture
     *
     * @param fixture
     * @return a list of available DeMark highlighters, can be empty.
     */
    public static List<RangeHighlighter> getDeMarkHighlighters(@Nonnull JavaCodeInsightTestFixture fixture) {
        List<RangeHighlighter> result = new ArrayList<RangeHighlighter>();
        RangeHighlighter[] highlighters = fixture.getEditor().getMarkupModel().getAllHighlighters();

        int caretLine = getCurrentCaretLine(fixture);
        int offset = getLineOffset(fixture, caretLine);

        for (RangeHighlighter highlighter : highlighters) {
            TextAttributes highlightAtt = highlighter.getTextAttributes();

            if (highlightAtt != null) {
                boolean sameLine = highlighter.getStartOffset() == offset && highlighter.getEndOffset() == offset;
                boolean sameTextAtt = highlightAtt.getBackgroundColor().equals(new JBColor(Gray._222, Gray._220));

                if (sameLine && sameTextAtt) {
                    result.add(highlighter);
                }
            }
        }

        return result;
    }

    /**
     * Shifts the caret down or up relative to its current position
     *
     * @param fixture
     * @param lineShift
     * @param withSelection
     * @param scrollToCaret
     */
    public static void moveCaretLine(@Nonnull JavaCodeInsightTestFixture fixture,
                                     int lineShift, boolean withSelection, boolean scrollToCaret) {
        fixture.getEditor().getCaretModel().getCurrentCaret().moveCaretRelatively(0, lineShift, withSelection, scrollToCaret);
    }

    /**
     * Shifts the caret to the right or left relative to its current position
     *
     * Avoid using because of wrapping
     * @param fixture
     * @param columnShift
     * @param withSelection
     * @param scrollToCaret
     */
    public static void moveCaretColumn(@Nonnull JavaCodeInsightTestFixture fixture,
                                       int columnShift, boolean withSelection, boolean scrollToCaret) {
        fixture.getEditor().getCaretModel().getCurrentCaret().moveCaretRelatively(columnShift, 0, withSelection, scrollToCaret);
    }

    /**
     * Moves the caret to a specific offset, takes in account soft wrapping
     *
     * @param fixture
     * @param offset
     */
    public static void moveCaretToOffset(@Nonnull JavaCodeInsightTestFixture fixture, int offset) {
        fixture.getEditor().getCaretModel().getCurrentCaret().moveToOffset(offset);
    }

    /**
     * Selects text between two offsets
     *
     * @param fixture
     * @param selectionStartOffset
     * @param selectionEndOffset
     */
    public static void selectOffsets(@Nonnull JavaCodeInsightTestFixture fixture, int selectionStartOffset, int selectionEndOffset) {
        fixture.getEditor().getCaretModel().getCurrentCaret().setSelection(selectionStartOffset, selectionEndOffset);
    }

    /**
     * Selects the line that the caret is currently on
     *
     * @param fixture
     */
    public static void selectLineAtCaret(@Nonnull JavaCodeInsightTestFixture fixture) {
        fixture.getEditor().getCaretModel().getCurrentCaret().selectLineAtCaret();
    }
}
