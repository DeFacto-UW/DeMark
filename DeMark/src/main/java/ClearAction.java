package main.java;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

import java.util.List;

public class ClearAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        if (project == null) {
            return;
        }

        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        Document document = anActionEvent.getData(LangDataKeys.EDITOR).getDocument();
        Editor editor = anActionEvent.getData(LangDataKeys.EDITOR);

        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();

        for (Bookmark bookmark : bookmarkList) {

            // TODO: Check file name
            // Remove all bookmarks with Demark in this file
            if (bookmark.getDescription().equals("Demark")) {
                int lineNum = bookmark.getLine();
                bookmarkManager.removeBookmark(bookmark);

                // Converting lines to character positions
                int startPos = document.getLineStartOffset(lineNum);
                int endPos = document.getLineEndOffset(lineNum);

                // Remove the line
                Runnable removeText = () -> document.deleteString(startPos, endPos);
                WriteCommandAction.runWriteCommandAction(project, removeText);
            }
        }
    }
}
