# Napt

<a href="https://search.maven.org/search?q=a:napt-gradle-plugin">
    <img src="https://img.shields.io/maven-central/v/com.github.pavel-vasilev/napt-gradle-plugin"/>
</a>

## Installation

```groovy
# Top-level build.gradle

buildscript {
    dependencies {
        classpath "com.github.pavel-vasilev:napt-gradle-plugin:1.0.1"
    }
    repositories {
        mavenCentral()
    }
}

subprojects {
    repositories {
        mavenCentral()
    }
}
```

#### Before

```groovy
# Module-level build.gradle

apply plugin: "kotlin-kapt"

dependencies {
    kapt "com.google.dagger:dagger:2.29"
}
```

#### After

```groovy
# Module-level build.gradle

apply plugin: "napt"

dependencies {
    annotationProcessor "com.google.dagger:dagger:2.29"
}
```