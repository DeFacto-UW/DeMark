package main.java;

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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import java.util.*;
import java.util.List;


public class MarkAction extends AnAction {

    private BookmarkManager bookmarkManager;
    private HighlightManager highlightManager;

    private Document document;
    private Editor editor;


    private Set<RangeHighlighter> highlighters = new HashSet<RangeHighlighter>();

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        if (project == null) {
            return;
        }
        init(project, anActionEvent);
        // Get the current open file
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        String thisFileName = virtualFile.getName();

        int currentLine = document.getLineNumber(editor.getSelectionModel().getSelectionStart());

        // TODO: Handle batch selection bookmarking

        // ISSUE: Bookmark persists through sessions but highlight does not.

        boolean removed = false;

        // Remove bookmark if already exists
        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();
        for (Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();
            String bookmarkFileName = bookmark.getFile().getName();

            //TODO: Optimize this so we don't have nested loops
            // Line is already contained
            if (lineNum == currentLine && bookmark.getDescription().equals("DeMark") && thisFileName.equals(bookmarkFileName)) {
                removeHighlight(currentLine);
                bookmarkManager.removeBookmark(bookmark);

                removed = true;
            }
        }

        // Add bookmark with description "DeMark"
        if (!removed) {
            bookmarkManager.addEditorBookmark(editor, currentLine);
            Bookmark added = bookmarkManager.findEditorBookmark(document, currentLine);
            if (added != null) {
                bookmarkManager.setDescription(added, "DeMark");
            }
            addHighlight(currentLine);
        }
    }

    // initializes all fields
    private void init(Project project, AnActionEvent anActionEvent) {
        bookmarkManager = BookmarkManager.getInstance(project);
        document = anActionEvent.getData(LangDataKeys.EDITOR).getDocument();
        editor = anActionEvent.getData(LangDataKeys.EDITOR);

        highlightManager = HighlightManager.getInstance(project);
    }

    // removes a highlight on the current line
    private void removeHighlight(int currentLine) {
        for (RangeHighlighter highlighter : highlighters) {
            if (highlighter.getStartOffset() == document.getLineStartOffset(currentLine) &&
                    highlighter.getEndOffset() == document.getLineEndOffset(currentLine)) {
                highlightManager.removeSegmentHighlighter(editor, highlighter);
                break;
            }
        }
    }

    // adds highlight on current line.
    private void addHighlight(int currentLine) {
        int startPos = document.getLineStartOffset(currentLine);
        int endPos = document.getLineEndOffset(currentLine);

        TextAttributes ta = new TextAttributes();
        ta.setBackgroundColor(new JBColor(Gray._220, Gray._220));

        highlightManager.addOccurrenceHighlight(editor, startPos, endPos, ta, 0, highlighters, null);
    }
}
