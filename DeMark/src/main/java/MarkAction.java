package main.java;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
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
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.List;

public class MarkAction extends AnAction {

    private BookmarkManager bookmarkManager;
    private Document document;
    private Editor editor;


    private Set<RangeHighlighter> highlighters = new HashSet<RangeHighlighter>();

    public void update(AnActionEvent e) {
        //perform action if and only if EDITOR != null
        boolean enabled = e.getData(CommonDataKeys.EDITOR) != null;
        e.getPresentation().setEnabled(enabled);
    }

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

        // Find the line start positions of selected text
        ArrayList<Integer> lineStarts = getSelectionStarts();
        int countMarked = 0;

        // Count the number of lines marked
        for (int i = 0; i < lineStarts.size(); i++) {
            int lineNum = document.getLineNumber(lineStarts.get(i));
           if (lineAlreadyMarked(lineNum, thisFileName)) {
              countMarked++;
           }
        }

        // Toggle on and off bookmarks
        for (int i = 0; i < lineStarts.size(); i++) {
            int lineNum = document.getLineNumber(lineStarts.get(i));

            if (countMarked != lineStarts.size()) {
                if (!lineAlreadyMarked(lineNum, thisFileName)) {
                    addDemarkBookmark(lineNum);
                }
            } else {
                removeDemarkBook(lineNum, thisFileName);
            }
        }
    }

    /**
     *
     * @param lineNum, the line to remove the Demark bookmark from
     * @param fileName, the name of the file that contains the Demark bookmark (ie. currently shown)
     */
    private void removeDemarkBook(int lineNum, String fileName) {
        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();
        for (Bookmark bookmark : bookmarkList) {
            int currLine = bookmark.getLine();
            String bookmarkFileName = bookmark.getFile().getName();

            // Line is already contained
            if (currLine == lineNum && bookmark.getDescription().equals("DeMark") && fileName.equals(bookmarkFileName)) {
                removeHighlight(lineNum);
                bookmarkManager.removeBookmark(bookmark);
            }
        }
    }

    // Add a DeMark bookmark and add highlighting
    private void addDemarkBookmark(int lineNum) {
        bookmarkManager.addEditorBookmark(editor, lineNum);
        Bookmark added = bookmarkManager.findEditorBookmark(document, lineNum);
        if (added != null) {
            bookmarkManager.setDescription(added, "DeMark");

        }
        addHighlight(lineNum);
    }

    /**
     * Determine if the a line is already marked by Demark plugin
     * @param lineChecked, the line to cross check
     * @param filename, the file name that the line is in
     * @return true if line contained, false otherwise
     */
    private boolean lineAlreadyMarked(int lineChecked, String filename) {
        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();
        for (Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();
            String bookmarkFileName = bookmark.getFile().getName();

            //TODO: Optimize this so we don't have nested loops
            // Line is already contained
            if (lineChecked == lineNum && bookmark.getDescription().equals("DeMark") && filename.equals(bookmarkFileName)) {
                return true;
            }
        }
        return false;
    }

    // initializes all fields
    private void init(Project project, @NotNull AnActionEvent anActionEvent) {
        bookmarkManager = BookmarkManager.getInstance(project);
        document = anActionEvent.getData(LangDataKeys.EDITOR).getDocument();
        editor = anActionEvent.getData(LangDataKeys.EDITOR);
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
        // Offset of a line
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

    /**
     * Find the start positions of each line of the editors currently selected text
     * @return ArrayList of line start positions
     */
    private ArrayList<Integer> getSelectionStarts() {
        // Character position of selected model
        int selectPosStart = editor.getSelectionModel().getSelectionStart();
        int selectPosEnd = editor.getSelectionModel().getSelectionEnd();

        int currPos = selectPosStart;
        ArrayList<Integer> lineStarts = new ArrayList<>();

        // Add all line starts positions to the array
        while (currPos <= selectPosEnd) {
            lineStarts.add(currPos);
            int lineNum = document.getLineNumber(currPos);

            // Calculate the offset from start of line to end
            int startOffset = document.getLineStartOffset(lineNum);
            int endOffset = document.getLineEndOffset(lineNum);

            // Position of next line is 1 over currPos + offSet
            int offSet = endOffset - startOffset;
            currPos += offSet + 1;
        }
        return lineStarts;
    }
}
