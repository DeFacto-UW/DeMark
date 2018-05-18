package actions.simpleTests;

import actions.ToggleAction;
import actions.MarkAction;
import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.junit.Before;
import tests.java.TestingUtility;

import java.util.List;

/**
 * Tests the ToggleAction on a simple, one line Java file.
 */
public class ToggleActionSimpleTests extends LightCodeInsightFixtureTestCase  {
    private static final String COMMENT_MARKER = "//";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureByFile("SimpleJava.java");
        myFixture.testAction(new MarkAction());
        myFixture.testAction(new ToggleAction());
    }

    @Override
    public String getTestDataPath() {
        return "src/tests/java/__testData__";
    }

    public void testToggleCommentMarkedLine() {
        // get the current line, which we have marked and toggled
        int caretLine = TestingUtility.getCurrentCaretLine(myFixture);
        String line = TestingUtility.getTextOnLine(myFixture, caretLine);

        // check if the line is commented out (i.e. starts with "//")
        assertTrue("Toggle did not comment out the current line that is marked", line.startsWith(COMMENT_MARKER));
    }

    public void testToggleAgainUncommentMarkedLine() {
        // do a toggle on the current line that is marked and commented out by toggle already
        myFixture.testAction(new ToggleAction());

        // get the current line
        int caretLine = TestingUtility.getCurrentCaretLine(myFixture);
        String line = TestingUtility.getTextOnLine(myFixture, caretLine);

        // check if the line is no longer commented out (i.e. no longer starting with "//")
        assertFalse("Toggle again did not uncomment the currently marked and commented line", line.startsWith(COMMENT_MARKER));
    }

    public void testToggleDoesNotRemoveDeMark() {
        List<Bookmark> deMarkBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);

        assertEquals("DeMark bookmark was removed", 1, deMarkBookmarks.size());
    }

    public void testToggleAddsCommentLevel() {
        // restoring the state of the file to it's original state
        myFixture.testAction(new ToggleAction());
        myFixture.testAction(new MarkAction());

        // moves the caret up a line to the line with the comment.
        TestingUtility.shiftCaretLine(myFixture, -1, false, true);
        int lineNum = TestingUtility.getCurrentCaretLine(myFixture);

        myFixture.testAction(new MarkAction());

        String textBeforeToggle = TestingUtility.getTextOnLine(myFixture, lineNum);

        myFixture.testAction(new ToggleAction());
        String textAfterToggle = TestingUtility.getTextOnLine(myFixture, lineNum);

        assertNotSame("Toggle did not change the text", textBeforeToggle, textAfterToggle);

        textAfterToggle = textAfterToggle.replaceAll("[ \t]", "");
        assertTrue("Toggle did not add comment level", textAfterToggle.startsWith(COMMENT_MARKER + "//"));
    }
}
