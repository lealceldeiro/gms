# 2. Using Eclipse

* Make a local working copy of the project (`git clone https://github.com/lealceldeiro/gms.git`).

## 2.1. API Server module

* Go to _File_ > _Import_.

![Image: Import](./images/eclipse-1-import.png)

* Select the wizard for "_Existing Gradle Project_"

![Image: Select the wizard for Existing Gradle Project](./images/eclipse-2-import-server-gradle-wizard.png)

* Select the _server_ folder location (where you ckecked out the project from github)

![Image: Select the server folder location](./images/eclipse-3-select-folder-location.png)

* Specify the [gradle][1] and Java options. The recommended way is to leave it as it is by default for gradle: use the wrapper. Nevertheless you can customize all these parameters as desired.

![Image: Specify the Gradle and Java options](./images/eclipse-4-select-gradle-and-java-options.png)

* Check that all parameters are the correct ones for the import in the "_Import Preview_" step and click _Finish_.

![Image: Import Preview](./images/eclipse-5-finish-server-import.png)

* The _server_ module should be imported at this point and shown similar to this:

![Image: Import Preview](./images/eclipse-6-server-imported.png)

## 2.2. Client module

* Go to _File_ > _Import_.

![Image: Import](./images/eclipse-1-import.png)

* Select the wizard for "_Angular Project_"

![Image: Select the wizard for Angular Project](./images/eclipse-7-import-client-angular-wizard.png)

* Select the _client_ folder location (where you checked out the project from github)

![Image: Select the server folder location](./images/eclipse-8-select-folder-location.png)

* Check that all parameters are the correct ones for the import in the "_Configure NPM and Angular CLI Commands_" step and click _Finish_ (this will search for the [Angular CLI][2] framework) installed through the [angular-cli node package][3].

![Image: Configure NPM and Angular CLI Commands](./images/eclipse-9-finish-client-import.png)

* The _client_ module should be imported at this point and shown similar to this:

![Image: Import Preview](./images/eclipse-10-client-imported.png)

### At this point in the workspace should be two projects (_server_ and _client_)

## 2.3. Additional tools

These tools are not really mandatory, but in order to get an increased productivity they are recommended.

* [Spring Tools 4 - for Spring Boot (aka Spring Tool Suite 4)][9]
* [TypeScript IDE][10]

[1]: https://gradle.org/
[2]: https://cli.angular.io/
[3]: https://www.npmjs.com/package/angular-cli
[9]: https://marketplace.eclipse.org/content/spring-tools-4-spring-boot-aka-spring-tool-suite-4
[10]: https://marketplace.eclipse.org/content/typescript-ide
