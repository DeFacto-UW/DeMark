package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import main.java.utils.DemarkUtil;
import main.java.utils.SelectionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MarkAction extends AnAction {

    private Document document;
    private Editor editor;
    private DemarkUtil demarkUtil;
    private SelectionUtil selectionUtil;
    private Project project;


    // TODO: Check the ones that may be null
    // Initializes all fields
    private void init(@NotNull AnActionEvent anActionEvent) {
        project = anActionEvent.getProject();
        document = anActionEvent.getData(LangDataKeys.EDITOR).getDocument();
        editor = anActionEvent.getData(LangDataKeys.EDITOR);
        demarkUtil = new DemarkUtil(editor, project, document);
        selectionUtil = new SelectionUtil(editor, project, document);
    }

    public void update(AnActionEvent e) {
        //perform action if and only if EDITOR != null
        boolean enabled = e.getData(CommonDataKeys.EDITOR) != null;
        e.getPresentation().setEnabled(enabled);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        init(anActionEvent);
        
        // Find the line start positions of selected text
        ArrayList<Integer> lineStarts = selectionUtil.getSelectionStarts();
        int countMarked = 0;

        // Count the number of lines marked
        for (int i = 0; i < lineStarts.size(); i++) {
            int lineNum = document.getLineNumber(lineStarts.get(i));
           if (demarkUtil.isDemarked(lineNum)) {
              countMarked++;
           }
        }

        // Toggle on and off bookmarks
        // Partially marked: Add Demark bookmark to all lines
        //                   Only add a single layer of bookmarks
        // All marked: Remove Demark bookmarks from all lines
        for (int i = 0; i < lineStarts.size(); i++) {

            int lineNum = document.getLineNumber(lineStarts.get(i));
            if (countMarked != lineStarts.size()) {
                if (!demarkUtil.isDemarked(lineNum)) {
                    demarkUtil.addDemarkBookmark(lineNum);
                }

            } else {
                demarkUtil.removeDemarkBookmark(lineNum);
            }
        }
    }
}
