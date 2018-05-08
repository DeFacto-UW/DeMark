package main.java.utils;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.DocumentUtil;
import components.facades.HighlighterProperties;
import components.PersistentHighlightsRepository;

import java.util.List;
import java.util.Set;

public class HighlightUtil {
    private Editor editor;
    private Project project;
    private Document document;
    private JBColor color;
    private TextAttributes textAttributes;

    private VirtualFile file;
    private PersistentHighlightsRepository projectHighlights;

    public HighlightUtil(Editor editor, Project project, Document document) {
        this.editor = editor;
        this.project = project;
        this.document = document;
        this.color = new JBColor(Gray._222, Gray._220);
        this.textAttributes = new TextAttributes();
        this.textAttributes.setBackgroundColor(this.color);

        file = FileDocumentManager.getInstance().getFile(editor.getDocument());
        projectHighlights = PersistentHighlightsRepository.getInstance(project);
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
        for (RangeHighlighter highlighter : rangeHighlighters) {
            if (highlighter.getStartOffset() == offset && highlighter.getEndOffset() == offset) {
                editor.getMarkupModel().removeHighlighter(highlighter);

                projectHighlights.removeDeMarkHighlightFromStorage(file.getPath(), offset, highlighter.getLayer(), Gray._222.getRed(), Gray._220.getRed());

                List<HighlighterProperties> fileHighlighters = projectHighlights.getFileHighlighters(file.getPath());
                System.out.println(fileHighlighters);
                return;
            }
        }
    }

    /**
     * Add a highlight to a given line
     *
     * @param lineNum, the line to add a highlight to
     */
    public void addHighlight(int lineNum) {
        RangeHighlighter highlighter = editor.getMarkupModel().addLineHighlighter(lineNum, HighlighterLayer.LAST - 1, this.textAttributes);
        int offset = DocumentUtil.getFirstNonSpaceCharOffset(editor.getMarkupModel().getDocument(), lineNum);
        projectHighlights.addDeMarkHighlightToStorage(file.getPath(), offset, highlighter.getLayer(), Gray._222.getRed(), Gray._220.getRed());

        List<HighlighterProperties> fileHighlighters = projectHighlights.getFileHighlighters(file.getPath());
        System.out.println(fileHighlighters.size());
        System.out.println(fileHighlighters);
    }
}
