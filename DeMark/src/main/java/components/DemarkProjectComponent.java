package components;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import main.java.utils.HighlightUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 */
public class DemarkProjectComponent implements ProjectComponent {

    private final Project project;
    @NotNull
    private final ApplicationComponent applicationComponent;
    private Stack<HashMap<Integer, String>> unclearHistory;

    /**
     * @param project The current project, i.e. the project which was just opened.
     */
    public DemarkProjectComponent(Project project, @NotNull components.DemarkApplicationComponent applicationComponent) {
        this.project = project;
        this.applicationComponent = applicationComponent;
        this.unclearHistory = new Stack<>();
    }

    public void initComponent() {
        System.out.println("Initializing project");
    }

    /**
     * After project as been opend
     */
    public void projectOpened() {
        Runnable addHighlights = () -> HighlightUtil.addHighlightsOnStart(project);
        StartupManager.getInstance(project).registerPostStartupActivity(addHighlights);
    }

    /**
     * On project close
     */
    public void projectClosed() {
    }


    /**
     * After project close, clean up
     */
    public void disposeComponent() {
    }

    @NotNull
    /**
     * Get the current Components name
     */
    public String getComponentName() {
        return "DemarkProjectComponent";
    }


    /**
     * Push the history onto the stack
     * @param history The cleared lines that were removed
     */
    public void pushHistory(HashMap<Integer, String> history) {
        this.unclearHistory.push(history);
    }

    /**
     * Undo the last history clear
     * @return The last cleared lines
     */
    public HashMap<Integer, String> popHistory() {
        return this.unclearHistory.isEmpty() ? new HashMap<>() : this.unclearHistory.pop();
    }
}