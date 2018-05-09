package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import main.java.utils.DemarkUtil;
import org.jetbrains.annotations.NotNull;

public class ToggleAction extends AnAction {
    private Editor editor;
    private Project project;
    private Document document;

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
        DemarkUtil.toggleDemarkComment(editor);

    }
}
