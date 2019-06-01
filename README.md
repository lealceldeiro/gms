[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)&nbsp;&nbsp;
[![Build Status](https://travis-ci.org/lealceldeiro/gms.svg?branch=master)](https://travis-ci.org/lealceldeiro/gms)&nbsp;&nbsp;
[![Coverage Status](https://coveralls.io/repos/github/lealceldeiro/gms/badge.svg?branch=master)](https://coveralls.io/github/lealceldeiro/gms?branch=master)&nbsp;&nbsp;

# Table of Contents
* [Overview](#overview)
* [Ready for production](#ready-for-production)
* [How to start developing](#how-to-start-developing)
    1. [Using IntelliJ IDEA](#1-using-intellij-idea)

        1.1. [API Server module](#11-api-server-module)

        1.2. [Client module](#12-client-module)
    2. [Using Eclipse](#2-using-eclipse)

        2.1. [Additional tools](#21-additional-tools)

        2.2. [API Server module](#22-api-server-module)

        2.3. [Client module](#23-client-module)


## Overview
The project consists of two main (independent) modules:

 - [Spring Boot application](./server)
   
 - [Angular application](./client)
  
  You can collaborate on any of them independently by modifying only the module you would like to change.

## Ready for production
* Packing as a WAR file with the API server only
  1. You need Java 8 in order to generate the WAR and a servlet container (such as [Apache Tomcat][8]) in order to deploy it.
  2. From the location `server/` run `gradle bootWar`.
* API + Angular Client
  1. You need to have [Node.js][4] 8.9 or higher, together with [NPM][7] 5.5.1 or higher installed.
  2. Consider modifying the file `client/src/environments/environment.prod.ts`, by setting the value `apiBaseUrl` to the value of the API base url. This takes as default `http://127.0.0.1/gms/api/`.
  3. From the location `server/` run `gradle bootWar -Pclient=true`.
  4. Optionally you can modify other behaviors for the client app while generating the WAR using the following commands:
    * `client`, use `-Pclient=true` for generating the client Angular app.
    * `clientDoc`, use `-PclientDoc=true` for generating the client app documentation.
    * `clientDependencies`, use `-PclientDependencies=true` for (re)installing all node dependencies before creating the WAR. Use this options if it is the first time you create the WAR file. Once the dependencies have been installed this can be safely skipped next time.
    * `clientDeployUrl`, use `-PclientDeployUrl=<deploy-url>` for specifying the base url were the WAR will be deployed. This is where the static resources will be allocated by the Angular app. For example, supposing the WAR is deployed under a domain `www.example.com` under the sub-domain `sub1` (full path would be `www.example.com/sub1`), you should set as `<deploy-url>` the value `/sub1/`.
    * `clientBaseUrl`, use `-PclientBaseUrl=<base-url>` for specifying the base url for resolving all the resources (js, css, images, etc). Generally it can be set to the same value as the `<deploy-url>` and its default values is `./`.
    
      * An example of build with the first two parameters enabled to true would be `gradle bootWar -Pclient=true -PclientDoc=true`.
      
* This will create a WAR file inside `server/build/libs` ready to be deployed.
* Database configuration
  1. Create a [PostgreSQL][5] database.
  2. Open the WAR file and set the proper connection parameters inside the file `WEB-INF/classes/config/application-production.properties`
  3. Save the file and update it inside the WAR file.
  4. Deploy the WAR file.

_**Note:**_ If you don't have [Gradle][6] installed, you can use the command `./gradlew` instead of `gradle` for the previous mentioned steps.

## How to start developing
* Make a local working copy of the project (` git clone https://github.com/lealceldeiro/gms.git`).

### 1. Using IntelliJ IDEA
#### 1.1. API Server module
* Open (import) the folder **server** in the working copy of the project.

![Image: Open server folder](./help/images/1-server-open-home.idea.png)

![Image: Select server folder](./help/images/2-server-select-folder.idea.png)

* Configure the [gradle][1] options.

![Image: Configure gradle options](./help/images/3-server-gradle-config.png)

  1. Select the distribution of gradle to be used. The default option is "_Use default gradle wrapper_", but if you like you can check the "_Use local gradle distribution_" if you have gradle installed on your PC (in which case you must set the "Gradle home" directory.
  2. Select the JVM.
  3. Select the project format (`.idea (directory based)` in this case).
* This will create an IntelliJ IDEA project with one module (_server_). Here you should choose to add the root directory to the VCS.

![Image: Server module ready](./help/images/4-server-module-ready.png)

#### 1.2. Client module
* `Open Module Settings` (F4) and add a new module (+ green sign) and select the `Import Module` option.

![Image: Open module settings](./help/images/5-client-add-module.png)

![Image: Add new module](./help/images/6-client-import-module.png)

* Select the folder `client` in the same level of the `server` folder.

![Image: Select client folder](./help/images/7-client-select-folder.png)

* Select the option "_Create module from existing source_".

![Image: Select options create module from existing source](./help/images/8-client-create-module.png)

* _Next_.
* In the window "Import Module", keep the checkbox checked and click "Next";

![Image: Select next in the Import Module option](./help/images/9-client-import-sources.png)

* _Finish_ (this will search for the [Angular CLI][2] framework) installed through the [angular-cli node package][3].

![Image: Select options create module from existing source](./help/images/10-client-finish-import.png)

* This will create another module on the project (_client_).

![Image: Select options create module from existing source](./help/images/11-client-done.png)

#### At this point the project should contain two modules (_server_ and _client_)

### 2. Using Eclipse
#### 2.1. Additional tools

These tools are not really mandatory, but in order to get an increased productivity they are recommended.

* [Spring Tools 4 - for Spring Boot (aka Spring Tool Suite 4)][9]
* [TypeScript IDE][10]

#### 2.2. API Server module

* Go to _File_ > _Import_.

![Image: Import](./help/images/eclipse-1-import.png)

* Select the wizard for "_Existing Gradle Project_"

![Image: Select the wizard for Existing Gradle Project](./help/images/eclipse-2-import-server-gradle-wizard.png)

* Select the _server_ folder location (where you ckecked out the project from github)

![Image: Select the server folder location](./help/images/eclipse-3-select-folder-location.png)

* Specify the [gradle][1] and Java options. The recommended way is to leave it as it is by default for gradle: use the wrapper. Nevertheless you can customize all these parameters as desired.

![Image: Specify the Gradle and Java options](./help/images/eclipse-4-select-gradle-and-java-options.png)

* Check that all parameters are the correct ones for the import in the "_Import Preview_" step and click _Finish_.

![Image: Import Preview](./help/images/eclipse-5-finish-server-import.png)

* The _server_ module should be imported at this point and shown similar to this:

![Image: Import Preview](./help/images/eclipse-6-server-imported.png)

#### 2.3. Client module

* Go to _File_ > _Import_.

![Image: Import](./help/images/eclipse-1-import.png)

* Select the wizard for "_Angular Project_"

![Image: Select the wizard for Angular Project](./help/images/eclipse-7-import-client-angular-wizard.png)

* Select the _client_ folder location (where you ckecked out the project from github)

![Image: Select the server folder location](./help/images/eclipse-8-select-folder-location.png)

* Check that all parameters are the correct ones for the import in the "_Configure NPM and Angular CLI Commands_" step and click _Finish_.

![Image: Configure NPM and Angular CLI Commands](./help/images/eclipse-9-finish-client-import.png)

* The _client_ module should be imported at this point and shown similar to this:

![Image: Import Preview](./help/images/eclipse-10-client-imported.png)

#### At this point in the workspace should be two projects (_server_ and _client_)

[1]: https://gradle.org/
[2]: https://cli.angular.io/
[3]: https://www.npmjs.com/package/angular-cli
[4]: https://nodejs.org/en/
[5]: https://www.postgresql.org/
[6]: https://gradle.org/
[7]: https://www.npmjs.com/get-npm
[8]: http://tomcat.apache.org/
[9]: https://marketplace.eclipse.org/content/spring-tools-4-spring-boot-aka-spring-tool-suite-4
[10]: https://marketplace.eclipse.org/content/typescript-ide