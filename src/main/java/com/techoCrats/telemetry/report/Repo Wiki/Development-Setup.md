# Development setup

## Prerequisites

- JDK 17.
- Maven.
- BlueJ 5.1.0, or another BlueJ release compatible with the configured
  Extensions API.

The BlueJ Extensions API is supplied by BlueJ and is not published to Maven
Central. Install the `bluej.jar` from your BlueJ installation into your local
Maven repository before building. For the default Windows installation, run
this command in PowerShell from the repository root:

```powershell
mvn install:install-file "-Dfile=C:\Program Files\BlueJ\lib\bluej.jar" "-DgroupId=bluej" "-DartifactId=bluej" "-Dversion=5.1.0" "-Dpackaging=jar"
```

For a different BlueJ version or installation location, update the jar path and
the `bluej.version` property in `pom.xml` to match.

## Build and test

Run the test suite from the repository root:

```powershell
mvn test
```

The BlueJ dependency is declared with Maven's `provided` scope because BlueJ
supplies it when loading the extension.

## Source layout

- Production code: `src/main/java/`
- Tests: `src/test/java/`
- Maven configuration: `pom.xml`

The project targets Java 17 and uses JUnit Jupiter for tests, Gson as a
dependency, and OpenPDF for PDF generation.
