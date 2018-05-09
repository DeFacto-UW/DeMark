package actions;

import actions.model.ClearRecord;
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

public class UnclearAction extends AnAction {

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
    @Override
    public void actionPerformed(AnActionEvent e) {
        init(e);
        DemarkProjectComponent demarkProjectComponent = project.getComponent(DemarkProjectComponent.class);

        ClearRecord prevClearedLines = demarkProjectComponent.popHistory(DemarkUtil.getDocumentName(document));
        if (prevClearedLines != null) {
            DemarkUtil.unclearLastClearAll(editor, prevClearedLines);

            Set<Integer> prevClearedLineNums = prevClearedLines.keySet();
            for(Integer lineNum : prevClearedLineNums) {
                DemarkUtil.addDemarkBookmark(editor, lineNum);
            }
        }
    }
}
