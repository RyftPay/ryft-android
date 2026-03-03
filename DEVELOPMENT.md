# Development Setup

## Java Version Requirements

This project requires **Java 17** to build successfully. The GitHub Actions CI also uses Java 17.

## Build Configuration Updates (March 2026)

The project has been updated with modern versions:
- **Android Gradle Plugin**: 8.2.2 (was 7.4.2)
- **Gradle Wrapper**: 8.5 (was 7.5)
- **Kotlin**: 1.9.25 (was 1.8.0)
- **Compile & Target SDK**: 34 (was 33)
- **Java Version**: 17 (was 1.8)
- **KtLint Plugin**: 11.6.1 (was 10.2.1)

### Local Development Setup

If you have Java 17 installed via Homebrew:
```bash
export JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.18/libexec/openjdk.jdk/Contents/Home
./gradlew check
```

Or use the convenient wrapper script:
```bash
./gradlew-java17 check
./gradlew-java17 build
./gradlew-java17 ktlintFormat  # Auto-fix code style
```

### Installing Java 17

If you don't have Java 17 installed:
```bash
brew install openjdk@17
```

### Common Issues

- **Error: "25.0.1" Java version parsing**: This happens when using Java 25+ which is unsupported. Switch to Java 17.
- **Kotlin compilation failures**: Usually caused by Java version incompatibility. Ensure Java 17 is being used.

### Available Java Versions

Check what Java versions you have installed:
```bash
/usr/libexec/java_home -V
```

Switch to a specific Java version:
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```