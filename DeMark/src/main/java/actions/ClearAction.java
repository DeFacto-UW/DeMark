package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import main.java.utils.DemarkUtil;
import main.java.utils.SelectionUtil;
import org.jetbrains.annotations.NotNull;

public class ClearAction extends AnAction {


    private Editor editor;
    private Project project;
    private Document document;
    private DemarkUtil demarkUtil;
    private SelectionUtil selectionUtil;

    // TODO: Check the ones that may be null
    // Initializes all fields
    private void init(@NotNull AnActionEvent anActionEvent) {
        project = anActionEvent.getProject();
        document = anActionEvent.getData(LangDataKeys.EDITOR).getDocument();
        editor = anActionEvent.getData(LangDataKeys.EDITOR);
        demarkUtil = new DemarkUtil(editor, project, document);
        selectionUtil = new SelectionUtil(editor, project, document);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        init(anActionEvent);

        // Get the current line that the cursor is at
        int currLine = document.getLineNumber(editor.getSelectionModel().getSelectionStart());

        // If it is a Demark line, remove it
        if (demarkUtil.isDemarked(currLine)) {
            demarkUtil.clearDemarkBookmark(currLine);
        }
    }
}