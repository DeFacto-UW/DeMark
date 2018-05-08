package main.java.utils;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.project.Project;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.DocumentUtil;

import main.java.utils.DemarkUtil;

import javax.annotation.Nonnull;
import java.util.List;

public class HighlightUtil {


    private static JBColor highlightColor = new JBColor(Gray._222, Gray._220);
    // NOTES ON HIGHLIGHTING:
    // There are multiple highlighting layers. This can be found in HighlighterLayer.java in the SDK.
    // We are using the layer directly below the selection layer, which is SELECTION - 1 or LAST - 1
    // We don't want to remove the implicit highlighters cause that might break things.
    // We don't need to use highlight manager to do things, just straight up use the editor markup model
    // Currently we are only using the line numbers and the list of highlighters that we added to determine

    /**
     * Remove a highlight from a line
     *
     * @param editor,  the editor to remove the highlight from
     * @param lineNum, the line number to remove the highlight from
     */
    public static void removeHighlight(@Nonnull Editor editor, int lineNum) {

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
     * Add a highlight to a line
     *
     * @param editor,  the editor to add the highlight to
     * @param lineNum, the line number to add the highlight to
     */
    public static void addHighlight(@Nonnull Editor editor, int lineNum) {
        TextAttributes textAttributes = new TextAttributes();
        textAttributes.setBackgroundColor(highlightColor);

        editor.getMarkupModel().addLineHighlighter(lineNum, HighlighterLayer.LAST - 1, textAttributes);
    }


    /**
     * @param project The project to add the highlights to
     */
    public static void addHighlightsOnStart(Project project) {
        List<Bookmark> bookmarkList = BookmarkManager.getInstance(project).getValidBookmarks();
        TextAttributes textAttributes = new TextAttributes();
        textAttributes.setBackgroundColor(highlightColor);

        Editor[] editorArray = EditorFactory.getInstance().getAllEditors();
        for (Editor editor : editorArray) {
            Document document = editor.getDocument();

            for (Bookmark bookmark : bookmarkList) {
                Document bookmarkDoc = bookmark.getDocument();

                if (document.equals(bookmarkDoc) && bookmark.getDescription().equals(DemarkUtil.DEMARK_INDICATOR)) {
                    editor.getMarkupModel().addLineHighlighter(bookmark.getLine(), HighlighterLayer.LAST - 1, textAttributes);
                }
            }
        }
    }
}
