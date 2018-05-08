package main.java.utils;

import com.intellij.ide.highlighter.HighlighterFactory;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.DocumentUtil;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;

public class SelectionUtil {

    private static final String COMMENT_MARKER = "//";

    /**
     * Given a range of selected text, returns the line start positions of each line in the range as a list of integers.
     *
     * @param editor The nonnull editor in which the text lives.
     * @param document The nonnull document in which the text lives.
     *
     * @return An ArrayList of integers, where each integer represents the start positions of every line in the range.
     */
    public static ArrayList<Integer> getSelectionStarts(@Nonnull Editor editor, Document document) {
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
     * Checks if the line at a line number is commented.
     * @param lineNum The line number of the line.
     * @param document The nonnull document in which the lines lives.
     *
     * @return True if the line is commented, false otherwise.
     */
    public static boolean isCommented(@Nonnegative int lineNum, @Nonnull Document document) {
        // Gets the line range from a document using the line number.
        TextRange textRange = DocumentUtil.getLineTextRange(document, lineNum);

        // Gets the string using the text range and then check if the line starts a comment.
        String lineBody = document.getText(textRange);
        return lineBody.startsWith(COMMENT_MARKER);
    }

    /**
     * Adds a comment to the line at a passed line number.
     *
     * @param lineNum The line number to add a comment to.
     * @param document The nonnull document in which the line lives.
     * @param project The nonnull project in which the line lives.
     */
    public static void addComment(@Nonnegative int lineNum, @Nonnull Document document, @Nonnull Project project) {
        /*
         If the line is not commented already, go ahead and add a comment to the line at the start position of the line.
         */
        if (!isCommented(lineNum, document)) {
            int startPos = document.getLineStartOffset(lineNum);
            Runnable addComment = () -> document.insertString(startPos, COMMENT_MARKER);
            WriteCommandAction.runWriteCommandAction(project, addComment);
        }
    }

    /**
     * Remove a comment from a line given a line number.
     *
     * @param lineNum The line number to remove the comment marker from.
     * @param document The nonnull document in which the line lives.
     * @param project The nonnull project in which the line lives.
     */
    public static void removeComment(int lineNum, Document document, Project project) {
        // Only remove if the line is commented.
        if (isCommented(lineNum, document)) {
            // Get the start of the line and then add the length of a comment marker.
            int startPos = document.getLineStartOffset(lineNum);
            int endPos = startPos + COMMENT_MARKER.length();
            // Delete the string given the range.
            Runnable removeComment = () -> document.deleteString(startPos, endPos);
            WriteCommandAction.runWriteCommandAction(project, removeComment);
        }
    }

    /**
     * Adds a line of text at a line number.
     *
     * @param lineNum The line number to add the text at.
     * @param text The nonnull line of text to add to the document.
     * @param document The nonnull document in which to add the line of text.
     * @param project The nonnull project in which to add the line of text.
     */
    public static void addLine(@Nonnegative int lineNum, @Nonnull String text, @Nonnull Document document,
                               @Nonnull Project project) {
        // Get the start position using the line number and add the line of text.
        int startPos = document.getLineStartOffset(lineNum);
        Runnable addLine = () -> document.insertString(startPos, text + "\n");
        WriteCommandAction.runWriteCommandAction(project, addLine);
    }

    /**
     * Removes a line of text from a document using the line number.
     * @param lineNum The line number to delete.
     * @param document The nonnull document in which the line lives.
     * @param project The nonnull project in which the line lives.
     *
     * @return the String that is deleted from the document.
     */
    public static String removeLine(@Nonnegative int lineNum, @Nonnull Document document, @Nonnull Project project) {
        // Convert lines to character positions
        TextRange textRange = DocumentUtil.getLineTextRange(document, lineNum);
        String text = document.getText(textRange);

        // Remove the line content, make sure to check whether the offset will return over the length of the document.
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
