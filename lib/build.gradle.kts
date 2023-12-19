import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
    signing
}

android {
    namespace = "de.sipgate.federmappe"
    compileSdk = 34
    defaultConfig.minSdk = 24

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.kotlinx.serialization)
    implementation(libs.firebase.firestore)

    testImplementation(libs.junit)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    outputs.upToDateWhen { false }
}

fun Project.setupVersionInfo(): Properties {
    val versionProperties = File(project.rootDir, "version.properties")
    return versionProperties.inputStream().use { inputStream ->
        Properties().apply {
            load(inputStream)
            project.version = getVersionName()
        }
    }
}

fun Properties.getVersionName(): String {
    val major = (get("majorVersion") as String).toInt()
    val minor = (get("minorVersion") as String).toInt()
    val patch = (get("patchVersion") as String).toInt()
    return "$major.$minor.$patch"
}

publishing {
    publications.withType<MavenPublication> {
        groupId = "de.sipgate"
        artifactId = "federmappe"
        version = setupVersionInfo().getVersionName()

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
            from(components["release"])
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

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}
