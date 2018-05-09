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
    private Project project;


    // TODO: Check the ones that may be null
    // Initializes all fields
    private void init(@NotNull AnActionEvent anActionEvent) {
        editor = anActionEvent.getData(LangDataKeys.EDITOR);
        project = editor.getProject();
        document = editor.getDocument();
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
        ArrayList<Integer> lineStarts = SelectionUtil.getSelectionStarts(editor);
        int countMarked = 0;

        // Count the number of lines marked
        for (int i = 0; i < lineStarts.size(); i++) {
            int lineNum = document.getLineNumber(lineStarts.get(i));
            if (DemarkUtil.isDemarked(editor, lineNum)) {
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
                if (!DemarkUtil.isDemarked(editor, lineNum)) {
                    DemarkUtil.addDemarkBookmark(editor, lineNum);
                }

            } else {
                DemarkUtil.removeDemarkBookmark(editor, lineNum);
            }
        }


    }
}
