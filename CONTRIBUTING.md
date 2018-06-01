## Build Instructions
### Prerequisites
- Java 8
- Gradle 2.1+
- Git
- Command line using Bash
- A machine with a graphical interface. 
 
Note: University of Washington CSE Students or faculty may use a CSE VM or a Lab Computer to build.
 
### Building
Make sure all prerequisites are met, then run
```
git clone https://github.com/DeFacto-UW/DeMark.git
cd DeMark/DeMark
gradle build
```

All instructions and documentation below requires the working directory to be `DeMark/DeMark`.

## Repository Structure
- `src.main.java`: 
  - `actions`: classes that extend `AnAction` class that represents the functions of the plugin.
  These are mainly controllers that uses the `utils` classes. 
  - `components`: classes that extend different types of `Component` classes, these are used to mainly
  store data and perform startup activities. Handles adding data on project and file close, and restoring data on project
  and file open. 
    - `components.model`: storage classes
  - `exceptions`: classes that extends `Exception` class. For custom exceptions. 
  - `utils`: classes that work directly with the IntelliJ SDK Platform. Run `gradle javadoc` to generate JavaDocs files.
    - The documentation can be found in `/build/docs/javadoc/`. To view open `index.html`.
- `src.main.resources`:
  - `icons`: stores icons and a class that gets the icons using `IconLoader`.
  - `META-INF`: has the `plugin.xml` file.

## Testing

### Structure
- `src.tests.java`:
  - `actions`: tests for classes in `src.main.java.actions`. 
    - `simpleTests`: simple usage case tests. 
    - `complexTests`: more complicated usage case tests.
  - `components`: tests for classes in `src.main.java.components`.
  - `utils`: tests for classes in `src.main.java.utils`.
  - `__testData__`: holds sample "classes" for tests. See [writing tests](#writing-tests).

### Running tests 
- Run `gradle test`

### Writing tests
- See "[Adding tests and test data](https://github.com/DeFacto-UW/DeMark/blob/master/DeMark/src/tests/adding-tests-and-data.md)".

### Generating test reports
- Run `gradle check jacocoTestReports`
- Reports can be found under `${BUILD_DIR}/reports/jacoco/report`

## Running the IDE
Requires a graphical interface (won't work on terminal-only systems).
- Run `gradle runIde`

## Distributions
- Run `gradle patchPluginXml buildPlugin`

## Any modifications need to make a pull request with summary of changes. 
