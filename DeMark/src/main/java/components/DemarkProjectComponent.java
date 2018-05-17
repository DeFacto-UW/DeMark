package components;
import components.model.AllClearHistory;
import components.model.ClearHistory;
import components.model.ClearRecord;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import main.java.utils.HighlightUtil;
import org.jetbrains.annotations.NotNull;

/**
 * A component for the project specifically used to detect when the project is opened, allowing the plugin
 * to perform certain actions during the IDE startup.
 *
 * Interacts with {@link actions.ClearAllAction} and {@link actions.UnclearAction} to handle cleared lines
 *
 * Uses: {@link HighlightUtil}, {@link AllClearHistory}, {@link ClearRecord}, {@link ClearRecord}
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
     * After project as been opened
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
     *
     * @param file, the name of the current file
     * @param cr, the collection of cleared lines
     */
    public void pushHistory(String file, ClearRecord cr) {
        this.history.addSingleHistory(file, cr);
    }

    /**
     * Gives the record of the most recently cleared lines
     *
     * @param file, the name of the current file
     * @return {@link ClearRecord} containing most recently cleared lines
     */
    public ClearRecord popHistory(String file) {
        ClearHistory singleHistory = history.getSingleHistory(file);
        if (singleHistory != null) {
            return singleHistory.getHistory();
        }
        return null;
    }
}