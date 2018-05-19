package actions.simpleTests;

import tests.java.TestingUtility;
import actions.ClearAllAction;
import actions.MarkAction;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.junit.Before;

public class ClearActionSimpleTests extends LightCodeInsightFixtureTestCase {
    @Before
    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureByFile("SimpleJava.java");
        myFixture.testAction(new MarkAction());
    }

    @Override
    public String getTestDataPath() {
        return "src/tests/java/__testData__";
    }

    public void testClearSuccessful() {
        int beforeClear = TestingUtility.getFileNumberOfLines(myFixture);
        myFixture.testAction(new ClearAllAction());
        int afterClear = TestingUtility.getFileNumberOfLines(myFixture);

        assertTrue("Clear was unsuccessful", beforeClear > afterClear);
    }
}
