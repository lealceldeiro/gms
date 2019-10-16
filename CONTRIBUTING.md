# Contributing to GMS

First off, thanks for taking the time to contribute! :+1:

The following is a set of guidelines for contributing to GMS project. These are mostly guidelines, not rules. Use your common sense and best judgment, and feel free to propose changes to this document in a pull request.

#### Table Of Contents

[Code of Conduct](#code-of-conduct)

[What should I know before I get started?](#what-should-i-know-before-i-get-started)
  * [Frontend](#frontend)
  * [Backend](#backend)
  
[How Can I Contribute?](#how-can-i-contribute)
  * [Reporting Bugs](#reporting-bugs)
  * [Suggesting Enhancements](#suggesting-enhancements)
  * [Pull Requests](#pull-requests)

## Code of Conduct

This project and everyone participating in it is governed by the [GMS Code of Conduct](CODE_OF_CONDUCT.md). By participating in it, you are expected to keep this code. Please report any unacceptable behavior to [lealceldeiro@gmail.com](mailto:lealceldeiro@gmail.com).

## What should I know before I get started?
  
  ### Frontend
If you have extensively developed using Spring and preferably [Spring Boot](https://spring.io/projects/spring-boot) and [Angular](https://angular.io/) or simply want to make an improvement in any of the areas of the project that matches some of your expertises that's absolutely fine.

Here is a list of some things (not a comprehensive list) worth knowing to develop in the frontend:

 - TypeScript
 - [Angular](https://angular.io/docs)
 - Unit testing with [karma](https://karma-runner.github.io) and [jasmine](https://jasmine.github.io/)
 - e2e testing using [protractor](http://www.protractortest.org)
 - [Sass](https://sass-lang.com/)
 - HTML

  ### Backend

Here is a list of some things (not a comprehensive list) worth knowing to develop in the backend:

 - [Spring Boot](https://projects.spring.io/spring-boot)
 - [Spring Security](https://projects.spring.io/spring-security/)
 - [Spring Data JPA](https://projects.spring.io/spring-data-jpa/)
 - [Spring HATEOAS](https://projects.spring.io/spring-hateoas/)
 - [Spring Data REST](https://projects.spring.io/spring-data-rest/)
 - [Spring REST Docs](https://projects.spring.io/spring-restdocs/)
 - [Flyway](https://flywaydb.org/)
 
 ## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check [this list](#before-submitting-a-bug-report) as you might find out that you don't need to create one. When you are creating a bug report, please [include as many details as possible](#how-do-i-submit-a-good-bug-report). Fill out [the required template](https://github.com/lealceldeiro/gms/tree/master/.github/ISSUE_TEMPLATE/bug_report.md), the information it asks for helps us resolve issues faster.

> **Note:** If you find a **Closed** issue that seems like it is the same thing that you're experiencing, open a new issue and include a link to the original issue in the body of your new one.

#### Before Submitting A Bug Report

* **Perform a [search](https://github.com/lealceldeiro/gms/issues?utf8=%E2%9C%93&q=)** to see if the problem has already been reported. If it has **and the issue is still open**, add a comment to the existing issue instead of opening a new one.

#### How Do I Submit A (Good) Bug Report?

Bugs are tracked as [GitHub issues](https://guides.github.com/features/issues/). Create an issue by providing the following information by filling in [the template](https://github.com/lealceldeiro/gms/blob/master/.github/ISSUE_TEMPLATE/bug_report.md).

Explain the problem and include additional details to help maintainers reproduce the problem:

* **Use a clear and descriptive title** for the issue to identify the problem.
* **Describe the exact steps which reproduce the problem** in as many details as possible.
* **Provide specific examples to demonstrate the steps**. Include links to files or GitHub projects, or copy/pasteable snippets, which you use in those examples. If you're providing snippets in the issue, use [Markdown code blocks](https://help.github.com/articles/markdown-basics/#multiple-lines).
* **Describe the behavior you observed after following the steps** and point out what exactly is the problem with that behavior.
* **Explain which behavior you expected to see instead and why.**
* **Include screenshots and animated GIFs** which show you following the described steps and clearly demonstrate the problem. You can use [this tool](https://www.cockos.com/licecap/) to record GIFs on macOS and Windows, and [this tool](https://github.com/colinkeenan/silentcast) on Linux.
* **If the problem wasn't triggered by a specific action**, describe what you were doing before the problem happened.

### Suggesting Enhancements

Before creating enhancement suggestions, please check [this list](#before-submitting-an-enhancement-suggestion) as you might find out that you don't need to create one. When you are creating an enhancement suggestion, please [include as many details as possible](#how-do-i-submit-a-good-enhancement-suggestion). Fill in [the template](https://github.com/lealceldeiro/gms/blob/master/.github/ISSUE_TEMPLATE/feature_request.md), including the steps that you imagine you would take if the feature you're requesting existed.

#### Before Submitting An Enhancement Suggestion

* **Determine which part of the project the enhancement should be suggested in (brackend, frontend)**
* **Perform a [search](https://github.com/lealceldeiro/gms/issues?utf8=%E2%9C%93&q=)** to see if the enhancement has already been suggested. If it has, add a comment to the existing issue instead of opening a new one.

#### How Do I Submit A (Good) Enhancement Suggestion?

Enhancement suggestions are tracked as [GitHub issues](https://guides.github.com/features/issues/). After you've determined which part of the project your enhancement suggestion is related to, create an issue and provide the following information:

* **Use a clear and descriptive title** for the issue to identify the suggestion.
* **Provide a step-by-step description of the suggested enhancement** in as many details as possible.
* **Provide specific examples to demonstrate the steps**. Include copy/pasteable snippets which you use in those examples, as [Markdown code blocks](https://help.github.com/articles/markdown-basics/#multiple-lines).
* **Describe the current behavior** and **explain which behavior you expected to see instead** and why.
* **Include screenshots and animated GIFs** which help you demonstrate the steps or point out the part of Atom which the suggestion is related to. You can use [this tool](https://www.cockos.com/licecap/) to record GIFs on macOS and Windows, and [this tool](https://github.com/colinkeenan/silentcast) on Linux.
* **Explain why this enhancement would be useful**.
* **Specify which version of GMS you're using.**
* **Specify the name and version of the OS you're using if the enhancement you're requesting corresponds to the backend.**
* **Specify the name and version of the browser you're using if the enhancement you're requesting corresponds to the frontend.**

### Pull Requests

The process described here has several goals:

- Maintain GMS code quality
- Fix problems that are important to users

Please follow these steps to have your contribution considered by the maintainers:

1. Follow all instructions in [the template](https://github.com/lealceldeiro/gms/blob/master/.github/PULL_REQUEST_TEMPLATE/pull_request_template.md)
2. Follow the [styleguides](#styleguides)
3. After you submit your pull request, verify that all [status checks](https://help.github.com/articles/about-status-checks/) are passing <details><summary>What if the status checks are failing?</summary>If a status check is failing, and you believe that the failure is unrelated to your change, please leave a comment on the pull request explaining why you believe the failure is unrelated. A maintainer will re-run the status check for you. If we conclude that the failure was a false positive, then we will open an issue to track that problem with our status check suite.</details>

While the prerequisites above must be satisfied prior to having your pull request reviewed, the reviewer(s) may ask you to complete additional design work, tests, or other changes before your pull request can be ultimately accepted.

## Styleguides

### Git Commit Messages

* Use the present tense ("Add feature" not "Added feature")
* Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
* Limit the first line to 72 characters or less
* Reference issues and pull requests liberally after the first line
* Start the comment with `Server` if the changes are in the server side or `Client` if they are in the client side. Do not use any of these if the changes are in any other file(s).

### JavaScript Styleguide

All JavaScript must adhere to [JavaScript Standard Style](https://standardjs.com/).

* Prefer the object spread operator (`{...anotherObj}`) to `Object.assign()`
* Inline `export`s with expressions whenever possible
  ```js
  // Use this:
  export default class ClassName {

  }

  // Instead of:
  class ClassName {

  }
  export default ClassName
  ```
* In general for the frontend code use the [Angular Style Guide](https://angular.io/guide/styleguide)
* For the backend code use the [Java Code Conventions](https://www.oracle.com/technetwork/java/codeconventions-150003.pdf), a good resource can be found in [Java Style Guide GitHub repo](https://github.com/twitter/commons/blob/master/src/java/com/twitter/common/styleguide.md)
* Place requires in the following order:
    * Angular Modules
    * Third party modules/libraries
    * Local Modules
    * For java classes the import should alphabetically ordered
* Place class properties in the following order:
    * Class methods and properties (methods starting with `static`)
    * Instance methods and properties
* Avoid platform-dependent code

### Specs Styleguide

* Frontend
  * Include thoughtfully-worded, well-structured [Jasmine](https://jasmine.github.io/) specs next to each component, service, etc.
  * Treat `describe` as a noun or situation.
  * Treat `it` as a statement about state or how an operation changes state.
* Backend
  * Create the spec for every class that need it under `src/test/java/com/gms/<path-to-class-as-it-is-in-src-main-java-com-gms>`
  * Always aim to create unitary tests and all the e2e test considered necessary
  * For endpoints, always provide e2e test where the documentation is generated using [Asciidoctor](https://asciidoctor.org/). Also, create the needed resources in the folder `src/main/asciidoc` with the correspondent description regarding to the created/updated change and link them to the resources created by the tests.
  
  ### Documentation Styleguide

* Always documents every field, property and method in both sides: client and server.
* Use [Markdown](https://daringfireball.net/projects/markdown) for frontend and the property jsdoc style for backend.
  
