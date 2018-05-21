package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import main.java.utils.DemarkUtil;
import org.jetbrains.annotations.NotNull;

/**
 * AnAction class that represents the "Display" function in the plugin.
 * The action is unavailable when no editors are opened.
 *
 * Uses: {@link DemarkUtil}
 */
public class DisplayAction extends AnAction {
    private Editor editor;

    /**
     * Attempt to initialize all the fields needed for this action.
     *
     * @param anActionEvent An action event to help initialize the editor.
     * @return True if fields initialized successfully, False otherwise.
     */
    private boolean init(@NotNull AnActionEvent anActionEvent) {
        editor = anActionEvent.getData(LangDataKeys.EDITOR);
        return editor != null;
    }

    public void update(AnActionEvent e) {
        if (editor != null) {
            boolean enabled = e.getData(CommonDataKeys.EDITOR) != null;
            e.getPresentation().setEnabled(enabled);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        boolean initSuccess = init(anActionEvent);

        if (initSuccess) {
            DemarkUtil.displayDemarkedLines(editor);
        }
    }
}
