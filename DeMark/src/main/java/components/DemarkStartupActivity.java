package main.java.components;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import main.java.utils.DemarkUtil;
import main.java.utils.HighlightUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DemarkStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {

    }
}
