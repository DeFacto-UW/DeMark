
import com.intellij.debugger.actions.ArrayFilterAction;
import com.intellij.ide.bookmarks.Bookmark;
import com.intellij.ide.bookmarks.BookmarkManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.util.List;

public class HelloAction extends AnAction {
    public HelloAction() {
        super("Hello");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();


        if (project == null) {
            return;
        }

        // Get list of all books
        BookmarkManager bookmarkManager = BookmarkManager.getInstance(project);
        List<Bookmark> bookmarkList = bookmarkManager.getValidBookmarks();
        // Get a document based off an action
        Document document = anActionEvent.getData(LangDataKeys.EDITOR).getDocument();

        // Get an editor based off an action
        Editor editor = anActionEvent.getData(LangDataKeys.EDITOR);

        // Get line number of cursor
        int cursorPos = document.getLineNumber(editor.getSelectionModel().getSelectionStart());


        // Add a book mark.
        // Note: may have multiple bookmarks on a line
        // Note: it is persistent!!!
//        bookmarkManager.addEditorBookmark(editor, cursorPos);
//        bookmarkManager.removeBookmark(bookmarkList.get(bookmarkList.size()));
//        Messages.showMessageDialog(project, "Adding bookmark at " + cursorPos, "Greeting", Messages.getInformationIcon());

        // Adding text at cursor
        Runnable r = () -> EditorModificationUtil.insertStringAtCaret(editor, "ANOOOYSAEYOOOOOO");
        WriteCommandAction.runWriteCommandAction(project, r);

        // Removing text given the start and end
        // document.deleteString(startCharPos, endCharPos [exclusive])
        Runnable removeText = () -> document.deleteString(editor.getSelectionModel().getSelectionStart(), editor.getSelectionModel().getSelectionEnd());
        WriteCommandAction.runWriteCommandAction(project, removeText);

        // Get the position of the charactor at the cursor.
        // editor.getSelectionModel().getSelection[start/end]()
        Messages.showMessageDialog(project, "Start character is at:  " + editor.getSelectionModel().getSelectionStart(), "Greeting", Messages.getInformationIcon());


        // Converting lines to character positions where 0 is the line number 0
        int startPos = document.getLineStartOffset(0);
        int endPos = document.getLineEndOffset(0);
        Messages.showMessageDialog(project, "Start character is at:  " + startPos + " and end character is at: " + endPos, "Greeting", Messages.getInformationIcon());
    }
}
