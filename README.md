## Overview
The project consist of two main (independent) modules (api server and front-end resources). These are located in the **server** and **client** folder respectively. You can collaborate on any of them indepentently by modifying only the module you would like to change.

## How to start to develop?
* Make a local working copy of the project (` git clone https://github.com/lealceldeiro/gms.git`).

### IntelliJ IDEA
#### API Server module
* Open (import) the folder **server** in the working copy of the project (`File > Open`).
* Configure the [gradle][1] options
  1. Select the distribution of gradle to be used. The default option is "_Use default gradle wrapper_", but if you like you can check the "_Use local gradle distribution_" if you have gradle installed on your PC (in which case you must set the "Gradle home" directory.
  2. Select the JVM
  3. Select the project format (`.idea (directory based)` in this case).
* This will create an IntelliJ IDEA project with one module (_server_).
#### Client module
* `Open Module Settings` (F4) and add a new module (+ green sign) and select the `Import Module` option.
* Select the folder `client` in the same level of the `server` folder.
* Select the option "_Create module from existing source_".
* _Next_
* _Finish_ (this will search for the [Angular CLI][2] framework) installed through the [angular-cli node package][3]
* At this point the project should contains two modules (_server_ and _client)

[1]: https://gradle.org/
[2]: https://cli.angular.io/
[3]: https://www.npmjs.com/package/angular-cli
