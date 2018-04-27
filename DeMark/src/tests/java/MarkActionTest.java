import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.junit.Before;
import main.java.MarkAction;

import java.util.List;

public class MarkActionTest extends LightCodeInsightFixtureTestCase {
    //TODO: Clean up test code

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public String getTestDataPath() {
        return "src/tests/java/testData";
    }

    public void testMarkCorrectDescription() {
        myFixture.configureByFile("SimpleJava.java");
        myFixture.testAction(new MarkAction());

        // gets the bookmark that we are checking out
        BookmarkManager bookmarkManager = BookmarkManager.getInstance(myFixture.getProject());
        List<Bookmark> validBookmarks = bookmarkManager.getValidBookmarks();
        assertEquals("There are more than one bookmark in file.", 1, validBookmarks.size());
        // assuming there is only one bookmark
        Bookmark bookmark = validBookmarks.get(0);

        assertTrue("Bookmark does not have \"DeMark\" as part of the description.", bookmark.getDescription().contains("DeMark"));
    }

    public void testMarkCorrectLine() {
        myFixture.configureByFile("SimpleJava.java");
        myFixture.testAction(new MarkAction());

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
        myFixture.configureByFile("SimpleJava.java");
        myFixture.testAction(new MarkAction());

        // gets the bookmark that we are checking out
        BookmarkManager bookmarkManager = BookmarkManager.getInstance(myFixture.getProject());
        List<Bookmark> validBookmarks = bookmarkManager.getValidBookmarks();

        assertEquals("There are more than one bookmark in file.", 1, validBookmarks.size());
    }
}