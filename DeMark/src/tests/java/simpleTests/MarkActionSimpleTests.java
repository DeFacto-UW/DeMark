package simpleTests;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.DocumentUtil;
import org.junit.Before;
import main.java.MarkAction;

import java.util.Arrays;
import java.util.List;

/**
 * Tests the MarkAction on a simple, one line Java file.
 */
public class MarkActionSimpleTests extends LightCodeInsightFixtureTestCase {
    //TODO: Clean up test code

    @Before
    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureByFile("SimpleJava.java");
        myFixture.testAction(new MarkAction());
    }

    @Override
    public String getTestDataPath() {
        return "src/tests/java/testData";
    }


    // ADDITION TESTS
    public void testMarkCorrectDescription() {
        // gets the bookmark that we are checking out
        BookmarkManager bookmarkManager = BookmarkManager.getInstance(myFixture.getProject());
        List<Bookmark> validBookmarks = bookmarkManager.getValidBookmarks();
        assertEquals("There are more than one bookmark in file.", 1, validBookmarks.size());
        // assuming there is only one bookmark
        Bookmark bookmark = validBookmarks.get(0);

        assertTrue("Bookmark does not have \"DeMark\" as part of the description.", bookmark.getDescription().contains("DeMark"));
    }

    public void testMarkCorrectLine() {
        // gets necessary details on current file
        Editor editor = myFixture.getEditor();
        Document document = editor.getDocument();
        int caretLine = document.getLineNumber(editor.getCaretModel().getVisualLineStart());

        // gets the bookmark that we are checking out
        BookmarkManager bookmarkManager = BookmarkManager.getInstance(myFixture.getProject());
        List<Bookmark> validBookmarks = bookmarkManager.getValidBookmarks();
        // assuming there is only one bookmark
        Bookmark bookmark = validBookmarks.get(0);

        assertEquals("Bookmark is on wrong line.", caretLine, bookmark.getLine());
    }

    public void testMarkOnlyCreatesOneBookMark() {
        // gets the bookmark that we are checking out
        BookmarkManager bookmarkManager = BookmarkManager.getInstance(myFixture.getProject());
        List<Bookmark> validBookmarks = bookmarkManager.getValidBookmarks();

        assertEquals("There are more than one bookmark in file.", 1, validBookmarks.size());
    }

    public void testMarkCreatesHighlight() {
        List<RangeHighlighter> validHighlights = Arrays.asList(myFixture.getEditor().getMarkupModel().getAllHighlighters());

        assertEquals("There are more than 1 highlight in the file.", 1, validHighlights.size());
    }

    public void testMarkCreatesHighlightOnCorrectLine() {
        Editor editor = myFixture.getEditor();
        Document document = editor.getDocument();
        int caretLine = document.getLineNumber(editor.getCaretModel().getVisualLineStart());
        int offset = DocumentUtil.getFirstNonSpaceCharOffset(editor.getMarkupModel().getDocument(), caretLine);


        List<RangeHighlighter> validHighlights = Arrays.asList(myFixture.getEditor().getMarkupModel().getAllHighlighters());
        RangeHighlighter highlighter = validHighlights.get(0);          // assuming only one highlighter

        assertTrue("The highlight is on the wrong line.", highlighter.getStartOffset() == offset && highlighter.getEndOffset() == offset);
    }

    // REMOVAL TESTS
    public void testUnmarkRemovesBookmark() {
        myFixture.testAction(new MarkAction());

        BookmarkManager bookmarkManager = BookmarkManager.getInstance(myFixture.getProject());
        List<Bookmark> bookmarks = bookmarkManager.getValidBookmarks();

        assertTrue("Bookmark not removed.", bookmarks.isEmpty());
    }

    public void testUnmarkRemovesHighlight() {
        myFixture.testAction(new MarkAction());

        Editor editor = myFixture.getEditor();
        Document document = editor.getDocument();
        int caretLine = document.getLineNumber(editor.getCaretModel().getVisualLineStart());
        int offset = DocumentUtil.getFirstNonSpaceCharOffset(editor.getMarkupModel().getDocument(), caretLine);

        List<RangeHighlighter> validHighlights = Arrays.asList(myFixture.getEditor().getMarkupModel().getAllHighlighters());

        // considers implicit highlighters as well
        // DeMark highlighters have TextAttributes with Gray_223, Gray_220 and startOffSet == endOffSet == offset
        int deMarkHighlights = 0;
        for (RangeHighlighter highlighter : validHighlights) {

            TextAttributes highlightAtt = highlighter.getTextAttributes();

            if (highlightAtt != null) {
                boolean sameLine = highlighter.getStartOffset() == caretLine && highlighter.getEndOffset() == offset;
                boolean sameTextAtt = highlightAtt.getBackgroundColor().equals(new JBColor(Gray._222, Gray._220));

                deMarkHighlights += sameLine && sameTextAtt ? 1 : 0;
            }
        }

        assertEquals("DeMark highlights not removed.", 0, deMarkHighlights);
    }
}