package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public abstract class DeMarkAction extends AnAction {
    private Editor editor;
    private Project project;
    private Document document;
    private main.java.utils.DemarkUtil demarkUtil;

    private static HashMap<String, HashMap<Integer, String>> unclearHistory = new HashMap<>();

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
        unclearHistory.put(demarkUtil.getDocumentName(document), demarkUtil.clearAllDemarkBookmarks());
    }

    public void unclear(AnActionEvent anActionEvent){
        init(anActionEvent);
        if (unclearHistory.size() != 0) {
            demarkUtil.unclearLastClearAll(unclearHistory.get(demarkUtil.getDocumentName(document)));
        }
    }
}
