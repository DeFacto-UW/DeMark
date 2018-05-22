package actions;

import components.model.ClearRecord;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import main.java.utils.DemarkUtil;
import org.jetbrains.annotations.NotNull;
import components.DemarkProjectComponent;

import java.util.Set;
/**
 * AnAction class that represents the "Unclear" function of the plugin
 * The action is unavailable when no editors are opened.
 *
 * Uses: {@link DemarkUtil}, {@link DemarkProjectComponent}, {@link ClearRecord}
 */
public class UnclearAction extends AnAction {

    private Editor editor;          // The current opened editor
    private Project project;        // The current opened project
    private Document document;      // The current opened document

    /**
     * Attempt to initialize all the fields needed for this action.
     *
     * @param anActionEvent An action event to help initialize the editor,
     *                      project and document
     * @return True if fields initialized successfully, False otherwise.
     */
    private boolean init(@NotNull AnActionEvent anActionEvent) {
        editor = anActionEvent.getData(LangDataKeys.EDITOR);
        if (editor == null) {
            return false;
        }

        project = editor.getProject();
        document = editor.getDocument();
        return project != null && document != null;
    }
    @Override
    public void actionPerformed(AnActionEvent e) {
        boolean initSuccess = init(e);

        if (initSuccess) {
            // Get the DeMark Component and clear global history
            DemarkProjectComponent projectComponent = DemarkUtil.getProjectComponent(project);
            String fileName = DemarkUtil.getDocumentName(document);
            ClearRecord prevClearedLines = projectComponent.popHistory(fileName);

            // Restore all previously cleared lines and re-bookmark them
            if (prevClearedLines != null) {
                DemarkUtil.unclearLastClearAll(editor, prevClearedLines);

                Set<Integer> prevClearedLineNums = prevClearedLines.keySet();
                for (Integer lineNum : prevClearedLineNums) {
                    DemarkUtil.addDemarkBookmark(editor, lineNum);
                }
            }
        }
    }
}
