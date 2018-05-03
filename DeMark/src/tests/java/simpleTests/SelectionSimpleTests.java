package simpleTests;

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
        int caretLine = TestingUtility.getCurrentCaretLine(myFixture);

        List<Bookmark> validBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);
        Bookmark bookmark = validBookmarks.get(0);

        assertEquals("Bookmark not on the correct line.", caretLine, bookmark.getLine());
    }
}
