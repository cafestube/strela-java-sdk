import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  id("java")
  alias(libs.plugins.shadow)
  alias(libs.plugins.fabricJavaGenerator)
}

group = "dev.strela"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation(libs.bundles.fabricKubernetes)
}

tasks.named("shadowJar", ShadowJar::class) {
  mergeServiceFiles()
  archiveFileName.set("${project.name}.jar")
}

javaGen {
  source = file("src/main/resources/kubernetes")
}

sourceSets {
  main {
    java {
      srcDirs("build/generated/sources/dev/strela/v1")
    }
  }
}

tasks.test {
  useJUnitPlatform()
}