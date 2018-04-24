package main.java;

import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;


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

        // Get the current open file
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        String thisFileName = virtualFile.getName();


        // TODO: Try and highlight a line??
        // TODO: Handle batch selection bookmarking
//        int startPos = document.getLineStartOffset(currLineNum);
//        int endPos = document.getLineEndOffset(currLineNum);
//        HighlightManager.getInstance(project).addOccurrenceHighlight(editor, startPos, endPos,);

        boolean removed = false;

        int currLineNum = document.getLineNumber(editor.getSelectionModel().getSelectionStart());
        // Remove bookmark if already removed
        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();
        for (Bookmark bookmark : bookmarkList) {
            int lineNum = bookmark.getLine();
            String bookmarkFileName = bookmark.getFile().getName();

            // Line is already contained
            if (lineNum == currLineNum && bookmark.getDescription().equals("Demark") && thisFileName.equals(bookmarkFileName)) {
                bookmarkManager.removeBookmark(bookmark);
                removed = true;
            }
        }

        // Add bookmark with description Demark
        if (!removed) {
            bookmarkManager.addEditorBookmark(editor, currLineNum);
            Bookmark added = bookmarkManager.findEditorBookmark(document, currLineNum);
            bookmarkManager.setDescription(added, "Demark");
        }
    }
}
