package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Stack;

public abstract class DeMarkAction extends AnAction {
    private Editor editor;
    private Project project;
    private Document document;
    private main.java.utils.DemarkUtil demarkUtil;

    private static Stack<HashMap<Integer, String>> unclearHistory = new Stack<>();

    // TODO: Check the ones that may be null
    // Initializes all fields
    private void init(@NotNull AnActionEvent anActionEvent) {
        project = anActionEvent.getProject();
        document = anActionEvent.getData(LangDataKeys.EDITOR).getDocument();
        editor = anActionEvent.getData(LangDataKeys.EDITOR);
        demarkUtil = new main.java.utils.DemarkUtil(editor, project, document);
    }

    public void clearALl(AnActionEvent anActionEvent){
        init(anActionEvent);
        unclearHistory.push(demarkUtil.clearAllDemarkBookmarks());
    }

    public void unclear(AnActionEvent anActionEvent){
        init(anActionEvent);
        demarkUtil.unclearLastClearAll(unclearHistory.pop());
    }
}
