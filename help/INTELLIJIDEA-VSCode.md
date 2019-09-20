# 3. How to start developing using IntelliJ IDEA Community Edition and VSCode

* Make a local working copy of the project (`git clone https://github.com/lealceldeiro/gms.git`).

## 3.1. API Server module

* Open (import) the folder **server** in the working copy of the project.

![Image: Open server folder](./images/idea.c-1-server-open-home.png)

![Image: Select server folder](./images/idea.c-2-server-select-folder.png)

* The IDE will start downloading the [Gradle][1] wrapper.

![Image: Configure gradle options](./images/idea.c-3-server-gradle-config.png)

* This will create an IntelliJ IDEA project with one module (_server_).

![Image: Server module ready](./images/idea.c-4-server-module-ready.png)

## 3.2. Client module

* Go to _File_ > _Open Folder_.

![Image: Open folder](./images/vs-code-1-open-folder.png)

* Select the folder containing the _client_ resources.

![Image: Select the folder containing the client resources](./images/vs-code-idea-2-select-folder.png)

* At this point the whole project should be imported and shown similar to this:

![Image: Project Structure](./images/vs-code-idea-3-imported-folder.png)

[1]: https://gradle.org/
[2]: https://cli.angular.io/
[3]: https://www.npmjs.com/package/angular-cli
