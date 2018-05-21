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

/**
 * AnAction class that represents the "Clear" function in the plugin.
 * The action is unavailable when no editors are opened.
 *
 * Uses: {@link DemarkUtil}, {@link DemarkProjectComponent}
 */
public class ClearAllAction extends AnAction {
    private Editor editor;          // the IntelliJ editor
    private Project project;        // the IntelliJ project
    private Document document;      // the IntelliJ document

    /**
     * Attempt to initialize all the fields needed for this action.
     *
     * @param anActionEvent An action event to help initialize the editor, project and document
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
    public void actionPerformed(AnActionEvent anActionEvent) {
        boolean initSuccess = init(anActionEvent);

        if (initSuccess) {
            DemarkProjectComponent demarkProjectComponent = project.getComponent(DemarkProjectComponent.class);

            ClearRecord clearedLines = DemarkUtil.clearAllDemarkBookmarks(editor);
            demarkProjectComponent.pushHistory(DemarkUtil.getDocumentName(document), clearedLines);
        }
    }
}
