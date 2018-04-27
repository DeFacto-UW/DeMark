package main.java;

import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;

public class ClearAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        if (project == null) {
            return;
        }

        // Bookmark Manager deals with bookmarks for this file
        // Document is this current file
        // Editor is the IntelliJ editor
        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        Document document = anActionEvent.getData(LangDataKeys.EDITOR).getDocument();
        Editor editor = anActionEvent.getData(LangDataKeys.EDITOR);
        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();

        // Get the corresponding file to this document
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        String thisFileName = virtualFile.getName();

        for (Bookmark bookmark : bookmarkList) {
            String bookmarkFileName = bookmark.getFile().getName();

            // Remove all bookmarks with Demark in this file
            if (bookmark.getDescription().equals("DeMark") && thisFileName.equals(bookmarkFileName)) {
                int lineNum = bookmark.getLine();
                bookmarkManager.removeBookmark(bookmark);

                // Converting lines to character positions
                int startPos = document.getLineStartOffset(lineNum);
                int endPos = document.getLineEndOffset(lineNum);

                // Remove the line content
                Runnable removeText = () -> document.deleteString(startPos, endPos + 1);
                WriteCommandAction.runWriteCommandAction(project, removeText);
            }
        }
    }
}
