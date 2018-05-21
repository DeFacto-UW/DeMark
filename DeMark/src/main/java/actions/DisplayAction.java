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

    // Initializes all fields
    private void init(@NotNull AnActionEvent anActionEvent) {
        editor = anActionEvent.getData(LangDataKeys.EDITOR);
    }

    public void update(AnActionEvent e) {
        if (editor != null) {
            boolean enabled = e.getData(CommonDataKeys.EDITOR) != null;
            e.getPresentation().setEnabled(enabled);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        init(anActionEvent);
        if (editor != null) {
            DemarkUtil.displayDemarkedLines(editor);
        }
    }
}
