package main.java.utils;

import actions.model.ClearRecord;
import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.DocumentUtil;

import main.java.utils.HighlightUtil;
import main.java.utils.SelectionUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class DemarkUtil {
    public static String DEMARK_INDICATOR = "DeMark";


    /**
     *  Mark a line at a given line number
     *
     * @param editor, the editor to add the mark to
     * @param lineNum, the line number to add the mark to
     */
    public static void addDemarkBookmark(@Nonnull Editor editor, int lineNum) {
        Project project = editor.getProject();
        Document document = editor.getDocument();

        if (project == null) {
            return;
        }

        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        bookmarkManager.addEditorBookmark(editor, lineNum);
        Bookmark added = bookmarkManager.findEditorBookmark(document, lineNum);

        if (added != null) {
            bookmarkManager.setDescription(added, DEMARK_INDICATOR);
            HighlightUtil.addHighlight(editor, lineNum);
        }
    }

    /**
     * Remove a mark from a given the line number
     *
     * @param editor, the editor to remove the mark from
     * @param lineNum, the line number to remove the mark from
     */
    public static void removeDemarkBookmark(@Nonnull Editor editor, int lineNum) {
        Project project = editor.getProject();
        Document document = editor.getDocument();

        if (project == null) {
            return;
        }

        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        Bookmark res = bookmarkManager.findEditorBookmark(document, lineNum);

        if (res != null && res.getDescription().equals(DEMARK_INDICATOR)) {
            bookmarkManager.removeBookmark(res);
            HighlightUtil.removeHighlight(editor, lineNum);
        }
    }

    /**
     * Toggle a marked line by either commenting or uncommenting the line
     *
     * @param editor, the editor to toggle the comments from
     */
    public static void toggleDemarkComment(@NotNull Editor editor) {
        Project project = editor.getProject();

        if (project == null) {
            return;
        }

        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();

        for (Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();
            if (isDemarked(editor, lineNum)) {
                if (SelectionUtil.isCommented(editor, lineNum)) {
                    SelectionUtil.removeComment(editor, lineNum);
                } else {
                    SelectionUtil.addComment(editor, lineNum);
                }
            }
        }
    }

    /**
     * Display the marked lines for the current editor
     *
     * @param editor, the current editor that contains all the bookmarks
     */
    public static void displayDemarkedLines(@Nonnull Editor editor) {

        // TODO: Make this prettier
        JFrame frame = new JFrame("Display");
        JTextArea area = new JTextArea();
        area.setEditable(false);

        Document document = editor.getDocument();
        area.append("File name: " + getDocumentName(document) + "\n");

        TreeMap<Integer, String> demarks = getDemarks(editor);

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
     * Removes all Demark bookmarks and deletes the corresponding lines with it from the current file
     *
     * @param editor, the editor that contains the marked lines
     *
     * @return Hashmap of all cleared marked lines. Line number -> line body
     */
    public static ClearRecord clearAllDemarkBookmarks(@Nonnull Editor editor) {
        Project project = editor.getProject();
        ClearRecord cr = new ClearRecord();
        if (project == null) {
            return cr;
        }

        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();
        cr = getClearRecord(editor, bookmarkList);

        for (Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();

            // Remove all bookmarks with Demark in this file
            if (isDemarked(editor, lineNum)) {
                bookmarkManager.removeBookmark(bookmark);
                HighlightUtil.removeHighlight(editor, lineNum);
                SelectionUtil.removeLine(editor, lineNum);
            }
        }
        return cr;
    }

    /**
     * Add all lines from last clear
     *
     * @param editor, the editor the unclear from
     * @param last, a collection that stores last clearAll action
     */
    public static void unclearLastClearAll(@Nonnull Editor editor, @NotNull ClearRecord last) {
        for (HashMap.Entry<Integer, String> entry : last.entrySet()) {
            SelectionUtil.addLine(editor, entry.getKey(), entry.getValue());
        }
    }

    /**
     * Get all Demark Bookmarks
     *
     * @param editor, the current editor to get Demark bookmarks from
     * @return All Demark bookmarks in the form of a sorted map containing line numbers to their respective text body
     */
    private static TreeMap<Integer, String> getDemarks(@Nonnull Editor editor) {
        Project project = editor.getProject();
        Document document = editor.getDocument();
        TreeMap<Integer, String> demarkBookmarks = new TreeMap<>();

        if (project == null) {
            return demarkBookmarks;
        }

        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();

        for (Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();

            if (isDemarked(editor, lineNum)) {
                TextRange textRange = DocumentUtil.getLineTextRange(editor.getDocument(), lineNum);
                String lineBody = document.getText(textRange);

                demarkBookmarks.put(lineNum, lineBody);
            }

        }
        return demarkBookmarks;
    }

    /**
     * Determine if a line is marked or not by the Demark plugin
     *
     * @param editor, the editor that contains the checked line
     * @param lineNum, the line num to check
     * @return true if line is marked by Demark plugin, false otherwise.
     */
    public static boolean isDemarked(@Nonnull Editor editor, int lineNum) {
        Project project = editor.getProject();
        Document document = editor.getDocument();

        if (project == null) {
            return false;
        }

        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        Bookmark bookmark = bookmarkManager.findEditorBookmark(document, lineNum);

        return bookmark != null && bookmark.getDescription().equals(DEMARK_INDICATOR);
    }

    /**
     * Get the name of the given document
     *
     * @param document, the document to get the name of
     * @return the document's file name, empty string if cannot get name
     */
    @NotNull
    public static String getDocumentName(@Nonnull Document document) {
        VirtualFile vf = FileDocumentManager.getInstance().getFile(document);
        return vf == null ? "" : vf.getName();
    }

    private static ClearRecord getClearRecord(@NotNull Editor editor, @NotNull List<Bookmark> bookmarkList) {
        Document document = editor.getDocument();
        ClearRecord cr = new ClearRecord();

        for(Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();

            if (isDemarked(editor, lineNum)) {
                cr.addRecord(lineNum, document.getText(DocumentUtil.getLineTextRange(document, lineNum)));
            }
        }
        return cr;
    }
}