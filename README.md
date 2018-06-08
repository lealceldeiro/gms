[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)&nbsp;&nbsp;
[![Build Status](https://travis-ci.org/lealceldeiro/gms.svg?branch=master)](https://travis-ci.org/lealceldeiro/gms)&nbsp;&nbsp;
[![Coverage Status](https://coveralls.io/repos/github/lealceldeiro/gms/badge.svg?branch=master)](https://coveralls.io/github/lealceldeiro/gms?branch=master)&nbsp;&nbsp;

# Table of Contents
* [Overview](#overview)
* [Ready for production](#ready-for-production)
* [How to start developing](#how-to-start-developing)


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
  3. From the location `client/` run `npm install` in order to install all client package dependencies.
  4. From the location `server/` run `gradle bootWar -Pclient=true`.
  5. Optionally you can generate the client app (Angular) documentation by setting also the `clientDoc` param to true like this `-PclientDoc=true`.
  
  An example of build with both params enabled to true would be `gradle bootWar -Pclient=true -PclientDoc=true`
* This will create a WAR file inside `server/build/libs` ready to be deployed.
* Database configuration
  1. Create a [PostgreSQL][5] database.
  2. Open the WAR file and set the proper connection parameters inside the file `WEB-INF/classes/config/application-production.properties`
  3. Save the file and update it inside the WAR file.
  4. Deploy the WAR file.

_**Note:**_ If you don't have [Gradle][6] installed, you can use the command `./gradlew` instead of `gradle` for the previous mentioned steps.

## How to start developing
* Make a local working copy of the project (` git clone https://github.com/lealceldeiro/gms.git`).

### IntelliJ IDEA
#### API Server module
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

#### Client module
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

[1]: https://gradle.org/
[2]: https://cli.angular.io/
[3]: https://www.npmjs.com/package/angular-cli
[4]: https://nodejs.org/en/
[5]: https://www.postgresql.org/
[6]: https://gradle.org/
[7]: https://www.npmjs.com/get-npm
[8]: http://tomcat.apache.org/
