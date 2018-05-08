package components;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import main.java.utils.HighlightUtil;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 */
public class DemarkProjectComponent implements ProjectComponent {

    private final Project project;
    @NotNull
    private final ApplicationComponent applicationComponent;
    private String message;

    /**
     * @param project The current project, i.e. the project which was just opened.
     */
    public DemarkProjectComponent(Project project, @NotNull components.DemarkApplicationComponent applicationComponent) {
        this.project = project;
        this.applicationComponent = applicationComponent;
        this.message = "Hello there";
    }

    public void initComponent() {
        System.out.println("Initializing project");
    }

    /**
     * After project as been opend
     */
    public void projectOpened() {
        Runnable addHighlights = () -> HighlightUtil.addHighlights(project);

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
}