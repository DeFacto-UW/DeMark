package simpleTests;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import main.java.utils.DemarkUtil;
import org.junit.Before;

import actions.MarkAction;
import tests.java.TestingUtility;

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
        List<Bookmark> validBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);

        // assuming there is only one bookmark
        Bookmark bookmark = validBookmarks.get(0);

        assertTrue("Bookmark does not have \"DeMark\" as part of the description.", bookmark.getDescription().contains(DemarkUtil.DEMARK_INDICATOR));
    }

    public void testMarkCorrectLine() {
        // gets necessary details on current file
        int caretLine = TestingUtility.getCurrentCaretLine(myFixture);

        // gets the bookmark that we are checking out
        List<Bookmark> validBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);

        // assuming there is only one bookmark
        Bookmark bookmark = validBookmarks.get(0);

        assertEquals("Bookmark is on wrong line.", caretLine, bookmark.getLine());
    }

    public void testMarkOnlyCreatesOneBookMark() {
        // gets the bookmark that we are checking out
        List<Bookmark> validBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);

        assertEquals("There are more than one bookmark in file.", 1, validBookmarks.size());
    }

    public void testMarkCreatesHighlight() {
        List<RangeHighlighter> deMarkHighlights = TestingUtility.getDeMarkHighlighters(myFixture);

        assertEquals("There are more than 1 highlight in the file.", 1, deMarkHighlights.size());
    }

    public void testMarkCreatesHighlightOnCorrectLine() {
        int caretLine = TestingUtility.getCurrentCaretLine(myFixture);
        int offset = TestingUtility.getLineOffset(myFixture, caretLine);


        List<RangeHighlighter> deMarkHighlights = TestingUtility.getDeMarkHighlighters(myFixture);
        RangeHighlighter highlighter = deMarkHighlights.get(0);          // assuming only one highlighter

        assertTrue("The highlight is on the wrong line.", highlighter.getStartOffset() == offset && highlighter.getEndOffset() == offset);
    }

    // REMOVAL TESTS
    public void testUnmarkRemovesBookmark() {
        myFixture.testAction(new MarkAction());

        List<Bookmark> bookmarks = TestingUtility.getDeMarkBookmarks(myFixture);

        assertTrue("Bookmark not removed.", bookmarks.isEmpty());
    }

    public void testUnmarkRemovesHighlight() {
        myFixture.testAction(new MarkAction());

        List<RangeHighlighter> deMarkHighlights = TestingUtility.getDeMarkHighlighters(myFixture);

        assertTrue("DeMark highlights not removed.", deMarkHighlights.isEmpty());
    }
}