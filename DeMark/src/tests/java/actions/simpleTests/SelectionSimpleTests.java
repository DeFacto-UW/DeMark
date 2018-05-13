package actions.simpleTests;

import com.intellij.ide.bookmarks.Bookmark;
import tests.java.TestingUtility;
import actions.MarkAction;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.junit.Before;

import java.util.List;

public class SelectionSimpleTests extends LightCodeInsightFixtureTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureByFile("SimpleJavaSelection.java");
        myFixture.testAction(new MarkAction());
    }

    @Override
    public String getTestDataPath() {
        return "src/tests/java/testData";
    }

    public void testSelectionMarksCorrectLine() {
        List<Integer> selectedLines = TestingUtility.getSelectionStarts(myFixture);
        // assuming only one selected line
        int selectedLine = myFixture.getEditor().getDocument().getLineNumber(selectedLines.get(0));

        List<Bookmark> validBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);
        Bookmark bookmark = validBookmarks.get(0);

        assertEquals("Bookmark not on the correct line.", selectedLine, bookmark.getLine());
    }

    public void testSelectionMarkOnlyOneLine() {
        List<Bookmark> deMarkBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);
        assertEquals("Selection marked more than one line", 1, deMarkBookmarks.size());
    }

    public void testSelectionUnmark() {
        myFixture.testAction(new MarkAction());

        List<Bookmark> deMarkBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);
        assertTrue("Selection did not unmark bookmark.", deMarkBookmarks.isEmpty());
    }
}
