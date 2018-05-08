package components;

import com.intellij.openapi.components.ApplicationComponent;

public class DemarkApplicationComponent implements ApplicationComponent {

    @Override
    public void initComponent() {
        System.out.println("Starting Demark application");
    }
}
