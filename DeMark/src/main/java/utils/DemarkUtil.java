package main.java.utils;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

import java.util.List;

public class DemarkUtil {
    public static String DEMARK_INDICATOR = "DeMark";

    private Editor editor;                      // The existing editor
    private Project project;                    // The currently opened project
    private Document document;                  // The current file
    private BookmarkManager bookmarkManager;
    private main.java.utils.HighlightUtil highlighterUtil;
    private main.java.utils.SelectionUtil selectionUtil;

    public DemarkUtil(Editor editor, Project project, Document document) {
        this.editor = editor;
        this.document = document;
        this.project = project;
        this.bookmarkManager = BookmarkManager.getInstance(project);
        this.highlighterUtil = new main.java.utils.HighlightUtil(this.editor, this.project, this.document);
        this.selectionUtil = new main.java.utils.SelectionUtil(editor, project, document);
    }

    /**
     * Add a Demark bookmark to the current file at a specified line
     * @param lineNum, the line number to mark
     */
    public void addDemarkBookmark(int lineNum) {
        bookmarkManager.addEditorBookmark(editor, lineNum);
        Bookmark added = bookmarkManager.findEditorBookmark(document, lineNum);
        if (added != null) {
            bookmarkManager.setDescription(added, DEMARK_INDICATOR);
            highlighterUtil.addHighlight(lineNum);
        }
    }

    /**
     * Remove a Demark bookmark from an existing line in the current file
     *
     * @param lineNum, the line number to remove the Demark bookmark from
     */
    public void removeDemarkBookmark(int lineNum) {
        Bookmark res = bookmarkManager.findEditorBookmark(this.document, lineNum);

        if (res != null && res.getDescription().equals(DEMARK_INDICATOR)) {
            bookmarkManager.removeBookmark(res);
            highlighterUtil.removeHighlight(lineNum);
        }
    }

    /**
     * Removes a Demark bookmark and deletes the corresponding line from the current file
     *
     * @param lineNum, the line number to delete and remove the Demark bookmark from
     */
    public void clearDemarkBookmark(int lineNum) {
        Bookmark res = bookmarkManager.findEditorBookmark(this.document, lineNum);

        if (res != null && res.getDescription().equals(DEMARK_INDICATOR)) {
            bookmarkManager.removeBookmark(res);
            highlighterUtil.removeHighlight(lineNum);
            selectionUtil.removeLine(lineNum);
        }
    }

    /**
     * Removes all Demark bookmarks and deletes the corresponding lines with it from the current file
     */
    public void clearAllDemarkBookmarks() {
        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();

        for (Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();

            // Remove all bookmarks with Demark in this file
            if (isDemarked(lineNum)) {
                bookmarkManager.removeBookmark(bookmark);
                highlighterUtil.removeHighlight(lineNum);
                selectionUtil.removeLine(lineNum);
            }
        }
    }

    public void toggleDemarkComment() {
        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();

        for (Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();
            if (isDemarked(lineNum)) {
               if (selectionUtil.isCommented(lineNum)) {
                   selectionUtil.removeComment(lineNum);
               } else {
                   selectionUtil.addComment(lineNum);
               }
            }
        }
    }

    /**
     * Determine if the a line is already marked by Demark plugin
     *
     * @param lineNum, the line to cross check
     * @return true if line is already marked, false otherwise
     */
    public boolean isDemarked(int lineNum) {
        Bookmark bookmark = bookmarkManager.findEditorBookmark(document, lineNum);
        return bookmark != null && bookmark.getDescription().equals(DEMARK_INDICATOR);
    }
}
