# 1. How to start developing using IntelliJ IDEA

* Make a local working copy of the project (`git clone https://github.com/lealceldeiro/gms.git`).

## 1.1. API Server module

* Open (import) the folder **server** in the working copy of the project.

![Image: Open server folder](./images/1-server-open-home.idea.png)

![Image: Select server folder](./images/2-server-select-folder.idea.png)

* Configure the [gradle][1] options.

![Image: Configure gradle options](./images/3-server-gradle-config.png)

  1. Select the distribution of gradle to be used. The default option is "_Use default gradle wrapper_", but if you like you can check the "_Use local gradle distribution_" if you have gradle installed on your PC (in which case you must set the "Gradle home" directory.
  2. Select the JVM.
  3. Select the project format (`.idea (directory based)` in this case).

* This will create an IntelliJ IDEA project with one module (_server_). Here you should choose to add the root directory to the VCS.

![Image: Server module ready](./images/4-server-module-ready.png)

## 1.2. Client module

* `Open Module Settings` (F4) and add a new module (+ green sign) and select the `Import Module` option.

![Image: Open module settings](./images/5-client-add-module.png)

![Image: Add new module](./images/6-client-import-module.png)

* Select the folder `client` in the same level of the `server` folder.

![Image: Select client folder](./images/7-client-select-folder.png)

* Select the option "_Create module from existing source_".

![Image: Select options create module from existing source](./images/8-client-create-module.png)

* _Next_.
* In the window "Import Module", keep the checkbox checked and click "Next";

![Image: Select next in the Import Module option](./images/9-client-import-sources.png)

* _Finish_ (this will search for the [Angular CLI][2] framework) installed through the [angular-cli node package][3].

![Image: Select options create module from existing source](./images/10-client-finish-import.png)

* This will create another module on the project (_client_).

![Image: Select options create module from existing source](./images/11-client-done.png)

### At this point the project should contain two modules (_server_ and _client_)

[1]: https://gradle.org/
[2]: https://cli.angular.io/
[3]: https://www.npmjs.com/package/angular-cli
