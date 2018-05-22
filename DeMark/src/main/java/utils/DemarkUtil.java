package main.java.utils;

import com.intellij.ui.components.JBScrollPane;
import components.model.ClearRecord;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.DocumentUtil;
import components.DemarkProjectComponent;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Utility class that works as the primary controller for the plugin.
 *
 * Handles marking, unmarking, toggling, display, clear, and unclear
 *
 * A marked line will have a {@link Bookmark} and
 * a {@link com.intellij.openapi.editor.markup.RangeHighlighter}
 * associated with it.
 *
 * Uses: {@link SelectionUtil}, {@link HighlightUtil}, {@link ClearRecord}
 */
public class DemarkUtil {
    // the indicator that is used for setting the bookmark description
    public static String DEMARK_INDICATOR = "DeMark";


    /**
     * Creates a bookmark at the current line number and adds
     * a highlight to the line.
     *
     * @param editor the editor to add the mark to
     * @param lineNum the line number to add the mark to
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

        // Add a bookmark by adding description and highlighting
        if (added != null) {
            bookmarkManager.setDescription(added, DEMARK_INDICATOR);
            HighlightUtil.addHighlight(editor, lineNum);
        }
    }

    /**
     * Removes the bookmark and the highlight associated with the line number
     *
     * @param editor, the editor to remove the mark from
     * @param lineNum, the line number to remove the mark from
     */
    public static void removeDemarkBookmark(@Nonnull Editor editor,
                                            int lineNum) {
        Project project = editor.getProject();
        Document document = editor.getDocument();

        if (project == null) {
            return;
        }

        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        Bookmark result = bookmarkManager.findEditorBookmark(document, lineNum);

        // Check if the bookmark is a DeMark bookmark by checking description
        if (result != null && isDemarkedLine(editor, result.getLine())) {
            bookmarkManager.removeBookmark(result);
            HighlightUtil.removeHighlight(editor, lineNum);
        }
    }

    /**
     * Comment or uncomments the text on a line that is marked by DeMark
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

        // Toggle all demark bookmarks
        for (Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();
            if (isDemarkedLine(editor, lineNum)) {
                if (SelectionUtil.isCommented(editor, lineNum)) {
                    SelectionUtil.removeComment(editor, lineNum);
                } else {
                    SelectionUtil.addComment(editor, lineNum);
                }
            }
        }
    }

    /**
     * Display the marked lines for the current file that is opened
     * in a separate window by IntelliJ
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

        JBScrollPane scrollPane = new JBScrollPane(area);

        frame.add(scrollPane);
        frame.setSize(500,500);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    /**
     * Removes all DeMark bookmarks and deletes the corresponding lines
     * with it from the current file
     *
     * @param editor, the editor that contains the marked lines
     *
     * @return {@link ClearRecord} of all cleared marked lines.
     */
    public static ClearRecord clearAllDemarkBookmarks(@Nonnull Editor editor) {
        Project project = editor.getProject();
        ClearRecord record = new ClearRecord();
        if (project == null) {
            return record;
        }

        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();

        record = getClearRecord(editor, bookmarkList);

        for (Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();

            // Remove all bookmarks with Demark in this file
            if (isDemarkedLine(editor, lineNum)) {
                bookmarkManager.removeBookmark(bookmark);
                HighlightUtil.removeHighlight(editor, lineNum);
                SelectionUtil.removeLine(editor, lineNum);
            }
        }
        return record;
    }

    /**
     * Add all lines from last clear
     *
     * @param editor, the editor the unclear from
     * @param last, a collection that stores last clearAll action
     */
    public static void unclearLastClearAll(@Nonnull Editor editor,
                                           @NotNull ClearRecord last) {

        // iterates through every entry in the ClearRecord
        // Each entry is a (Key, Value) pair with
        //      Key = line number
        //      Value = text at line number
        for (HashMap.Entry<Integer, String> entry : last.entrySet()) {
            Document document = editor.getDocument();
            int line = entry.getKey();

            TextRange textRange = DocumentUtil.getLineTextRange(document, line);
            String lineBody = document.getText(textRange);

            if (!lineBody.equals(entry.getValue())) {
                SelectionUtil.addLine(editor, entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Get all DeMark Bookmarks
     *
     * @param editor, the current editor to get Demark bookmarks from
     * @return All bookmarks labeled with the {@link #DEMARK_INDICATOR} in
     *          the form of a Treemap containing line numbers
     *          to their respective text body
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

        // Find all Demark bookmarks
        for (Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();

            if (isDemarkedLine(editor, lineNum)) {
                // Map the lineNumber -> lineBody
                TextRange textRange = DocumentUtil.getLineTextRange(document,
                                                                    lineNum);
                String lineBody = document.getText(textRange);
                demarkBookmarks.put(lineNum, lineBody);
            }

        }
        return demarkBookmarks;
    }

    /**
     * Determine if a line is marked or not by the Demark plugin
     * using the description of the bookmakr
     *
     * @param editor, the editor that contains the checked line
     * @param lineNum, the line num to check
     * @return true if the bookmark on the line has description
     *         containing {@link #DEMARK_INDICATOR}
     */
    public static boolean isDemarkedLine(@Nonnull Editor editor, int lineNum) {
        Project project = editor.getProject();
        Document document = editor.getDocument();

        if (project == null) {
            return false;
        }

        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        Bookmark bookmark = bookmarkManager.findEditorBookmark(document,
                                                                lineNum);

        // Check if the bookmark has the correct description
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

    /*
     * Adds all the bookmarks inside the bookmark list into
     * a ClearRecord as the pair (Line number, Text)
     */

    /**
     * Construct a new clear record from a list of bookmarks.
     *
     * @param editor The editor to put the clear record in
     * @param bookmarkList The bookmark list to add into the clear record
     * @return A new Clear Record containing bookmarks (line number -> text)
     */
    private static ClearRecord getClearRecord(@NotNull Editor editor,
                                              @NotNull List<Bookmark> bookmarkList) {
        Document document = editor.getDocument();
        ClearRecord cr = new ClearRecord();

        for(Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();

            if (isDemarkedLine(editor, lineNum)) {
                TextRange tr = DocumentUtil.getLineTextRange(document, lineNum);
                String textBody = document.getText(tr);

                cr.addRecord(lineNum, textBody);
            }
        }
        return cr;
    }

    public static DemarkProjectComponent getProjectComponent(Project project) {
        return project.getComponent(DemarkProjectComponent.class);
    }
}