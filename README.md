
# GMS

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)&nbsp;&nbsp;
[![Build Status](https://travis-ci.org/lealceldeiro/gms.svg?branch=master)](https://travis-ci.org/lealceldeiro/gms)&nbsp;&nbsp;
[![Coverage Status](https://coveralls.io/repos/github/lealceldeiro/gms/badge.svg?branch=master)](https://coveralls.io/github/lealceldeiro/gms?branch=master)&nbsp;&nbsp;
[![JavaScript Style Guide](https://img.shields.io/badge/code_style-standard-brightgreen.svg)](https://standardjs.com)&nbsp;&nbsp;

# Table of Contents

* [Overview](#overview)
* [Ready for production](#ready-for-production)
* [How to start developing](#how-to-start-developing)
  1. [Using IntelliJ IDEA Ultimate](./help/INTELLIJIDEA.md) :smile:
  2. [Using Visual Studio Code](./help/VSCode.md) :+1:
  3. [Using IntelliJ IDEA Community and Visual Studio Code](./help/INTELLIJIDEA-VSCode.md) :+1:
  4. [Using Eclipse](./help/Eclipse.md) :confused:

## Overview

The project consists of two main (independent) modules:

* [Spring Boot application](./server)

* [Angular application](./client)
  
  You can collaborate on any of them independently by modifying only the module you would like to change.

## Ready for production

* Packing as a WAR file with the API server only
  1. You need Java 8 in order to generate the WAR and a servlet container (such as [Apache Tomcat][8]) in order to deploy it.
  2. From the location `server/` run `gradle bootWar`.
* API + Angular Client
  1. You need to have [Node.js][4] 10.9.0 or later, together with an [npm][7] package manager.
  2. Consider modifying the configuration file with the proper values according to your environment:
      * `client/src/assets/config/config.dev.json` (when you're in development mode)
      * `client/src/assets/config/config.prod.json` (when you're in production mode)
  3. From the location `server/` run `gradle bootWar -Pclient=true`.
  4. Optionally you can modify other behaviors for the client app while generating the WAR using the following commands:
  * `client`, use `-Pclient=true` for generating the client Angular app.
  * `clientDoc`, use `-PclientDoc=true` for generating the client app documentation.
  * `clientDependencies`, use `-PclientDependencies=true` for (re)installing all node dependencies before creating the WAR. Use this options if it is the first time you create the WAR file. Once the dependencies have been installed this can be safely skipped next time.
  * An example of build with the first two parameters enabled to true would be `gradle bootWar -Pclient=true -PclientDoc=true`.

* This will create a WAR file inside `server/build/libs` ready to be deployed.
* Database configuration
  1. Create a [PostgreSQL][5] database.
  2. Open the WAR file and set the proper connection parameters inside the file `WEB-INF/classes/config/application-production.properties`
  3. Save the file and update it inside the WAR file.
  4. Deploy the WAR file.

_**Note:**_ If you don't have [Gradle][6] installed, you can use the command `./gradlew` instead of `gradle` for the previous mentioned steps.

## How to start developing

  1. Refer to [How to start developing using IntelliJ IDEA](./help/INTELLIJIDEA.md)
  2. Refer to [How to start developing using Visual Studio Code](./help/VSCode.md)
  3. Refer to [How to start developing using IntelliJ IDEA Community and Visual Studio Code](./help/INTELLIJIDEA-VSCode.md)
  4. Refer to [How to start developing using Eclipse](./help/Eclipse.md)

[4]: https://nodejs.org/en/
[5]: https://www.postgresql.org/
[6]: https://gradle.org/
[7]: https://www.npmjs.com/
[8]: http://tomcat.apache.org/
