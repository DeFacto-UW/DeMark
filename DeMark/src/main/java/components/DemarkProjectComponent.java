package components;
import actions.model.AllClearHistory;
import actions.model.ClearHistory;
import actions.model.ClearRecord;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import main.java.utils.HighlightUtil;
import org.jetbrains.annotations.NotNull;

/**
 */
public class DemarkProjectComponent implements ProjectComponent {

    private final Project project;
    @NotNull
    private final ApplicationComponent applicationComponent;
    private AllClearHistory history;

    /**
     * @param project The current project, i.e. the project which was just opened.
     */
    public DemarkProjectComponent(Project project, @NotNull components.DemarkApplicationComponent applicationComponent) {
        this.project = project;
        this.applicationComponent = applicationComponent;
        this.history = new AllClearHistory();
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
     * @param file, the name of the current file
     * @param cr, the collection of cleared lines
     */
    public void pushHistory(String file, ClearRecord cr) {
        this.history.addSingleHistory(file, cr);
    }

    /**
     * Undo the last history clear
     * @param file, the name of the current file
     * @return The last cleared lines
     */
    public ClearRecord popHistory(String file) {
        ClearHistory singleHistory = history.getSingleHistory(file);
        if (singleHistory != null) {
            return singleHistory.getHistory();
        }
        return null;
    }
}