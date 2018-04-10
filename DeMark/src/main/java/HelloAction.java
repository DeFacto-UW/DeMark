import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class HelloAction extends AnAction {
    public HelloAction() {
        super("Hello");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        Messages.showMessageDialog(project, "Hello, World!", "Greeting", Messages.getInformationIcon());

    }
}
