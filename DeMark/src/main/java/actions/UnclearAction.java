package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import exceptions.NotYetImplementedException;

public class UnclearAction extends DeMarkAction {

    public void update(AnActionEvent e) {
        //perform action if and only if EDITOR != null
        boolean enabled = e.getData(CommonDataKeys.EDITOR) != null;
        e.getPresentation().setEnabled(enabled);
    }

    /**
     * Not yet implemented.
     * @param e
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        this.unclear(e);
    }
}
