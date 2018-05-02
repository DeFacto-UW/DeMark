package main.java.utils;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

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

        int currPos = selectPosStart;
        ArrayList<Integer> lineStarts = new ArrayList<>();

        // Add all line starts positions to the array
        while (currPos <= selectPosEnd) {
            lineStarts.add(currPos);
            int lineNum = document.getLineNumber(currPos);

            // Calculate the offset from start of line to end
            int startOffset = document.getLineStartOffset(lineNum);
            int endOffset = document.getLineEndOffset(lineNum);

            // Position of next line is 1 over currPos + offSet
            int offSet = endOffset - startOffset;
            currPos += offSet + 1;
        }
        return lineStarts;
    }

    /**
     *  Comment out a Demark line
     * @param lineNum, the line number to comment out
     */
    public void addComment(int lineNum) {
        // TODO: Add checkers for already commented lines
        int startPos = document.getLineStartOffset(lineNum);
        Runnable addComment = () -> document.insertString(startPos, COMMENT_MARKER);
        WriteCommandAction.runWriteCommandAction(project, addComment);
    }

    /**
     *  Remove Comment from a Demark line
     * @param lineNum, the line number to remove the comment from
     */
    public void removeComment(int lineNum) {
        // TODO: Add checkers for already commented lines

        int startPos = document.getLineStartOffset(lineNum);
        int endPos = startPos + COMMENT_MARKER.length();
        Runnable addComment = () -> document.deleteString(startPos, endPos);
        WriteCommandAction.runWriteCommandAction(project, addComment);
    }

    /**
     * Remove a line from a document
     *
     * @param lineNum, the line number to remove from the document
     */
    public void removeLine(int lineNum) {
        // Convert lines to character positions
        int startPos = document.getLineStartOffset(lineNum);
        int endPos = document.getLineEndOffset(lineNum);

        // Remove the line content
        Runnable removeText = () -> document.deleteString(startPos, endPos + 1);
        WriteCommandAction.runWriteCommandAction(project, removeText);
    }
}
