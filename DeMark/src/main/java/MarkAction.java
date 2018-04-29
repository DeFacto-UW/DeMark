package main.java;

import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.DocumentUtil;

import java.awt.*;
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

    // NOTES ON HIGHLIGHTING:
    // There are multiple highlighting layers. This can be found in HighlighterLayer.java in the SDK.
    // We are using the layer directly below the selection layer, which is SELECTION - 1 or LAST - 1
    // We don't want to remove the implicit highlighters cause that might break things.
    // We don't need to use highlight manager to do things, just straight up use the editor markup model
    // Currently we are only using the line numbers and the list of highliters that we added to determine
    //TODO: figure out a good way to distinguish our Highlighter from other people's (including implicit ones)

    // removes a highlight on the current line
    private void removeHighlight(int currentLine) {
        int offset = DocumentUtil.getFirstNonSpaceCharOffset(editor.getMarkupModel().getDocument(), currentLine);
        for (RangeHighlighter highlighter : highlighters) {
            if (highlighter.getStartOffset() == offset &&
                    highlighter.getEndOffset() == offset) {
                editor.getMarkupModel().removeHighlighter(highlighter);
                break;
            }
        }
    }

    // adds highlight on current line.
    private void addHighlight(int currentLine) {
        TextAttributes ta = new TextAttributes();
        ta.setBackgroundColor(new JBColor(Gray._222, Gray._220));

        RangeHighlighter highlighter = editor.getMarkupModel().addLineHighlighter(currentLine, HighlighterLayer.LAST - 1, ta);
        highlighters.add(highlighter);
    }
}
