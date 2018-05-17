package actions.simpleTests;

import actions.ToggleAction;
import actions.MarkAction;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.junit.Before;
import tests.java.TestingUtility;

/**
 * Tests the ToggleAction on a simple, one line Java file.
 */
public class ToggleActionSimpleTest extends LightCodeInsightFixtureTestCase  {
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
}
