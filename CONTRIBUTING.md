 ## Build Instructions
 ### Prerequisites
 - Java 8
 - Gradle 2.1+
 - Linux
 - Git
 - Command line using Bash
 
 Note: University of Washington CSE Students or faculty may use a CSE VM or a Lab Computer to build
 
 ### Building
 To build DeMark, make sure all the prerequisites are installed. Afterwards, run the following commands on the command  line.
 ```
 git clone https://github.com/DeFacto-UW/DeMark.git
 cd DeMark/DeMark
 gradle build
 ```
## Testing
- Working directory is `DeMark/DeMark`.
### Running tests 
- Run `gradle test`

### Writing tests
- See "[Adding tests and test data](https://github.com/DeFacto-UW/DeMark/blob/master/DeMark/src/tests/adding-tests-and-data.md)".

### Generating test reports
- Run `gradle check jacocoTestReports`
- Reports can be found under `${BUILD_DIR}/reports/jacoco/report`

## Running the IDE
- Working directory is `DeMark/DeMark`.
- Run `gradle runIde`

## Distributions
- Working directory is `DeMark/DeMark`.
- Run `gradle patchPluginXml buildPlugin`

## Any modifications need to make a pull request with summary of changes. 
