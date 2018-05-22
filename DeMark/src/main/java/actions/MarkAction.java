package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import main.java.utils.DemarkUtil;
import main.java.utils.SelectionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * AnAction class that represents the "Mark" function of the plugin
 * The action is unavailable when no editors are opened.
 *
 * Uses: {@link SelectionUtil}, {@link DemarkUtil}
 */
public class MarkAction extends AnAction {

    private Document document;      // The current opened document
    private Editor editor;          // The current opened editor

    /**
     * Attempt to initialize all the fields needed for this action.
     *
     * @param anActionEvent An action event to help initialize the
     *                      editor and document
     * @return True if fields initialized successfully, False otherwise.
     */
    private boolean init(@NotNull AnActionEvent anActionEvent) {
        editor = anActionEvent.getData(LangDataKeys.EDITOR);
        if (editor == null) {
            return false;
        }
        document = editor.getDocument();
        return document != null;
    }

    public void update(AnActionEvent e) {
        //perform action if and only if EDITOR != null
        if (editor != null) {
            boolean enabled = e.getData(CommonDataKeys.EDITOR) != null;
            e.getPresentation().setEnabled(enabled);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        boolean initSuccess = init(anActionEvent);

        if (initSuccess) {
            // Find the line start positions of selected text
            List<Integer> lineStarts = SelectionUtil.getSelectionStarts(editor);
            int countMarked = 0;

            // Count the number of lines marked
            for (int i = 0; i < lineStarts.size(); i++) {
                int lineNum = document.getLineNumber(lineStarts.get(i));
                if (DemarkUtil.isDemarkedLine(editor, lineNum)) {
                    countMarked++;
                }
            }

            // Toggle on and off bookmarks:
            //      Partially marked -  Add Demark bookmark to all lines
            //                          Only add a single layer of bookmarks
            //      All marked - Remove Demark bookmarks from all lines

            for (int i = 0; i < lineStarts.size(); i++) {
                int lineNum = document.getLineNumber(lineStarts.get(i));

                if (countMarked != lineStarts.size()) {
                    if (!DemarkUtil.isDemarkedLine(editor, lineNum)) {
                        DemarkUtil.addDemarkBookmark(editor, lineNum);
                    }

                } else {
                    DemarkUtil.removeDemarkBookmark(editor, lineNum);
                }
            }
        }
    }
}
