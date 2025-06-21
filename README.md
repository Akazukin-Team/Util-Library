# Utility Library

[![Build Status](https://github.com/Akazukin-Team/Util-Library/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/Akazukin-Team/Util-Library/actions/workflows/build.yml?query=branch:main)

A library providing utilities, interfaces for commonize and extended standard features.

---

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [Contributing](#contributing)
- [Build Instructions](#build-instructions)
- [Continuous Integration](#continuous-integration)
- [License](#license)
- [Contact](#contact)

---

## Features

- **Custom Enhancements**: Extends standard Java libraries to add useful utilities.
- **Interfaces**: Provide interfaces for commonize
- Etc...

---

## Getting Started

### Prerequisites

Make sure you have the following installed:

- **Java Development Kit (JDK)** version 8 or later.

---

### Installation

#### Using Maven

1. Add the following repository to the `<repositories>` block in your `pom.xml` file:
    ```xml
    <repository>
        <id>akazukin-repo</id>
        <name>Akazukin Repository</name>
        <url>https://maven.akazukin.org/refer/maven-public/</url>
    </repository>
    ```

2. Add the dependency to the `<dependencies>` block in your `pom.xml` file:
    ```xml
    <dependency>
        <groupId>org.akazukin</groupId>
        <artifactId>util</artifactId>
        <version>VERSION</version>
    </dependency>
    ```

---

#### Using Gradle

1. Add the repository to the `repositories` block in your `build.gradle` file:
    ```groovy
    maven {
        name = 'Akazukin Repository'
        url = 'https://maven.akazukin.org/refer/maven-public/'
    }
    ```

2. Add the dependency to the `dependencies` block in your `build.gradle` file:
    ```groovy
    implementation 'org.akazukin:util:<VERSION>'
    ```

---

## Contributing

Please read the [Contribution Guide](./.github/CONTRIBUTING.md) carefully and follow the coding conventions and
guidelines when making your changes.

---

## Build Instructions

To build the project from source, follow these steps:

1. Clone the repository:
    ```shell
    git clone https://github.com/Akazukin-Team/Util-Library.git
    cd Util-Library
    ```

2. Build the project with Gradle:
    ```shell
    ./gradlew build
    ```
   The compiled JAR file will be located in the `build/libs/` directory.


3. Publish to the local Maven repository using the `maven-publish` plugin:
    ```shell
    ./gradlew publishToMavenLocal
    ```

---

## Continuous Integration

This project uses GitHub Actions for Continuous Integration (CI).
Every push to the `main` branch automatically triggers the build and test workflow.

---

## License

This project is licensed under the terms described in the [License](LICENSE) file.

---

## Contact

If you need further assistance or wish to contact us directly,
please refer to the [Support](./.github/SUPPORT.md) page.

---
