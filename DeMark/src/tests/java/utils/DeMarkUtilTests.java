package utils;

import actions.MarkAction;
import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.junit.Before;

import tests.java.TestingUtility;
import main.java.utils.DemarkUtil;

import java.util.ArrayList;
import java.util.List;

public class DeMarkUtilTests extends LightCodeInsightFixtureTestCase {
    Editor editor;
    Document document;

    int lineNum;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureByFile("MultipleLines.java");

        editor = myFixture.getEditor();
        document = myFixture.getEditor().getDocument();

        lineNum = TestingUtility.getCurrentCaretLine(myFixture);
    }

    @Override
    public String getTestDataPath() {
        return "src/tests/java/__testData__";
    }

    // ADD DEMARK BOOKMARK TESTS
    public void testAddDemarkBookmarkCorrectLine() {
        DemarkUtil.addDemarkBookmark(editor, lineNum);

        List<Bookmark> demarkBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);
        assertEquals("Bookmark not on the correct line", lineNum, demarkBookmarks.get(0).getLine());
    }

    public void testAddDemarkBookmarkSuccess() {
        DemarkUtil.addDemarkBookmark(editor, lineNum);

        List<Bookmark> demarkBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);
        assertEquals("Number of bookmarks incorrect", 1, demarkBookmarks.size());
    }

    public void testAddDemarkBookmarkCorrectDescription() {
        DemarkUtil.addDemarkBookmark(editor, lineNum);

        List<Bookmark> demarkBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);

        String description = demarkBookmarks.get(0).getDescription();
        assertEquals("Wrong description set for bookmark", DemarkUtil.DEMARK_INDICATOR, description);
    }

    public void testAddDemarkBookmarkTwoLinesCorrect() {
        DemarkUtil.addDemarkBookmark(editor, lineNum);
        TestingUtility.shiftCaretLine(myFixture, 3, false, true);

        int newLine = TestingUtility.getCurrentCaretLine(myFixture);
        DemarkUtil.addDemarkBookmark(editor, newLine);

        List<Bookmark> demarkBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);
        assertEquals("More than 2 bookmarks were set", 2, demarkBookmarks.size());

        List<Integer> lines = new ArrayList<>();     // assuming only two bookmarks are present
        lines.add(lineNum);
        lines.add(newLine);

        for (Bookmark bookmark : demarkBookmarks) {
            assertTrue("One of the line numbers are wrong", lines.contains(bookmark.getLine()));

            String description = demarkBookmarks.get(0).getDescription();
            assertEquals("One of the description is wrong", DemarkUtil.DEMARK_INDICATOR, description);
        }
    }

    // REMOVE DEMARK BOOKMARK
    public void testRemoveDemarkBookmarkCorrectLine() {
        myFixture.testAction(new MarkAction());

        DemarkUtil.removeDemarkBookmark(editor, lineNum);

        List<Bookmark> demarkBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);
        assertEquals("Bookmaark was not removed", 0, demarkBookmarks.size());
    }

    public void testRemoveDemarkBookmarkLineStays() {
        myFixture.testAction(new MarkAction());

        List<Bookmark> demarkBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);
        Bookmark bookmark = demarkBookmarks.get(0);
        String ogText = TestingUtility.getTextAtBookmark(myFixture, bookmark);

        DemarkUtil.removeDemarkBookmark(editor, lineNum);

        String text = TestingUtility.getTextOnLine(myFixture, lineNum);
        assertNotNull("Text was deleted", text);
        assertFalse("Text was deleted", text.isEmpty());
        assertEquals("Text was modified", ogText, text);
    }
}
