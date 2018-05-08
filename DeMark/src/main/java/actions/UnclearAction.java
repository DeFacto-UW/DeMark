package actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;

public class UnclearAction extends DeMarkAction {

    public void update(AnActionEvent e) {
        //perform action if and only if EDITOR != null
        boolean enabled = e.getData(CommonDataKeys.EDITOR) != null;
        e.getPresentation().setEnabled(enabled);
    }
    @Override
    public void actionPerformed(AnActionEvent e) {
        this.unclear(e);
    }

    public void update(AnActionEvent e) {
        //perform action if and only if EDITOR != null
        boolean enabled = e.getData(CommonDataKeys.EDITOR) != null;
        e.getPresentation().setEnabled(enabled);
    }
}
