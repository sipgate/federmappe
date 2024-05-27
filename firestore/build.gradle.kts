import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
    signing
}

version = versionString

android {
    namespace = "de.sipgate.federmappe.firestore"
    compileSdk = 34
    defaultConfig.minSdk = 23

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
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(project(":common"))

    compileOnly(libs.firebase.firestore)
    compileOnly(libs.kotlinx.serialization)

    implementation(libs.kotlinx.datetime)

    testImplementation(libs.firebase.firestore)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.serialization)

    androidTestImplementation(libs.firebase.firestore)
    androidTestImplementation(libs.kotlin.test)
    androidTestImplementation(libs.kotlinx.serialization)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    outputs.upToDateWhen { false }
}

val Project.versionString: String
    get() {
    val versionProperties = File(project.rootDir, "version.properties")
    return versionProperties.inputStream().use { inputStream ->
        Properties().run {
            load(inputStream)
            "${parseInt("majorVersion")}.${parseInt("minorVersion")}.${parseInt("patchVersion")}"
        }
    }
}

fun Properties.parseInt(key: String) = (this[key] as String).toInt()

publishing {
    publications.register<MavenPublication>("release") {
        groupId = "de.sipgate"
        artifactId = "federmappe-firestore"
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
