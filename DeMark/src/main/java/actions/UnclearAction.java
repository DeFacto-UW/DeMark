package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
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
}
