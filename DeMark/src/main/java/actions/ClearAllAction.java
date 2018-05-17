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

    // TODO: Check the ones that may be null
    // Initializes all fields
    private void init(@NotNull AnActionEvent anActionEvent) {
        editor = anActionEvent.getData(LangDataKeys.EDITOR);
        project = editor.getProject();
        document = editor.getDocument();
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        init(anActionEvent);
        DemarkProjectComponent demarkProjectComponent = project.getComponent(DemarkProjectComponent.class
        );

        ClearRecord clearedLines = DemarkUtil.clearAllDemarkBookmarks(editor);
        demarkProjectComponent.pushHistory(DemarkUtil.getDocumentName(document), clearedLines);
    }
}
