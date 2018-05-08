package main.java.utils;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.DocumentUtil;
import main.java.utils.SelectionUtil;
import main.java.utils.DemarkUtil;
import main.java.utils.HighlightUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.TreeMap;

public class DemarkUtil {
    public static String DEMARK_INDICATOR = "DeMark";

    private Editor editor;                      // The existing editor
    private Project project;                    // The currently opened project
    private Document document;                  // The current file
    private BookmarkManager bookmarkManager;
    private HighlightUtil highlighterUtil;
    private SelectionUtil selectionUtil;

    public DemarkUtil(Editor editor, Project project, Document document) {
        this.editor = editor;
        this.document = document;
        this.project = project;
        this.bookmarkManager = BookmarkManager.getInstance(project);
        this.highlighterUtil = new HighlightUtil(this.editor, this.project, this.document);
        this.selectionUtil = new SelectionUtil(editor, project, document);
    }

    /**
     * Add a Demark bookmark to the current file at a specified line
     * @param lineNum, the line number to mark
     */
    public static void addDemarkBookmark(Editor editor, int lineNum) {
        Project project = editor.getProject();
        Document document = editor.getDocument();
        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);

        bookmarkManager.addEditorBookmark(editor, lineNum);
        Bookmark added = bookmarkManager.findEditorBookmark(document, lineNum);
        if (added != null) {
            bookmarkManager.setDescription(added, DEMARK_INDICATOR);
            HighlightUtil.addHighlight(editor, lineNum);
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
     * Display the name of the current file, as well as the lines within that file that
     * is marked with a DeMark bookmark
     *
     */
    public void displayDemarkedLines() {
        // TODO: Make this prettier
        JFrame frame = new JFrame("Display");
        JTextArea area = new JTextArea();
        area.setEditable(false);

        area.append("File name: " + this.getDocumentName() + "\n");

        TreeMap<Integer, String> demarks = getDemarks();

        for (Integer lineNum : demarks.keySet()) {
            String lineBody = demarks.get(lineNum);

            area.append("    line " + (lineNum + 1) + ": " + lineBody + "\n");
        }

        frame.add(area);
        frame.setSize(500,500);
        area.setBounds(500,500, 500,500);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Get all Demark bookmarks
     *
     * @return, a sorted map from line numbers to their corresponding line body
     */
    private TreeMap<Integer, String> getDemarks() {
        TreeMap<Integer, String> demarks = new TreeMap<>();

        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();

        for (Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();

            if (isDemarked(lineNum)) {
                TextRange textRange = DocumentUtil.getLineTextRange(document, lineNum);
                String lineBody = document.getText(textRange);

                demarks.put(lineNum, lineBody);
            }

        }
        return demarks;
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

    /**
     * Get the name of the current file
     * @return the name of the current file
     */
    private String getDocumentName() {
        VirtualFile vf = FileDocumentManager.getInstance().getFile(this.document);
        return vf.getName();
    }

}
