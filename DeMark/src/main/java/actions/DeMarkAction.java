package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.Stack;

public abstract class DeMarkAction extends AnAction {
    Stack<String> history = new Stack<>();

    public void unclear(AnActionEvent e){
        history.push("1");
        System.out.println(history);
    }
}
