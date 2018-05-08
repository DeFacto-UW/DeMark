package components;
import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
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

    public void projectOpened() {
        System.out.println(String.format("Project '%s' has been opened, base dir '%s'", project.getName(), project.getBaseDir().getCanonicalPath()));
        List<Bookmark> bookmarkList = BookmarkManager.getInstance(this.project).getValidBookmarks();
        System.out.println(bookmarkList);
    }

    public void projectClosed() {
        List<Bookmark> bookmarkList = BookmarkManager.getInstance(this.project).getValidBookmarks();
//        System.out.println(bookmarkList);
        for (Bookmark bookmark : bookmarkList) {
//            System.out.println("demark bookmark at line: " + bookmark.getLine());
        }
    }


    public void disposeComponent() {
        //called after projectClosed()
    }

    @NotNull
    public String getComponentName() {
        return "DemarkProjectComponent";
    }

    public void setMessage(String message) {
       this.message = message;
    }
}