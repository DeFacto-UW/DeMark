import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.NotNullProducer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MarkAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        if (project == null) {
            return;
        }

        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        Document document = anActionEvent.getData(LangDataKeys.EDITOR).getDocument();
        Editor editor = anActionEvent.getData(LangDataKeys.EDITOR);

        // Get the current open file
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        String thisFileName = virtualFile.getName();

        int currLineNum = document.getLineNumber(editor.getSelectionModel().getSelectionStart());
        int startPos = document.getLineStartOffset(currLineNum);
        int endPos = document.getLineEndOffset(currLineNum);

        HighlightManager highlightManager = HighlightManager.getInstance(project);
        ArrayList<RangeHighlighter> highlighters = new ArrayList<RangeHighlighter>();
        TextAttributes ta = new TextAttributes();

        ta.setBackgroundColor(new JBColor(Gray._220, Gray._220));

        Color scrollMarkColor = null;
        highlightManager.addOccurrenceHighlight(editor, startPos, endPos, ta, 0, highlighters, scrollMarkColor);

        // TODO: Try and highlight a line??
        // TODO: Handle batch selection bookmarking

        // ISSUE: Bookmark persists through sessions but highlight does not.
        // ISSUE: No way to remove highlighted line yet.

        boolean removed = false;

        // Remove bookmark if already removed
        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();
        for (Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();
            String bookmarkFileName = bookmark.getFile().getName();

            // Line is already contained
            if (lineNum == currLineNum && bookmark.getDescription().equals("DeMark") && thisFileName.equals(bookmarkFileName)) {
                bookmarkManager.removeBookmark(bookmark);

                removed = true;
            }
        }

        // Add bookmark with description "DeMark"
        if (!removed) {
            bookmarkManager.addEditorBookmark(editor, currLineNum);
            Bookmark added = bookmarkManager.findEditorBookmark(document, currLineNum);
            if (added != null) {
                bookmarkManager.setDescription(added, "DeMark");
            }
        }
    }
}
