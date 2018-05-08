package main.java.utils;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.DocumentUtil;

import java.util.List;
import java.util.TreeSet;

public class HighlightUtil {
    private Editor editor;
    private Project project;
    private Document document;
    private JBColor color;
    private TextAttributes textAttributes;

    public HighlightUtil(Editor editor, Project project, Document document) {
        this.editor = editor;
        this.project = project;
        this.document = document;
        this.color = new JBColor(Gray._222, Gray._220);
        this.textAttributes = new TextAttributes();
        this.textAttributes.setBackgroundColor(this.color);
    }

    // NOTES ON HIGHLIGHTING:
    // There are multiple highlighting layers. This can be found in HighlighterLayer.java in the SDK.
    // We are using the layer directly below the selection layer, which is SELECTION - 1 or LAST - 1
    // We don't want to remove the implicit highlighters cause that might break things.
    // We don't need to use highlight manager to do things, just straight up use the editor markup model
    // Currently we are only using the line numbers and the list of highlighters that we added to determine
    // TODO: figure out a good way to distinguish our Highlighter from other people's (including implicit ones)
    // TODO: Fix highlighting to persist through multiple actions to be able to remove


    /**
     * Remove a highlight from a given line
     *
     * @param lineNum, the line to remove the highlight from
     */
    public void removeHighlight(int lineNum) {

        // Find all the highlights
        RangeHighlighter[] rangeHighlighters = editor.getMarkupModel().getAllHighlighters();
        int offset = DocumentUtil.getFirstNonSpaceCharOffset(editor.getMarkupModel().getDocument(), lineNum);

        // Remove the highlight that matches the given line
        for (RangeHighlighter highlight : rangeHighlighters) {
            if (highlight.getStartOffset() == offset && highlight.getEndOffset() == offset) {
                editor.getMarkupModel().removeHighlighter(highlight);
                return;
            }
        }

    }

    /**
     * Add a highlight to a given line
     *
     * @param lineNum, the line to add a highlight to
     */
    public static void addHighlight(Editor editor, int lineNum) {
        JBColor color = new JBColor(Gray._222, Gray._220);
        TextAttributes textAttributes = new TextAttributes();
        textAttributes.setBackgroundColor(color);

        editor.getMarkupModel().addLineHighlighter(lineNum, HighlighterLayer.LAST - 1, textAttributes);
    }

    public static void addHighlights(Project project) {

        List<Bookmark> bookmarkList = BookmarkManager.getInstance(project).getValidBookmarks();
        System.out.println(bookmarkList.size());

        Editor[] editorArray = EditorFactory.getInstance().getAllEditors();
        for (Editor singleEditor : editorArray) {
            Document document = singleEditor.getDocument();
            for (Bookmark bookmark : bookmarkList) {
                Document bookmarkDoc = bookmark.getDocument();

                if (document.equals(bookmarkDoc) && bookmark.getDescription().equals(DemarkUtil.DEMARK_INDICATOR)) {

                    JBColor color = new JBColor(Gray._222, Gray._220);
                    TextAttributes textAttributes = new TextAttributes();
                    textAttributes.setBackgroundColor(color);
                    singleEditor.getMarkupModel().addLineHighlighter(bookmark.getLine(), HighlighterLayer.LAST - 1, textAttributes);
                }
            }
        }
    }
}
