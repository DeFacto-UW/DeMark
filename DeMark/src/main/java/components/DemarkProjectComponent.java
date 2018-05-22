package components;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerAdapter;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBus;
import components.model.AllClearHistory;
import components.model.ClearHistory;
import components.model.ClearRecord;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import main.java.utils.HighlightUtil;
import org.jetbrains.annotations.NotNull;
import components.DemarkApplicationComponent;

/**
 * A component for the project specifically used to detect when the project is
 * opened, allowing the plugin to perform certain actions during IDE startup.
 *
 * Interacts with {@link actions.ClearAllAction} and
 * {@link actions.UnclearAction} to handle cleared lines
 *
 * Uses: {@link HighlightUtil},
 *       {@link AllClearHistory},
 *       {@link ClearRecord},
 *       {@link ClearRecord}
 */
public class DemarkProjectComponent implements ProjectComponent {

    private final Project project;
    @NotNull
    private final ApplicationComponent applicationComponent;
    private AllClearHistory history;

    /**
     * @param project The project which was just opened.
     */
    public DemarkProjectComponent(Project project,
                                  @NotNull DemarkApplicationComponent appComponent) {
        this.project = project;
        this.applicationComponent = appComponent;
        this.history = new AllClearHistory();
    }

    public void initComponent() {
        System.out.println("Initializing project");
    }

    /**
     * After project as been opened
     */
    public void projectOpened() {
        Runnable addHighlights = () -> HighlightUtil.addHighlightsOnProjectOpen(project);

        StartupManager.getInstance(project)
                        .registerPostStartupActivity(addHighlights);

        // Add file listeners to ensure highlights are "persistent"
        MessageBus messageBus = project.getMessageBus();
        messageBus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER,
                                        new FileEditorManagerAdapter() {
            @Override
            public void fileOpened(@NotNull FileEditorManager source,
                                   @NotNull VirtualFile file) {
                super.fileOpened(source, file);

                Editor editor = source.getSelectedTextEditor();
                assert(editor != null);
                HighlightUtil.addHighlightsOnFileOpen(editor, file);

            }

            @Override
            public void fileClosed(@NotNull FileEditorManager source,
                                   @NotNull VirtualFile file) {
                super.fileClosed(source, file);
            }

            @Override
            public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                super.selectionChanged(event);
            }
        });
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