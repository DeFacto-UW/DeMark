package main.java.components;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import main.java.utils.DemarkUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DemarkStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        System.out.println("Startup activity");
        BookmarkManager.getInstance(project).loadState(BookmarkManager.getInstance(project).getState());
    }
}
