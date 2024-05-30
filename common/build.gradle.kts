plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
    signing
}

version = "0.0.${System.getenv("GITHUB_RUN_NUMBER") ?: "0"}"

dependencies {
    compileOnly(libs.kotlinx.serialization)

    implementation(libs.kotlinx.datetime)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.serialization)
    testImplementation(libs.mockk.agent)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    outputs.upToDateWhen { false }
}

publishing {
    publications.register<MavenPublication>("release") {
        groupId = "de.sipgate"
        artifactId = "federmappe-common"
        version = project.version.toString()

        pom {
            name.set("Federmappe")
            description.set("")
            url.set("https://github.com/sipgate/federmappe")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            scm {
                connection.set("scm:git:git://git@github.com:sipgate/federmappe.git")
                developerConnection.set("scm:git:ssh://git@github.com:sipgate/federmappe.git")
                url.set("https://github.com/sipgate/federmappe")
            }
        }

        afterEvaluate {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/sipgate/federmappe")
            credentials {
                username = "sipgate"
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

kotlin {
    jvmToolchain(17)
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}
