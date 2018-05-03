package main.java.utils;

import com.intellij.ide.highlighter.HighlighterFactory;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.DocumentUtil;

import java.util.ArrayList;

public class SelectionUtil {

    private final String COMMENT_MARKER = "//";

    private Editor editor;
    private Document document;
    private Project project;
    public SelectionUtil(Editor editor, Project project, Document document) {
       this.editor = editor;
       this.project = project;
       this.document = document;
    }

    /**
     * Find the start positions of each line of the editors currently selected text
     *
     * @return ArrayList of line start positions
     */
    public ArrayList<Integer> getSelectionStarts() {
        // Character position of selected model
        int selectPosStart = editor.getSelectionModel().getSelectionStart();
        int selectPosEnd = editor.getSelectionModel().getSelectionEnd();


        ArrayList<Integer> lineStarts = new ArrayList<>();

        // User didn't select anything and is in cursor mode
        if (selectPosStart == selectPosEnd) {
            lineStarts.add(selectPosStart);
            return lineStarts;
        }

        int currPos = selectPosStart;

        // Add all line starts positions to the array
        while (currPos < selectPosEnd) {
            lineStarts.add(currPos);
            int currLine = document.getLineNumber(currPos);
            TextRange textRange = DocumentUtil.getLineTextRange(document, currLine);
            currPos = textRange.getEndOffset() + 1;
        }
        return lineStarts;
    }

    /**
     *  Determine if a line is already commented
     * @param lineNum, d
     * @return
     */
    public boolean isCommented(int lineNum) {
        TextRange textRange = DocumentUtil.getLineTextRange(document, lineNum);
        String lineBody = document.getText(textRange);

        return lineBody.startsWith(COMMENT_MARKER);
    }

    /**
     *  Comment out a Demark line
     * @param lineNum, the line number to comment out
     */
    public void addComment(int lineNum) {
        if (!isCommented(lineNum)) {
            int startPos = document.getLineStartOffset(lineNum);
            Runnable addComment = () -> document.insertString(startPos, COMMENT_MARKER);
            WriteCommandAction.runWriteCommandAction(project, addComment);
        }
    }

    /**
     *  Remove Comment from a Demark line
     * @param lineNum, the line number to remove the comment from
     */
    public void removeComment(int lineNum) {
        if (isCommented(lineNum)) {
            int startPos = document.getLineStartOffset(lineNum);
            int endPos = startPos + COMMENT_MARKER.length();
            Runnable addComment = () -> document.deleteString(startPos, endPos);
            WriteCommandAction.runWriteCommandAction(project, addComment);
        }
    }

    /**
     * Remove a line from a document
     *
     * @param lineNum, the line number to remove from the document
     */
    public void removeLine(int lineNum) {
        // Convert lines to character positions
        TextRange textRange = DocumentUtil.getLineTextRange(document, lineNum);

        // Remove the line content
        if (textRange.getEndOffset() == document.getTextLength()) {
            Runnable removeText = () -> document.deleteString(textRange.getStartOffset(), textRange.getEndOffset());
            WriteCommandAction.runWriteCommandAction(project, removeText);
        } else {
            Runnable removeText = () -> document.deleteString(textRange.getStartOffset(), textRange.getEndOffset() + 1);
            WriteCommandAction.runWriteCommandAction(project, removeText);

        }
    }
}
