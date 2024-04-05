# Federmappe

![Build Status](https://github.com/sipgate/federmappe/actions/workflows/ci.yml/badge.svg)

Painless Firebase Object Mapper that was developed with Kotlin in mind.

We rely on the Kotlin Serialization compiler plugin to generate the De-/Serialization
code during compile time, which removes the need for expensive reflection calls.

## Setup

This repository currently resides on the GitHub Packages Registry, which only allows authenticated downloads.
For this you need to create a token with at least the [read:packages](https://docs.github.com/en/rest/packages/packages?apiVersion=2022-11-28#about-github-packages) permission from [here](https://github.com/settings/personal-access-tokens/new).

```kotlin
dependencyResolutionManagement {
    repositories {
        maven {
            setUrl("https://maven.pkg.github.com/sipgate/federmappe")
            credentials {
                username = "your github username"
                password = "a personal token with read:packages permission"
            }
        }
    }
    // …
}
```

Once the Repository is set up, you can add the dependency to your module:

### Firestore Database

```kotlin
dependencies {
    implementation("de.sipgate:federmappe.firestore:0.0.23")
}
```

### Firebase Realtime Database

```kotlin
dependencies {
    implementation("de.sipgate:federmappe.realtimedb:0.0.23")
}
```

## How to use

```kotlin
enum class Universe {
    @SerialName("starwars") StarWars,
    @SerialName("startrek") StarTrek
}

@Serializable
data class Spaceship(
    val name: String,
    
    @SerialName("max_occupants")
    val maxOccupants: Long,

    val universe: Universe? = null,

    val hasWeapons: Boolean = false 
)

val voyager = firestore.collection("spaceships")
    .document("NCC-74656")
    .get()
    .await()
    .toObject<Spaceship> { ex ->
        // do your exception handling here
    }
```

## Contributions

This lib is used in production and works for most of our cases (Primitives, Lists, Nested Types, Sealed Classes, …).
If you miss any functionality or find a bug feel free to drop us an Issue. No promises though.
