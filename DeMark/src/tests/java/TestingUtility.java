package tests.java;

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
     * @return a list of available DeMark bookmarks
     */
    public static List<Bookmark> getDeMarkBookmarks(@Nonnull JavaCodeInsightTestFixture fixture) {
        BookmarkManager bookmarkManager = BookmarkManager.getInstance(fixture.getProject());
        List<Bookmark> validBookmarks = bookmarkManager.getValidBookmarks();

        List<Bookmark> result = new ArrayList<Bookmark>();
        for (int i = 0; i < validBookmarks.size(); i++) {
            Bookmark currBookmark = validBookmarks.get(i);
            if (DemarkUtil.DEMARK_INDICATOR.equals(currBookmark.getDescription())) {
                result.add(currBookmark);
            }
        }
        return result;
    }

    /**
     *
     */
}
