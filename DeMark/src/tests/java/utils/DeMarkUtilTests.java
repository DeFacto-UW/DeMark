package utils;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.junit.Before;
import org.junit.Test;

import tests.java.TestingUtility;
import main.java.utils.DemarkUtil;

import java.util.List;

public class DeMarkUtilTests extends LightCodeInsightFixtureTestCase {
    Editor editor;
    Document document;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureByFile("MultipleLines.java");

        editor = myFixture.getEditor();
        document = myFixture.getEditor().getDocument();
    }

    @Override
    public String getTestDataPath() {
        return "src/tests/java/__testData__";
    }

    public void testAddDemarkBookmarkCorrectLine() {
        int lineNum = TestingUtility.getCurrentCaretLine(myFixture);

        DemarkUtil.addDemarkBookmark(editor, lineNum);

        List<Bookmark> demarkBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);
        assertEquals("Bookmark not on the correct line", lineNum, demarkBookmarks.get(0).getLine());
    }

    public void testAddDemarkBookmarkSuccess() {
        int lineNum = TestingUtility.getCurrentCaretLine(myFixture);

        DemarkUtil.addDemarkBookmark(editor, lineNum);

        List<Bookmark> demarkBookmarks = TestingUtility.getDeMarkBookmarks(myFixture);
        assertEquals("Number of bookmarks incorrect", 1, demarkBookmarks.size());
    }
}
