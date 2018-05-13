package actions.complexTests;

import actions.MarkAction;
import com.intellij.ide.bookmarks.Bookmark;
import tests.java.TestingUtility;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.junit.Before;

import java.util.List;

public class MarkAction_SingleFile_MultiLines extends LightCodeInsightFixtureTestCase {
    private int caretLine;

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public String getTestDataPath() {
        return "src/tests/java/__testData__";
    }



    public void testTwoLinesMarked() {
        init("MultipleLines.java");

        myFixture.testAction(new MarkAction());

        TestingUtility.shiftCaretLine(myFixture, 3, false, true);
        myFixture.testAction(new MarkAction());

        List<Bookmark> deMarkBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);
        assertEquals("Not correct number of bookmarks", 2, deMarkBookmarks.size());
    }

    public void testTwoLinesCorrect() {
        init("MultipleLines.java");

        myFixture.testAction(new MarkAction());

        TestingUtility.shiftCaretLine(myFixture, 3, false, true);
        myFixture.testAction(new MarkAction());

        List<Bookmark> deMarkBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);
        for (Bookmark bookmark : deMarkBookmarks) {
            String text = TestingUtility.getTextAtBookmark(myFixture, bookmark);

            String message = "Bookmark on line " + bookmark.getLine();
            assertTrue(message + " is empty.", !text.isEmpty());                        // line is not empty
            assertTrue(message + " is incorrect.", text.startsWith("System.out."));     // correct line is marked
            assertTrue(message + " is incorrect.", text.contains("test"));              // correct println line is marked
        }
    }

    public void testUnmarkOneLine() {
        init("MultipleLines.java");

        myFixture.testAction(new MarkAction());

        TestingUtility.shiftCaretLine(myFixture, 3, false, true);
        myFixture.testAction(new MarkAction());

        TestingUtility.moveCaretToLineEnd(myFixture, 5);
        myFixture.testAction(new MarkAction());

        List<Bookmark> deMarkBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);
        assertEquals("Bookmark not removed.", 1, deMarkBookmarks.size());
        assertTrue("Wrong line is unmarked.", TestingUtility.getTextAtBookmark(myFixture, deMarkBookmarks.get(0)).contains("test2"));
    }

    private void init(String fileName) {
        myFixture.configureByFile(fileName);

        int expectedCaretLine;
        switch (fileName) {
            case "MultipleLines.java":
                expectedCaretLine = 3;
                break;
            case "MultiPrintln.java":
                expectedCaretLine = 2;
                break;
            default:
                expectedCaretLine = 0;
        }

        caretLine = TestingUtility.getCurrentCaretLine(myFixture);
        assertEquals(expectedCaretLine, caretLine);
    }
}
