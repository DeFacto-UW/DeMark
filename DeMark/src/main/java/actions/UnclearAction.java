package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import exceptions.NotYetImplementedException;

public class UnclearAction extends AnAction {
    /**
     * Not yet implemented.
     * @param anActionEvent
     */
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        try {
            throw new NotYetImplementedException();
        } catch (NotYetImplementedException e) {
            e.printStackTrace();
        }
    }

    public void update(AnActionEvent e) {
        //perform action if and only if EDITOR != null
        boolean enabled = e.getData(CommonDataKeys.EDITOR) != null;
        e.getPresentation().setEnabled(enabled);
    }
}
