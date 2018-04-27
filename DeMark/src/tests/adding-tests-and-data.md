# Adding tests to the plugin
by Andrew Tran

For simplicity's sake, the testing framework works by creating an instance of IntelliJ
in memory. This instance that is created by the instance has all the functionalities
that a normal IntelliJ instance would have. This allows testing of each individual
feature of the plugin.

## What makes a test
Each test in the test/java folder is a JUnit test that extends the `LightCodeInsightFixtureTestCase`
class from the IntelliJ platform SDK. This class does all the setup for the testing
framework automatically.

Each test file should include this block:
```
@Before
public void setUp() throws Exception {
    super.setUp();
}
```
To have the framework set up the in memory IntelliJ instance.

Additionally, each test file should override the `getTestDataPath()` method as well:
```
@Override
public String getTestDataPath() {
    return "src/tests/java/testData";
}
```

After that, each test should have a separate method for a specific behavior to test.

The `testData` folder keeps all the Java files that are inteded to be loaded up by the in
memory IntelliJ.

## Useful methods
The `LightCodeInsightFixtureTestCase` class provides several methods that are useful,
each method can be invoked by the field `myFixture`. This field is essentially the
in memory IntelliJ:
- `myFixture.configureByFile("FileName.extension")` sets up a single file that will be loaded
into the in memory IntelliJ's src folder. This file we also be opened in the editor.
- `myFixture.configureByFiles("File1.ext", "File2.ext", ...)` like the previous, but with
multiple files. The first file in the list will be the one opened in the editor.
- `myFixture.type(char)` stimulates typing a character in the editor.
- `myFixture.testAction(AnAction)` checks to see if the action is available, then invokes
it if it is. This will be the primary way to "activate" the plugin actions.
- `myFixture.getEditor()` returns an Editor object that has all the information of the
currently opened editor. This is the primary way to get line numbers and information.

Other information on lines and bookmarks can be accesed through other parts of the IntelliJ
platform SDK.

## Formatting test data
If there is need to add extra test data, the files that are opened in the in memory editor
will have special markup that helps the editor:
- `<caret>` specifies the position where the caret should be placed.
- `<selection>` and `</selection>` specify the start and end of the text range to be selected.
- `<block>` and `</block>` specify the start and end points of the column selection.

The test data, therefore, will need to need to have these special markup in them. Refer to
`SimpleJava.java` in the `testData` folder for an example of what a test data file might look
like.