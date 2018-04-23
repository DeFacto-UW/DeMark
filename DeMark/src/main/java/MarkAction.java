package main.java;

import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.print.Book;

public class MarkAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        Project project = anActionEvent.getProject();
        if (project == null) {
            return;
        }

        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        Document document = anActionEvent.getData(LangDataKeys.EDITOR).getDocument();
        Editor editor = anActionEvent.getData(LangDataKeys.EDITOR);

        int currLineNum = document.getLineNumber(editor.getSelectionModel().getSelectionStart());

        // TODO: Add bookmark description DeMark
        bookmarkManager.addEditorBookmark(editor, currLineNum);
        Bookmark added = bookmarkManager.findEditorBookmark(document, currLineNum);
        bookmarkManager.setDescription(added, "Demark");
    }
}
