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

    private static final String COMMENT_MARKER = "//";

    /**
     * Find the start positions of each line of the editors currently selected text
     *
     * @return ArrayList of line start positions
     */
    public static ArrayList<Integer> getSelectionStarts(Editor editor, Document document) {
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
    public static boolean isCommented(int lineNum, Document document) {
        TextRange textRange = DocumentUtil.getLineTextRange(document, lineNum);
        String lineBody = document.getText(textRange);

        return lineBody.startsWith(COMMENT_MARKER);
    }

    /**
     *  Comment out a Demark line
     * @param lineNum, the line number to comment out
     */
    public static void addComment(int lineNum, Document document, Project project) {
        if (!isCommented(lineNum, document)) {
            int startPos = document.getLineStartOffset(lineNum);
            Runnable addComment = () -> document.insertString(startPos, COMMENT_MARKER);
            WriteCommandAction.runWriteCommandAction(project, addComment);
        }
    }

    /**
     *  Remove Comment from a Demark line
     * @param lineNum, the line number to remove the comment from
     */
    public static void removeComment(int lineNum, Document document, Project project) {
        if (isCommented(lineNum, document)) {
            int startPos = document.getLineStartOffset(lineNum);
            int endPos = startPos + COMMENT_MARKER.length();
            Runnable removeComment = () -> document.deleteString(startPos, endPos);
            WriteCommandAction.runWriteCommandAction(project, removeComment);
        }
    }

    /**
     * Add a line to a document
     *
     * @param lineNum, the line number to add to the document
     * @param text, the text to add to the document
     */
    public static void addLine(int lineNum, String text, Document document, Project project) {
        int startPos = document.getLineStartOffset(lineNum);
        Runnable addLine = () -> document.insertString(startPos, text + "\n");
        WriteCommandAction.runWriteCommandAction(project, addLine);
    }

    /**
     * Remove a line from a document
     *
     * @param lineNum, the line number to remove from the document
     */
    public static String removeLine(int lineNum, Document document, Project project) {
        // Convert lines to character positions
        TextRange textRange = DocumentUtil.getLineTextRange(document, lineNum);
        String text = document.getText(textRange);

        // Remove the line content
        if (textRange.getEndOffset() == document.getTextLength()) {
            Runnable removeText = () -> document.deleteString(textRange.getStartOffset(), textRange.getEndOffset());
            WriteCommandAction.runWriteCommandAction(project, removeText);
        } else {
            Runnable removeText = () -> document.deleteString(textRange.getStartOffset(), textRange.getEndOffset() + 1);
            WriteCommandAction.runWriteCommandAction(project, removeText);
        }
        return text;
    }
}
