import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  id("java")
  alias(libs.plugins.shadow)
  alias(libs.plugins.fabricJavaGenerator)
  alias(libs.plugins.sonatypeCentralPortalPublisher)
}

group = "dev.strela"
version = "0.0.2"

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

tasks.withType<Javadoc> {
  source = files(source).minus(fileTree("build/generated/sources")).asFileTree
}

centralPortal {
  username = project.findProperty("sonatypeUsername") as String
  password = project.findProperty("sonatypePassword") as String

  pom {
    name.set("Strela Java SDK")
    description.set("A Java SDK for Strela")
    url.set("https://github.com/strela-dev/java-sdk")

    developers {
      developer {
        id.set("fllipeis")
        email.set("p.eistrach@gmail.com")
      }
    }
    licenses {
      license {
        name.set("Apache-2.0")
        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
      }
    }
    scm {
      url.set("https://github.com/strela-dev/java-sdk.git")
      connection.set("git:git@github.com:strela-dev/java-sdk.git")
    }
  }
}

signing {
  useGpgCmd()
  sign(configurations.archives.get())
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.test {
  useJUnitPlatform()
}