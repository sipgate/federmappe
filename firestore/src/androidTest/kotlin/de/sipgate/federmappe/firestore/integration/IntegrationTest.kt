package de.sipgate.federmappe.firestore.integration

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.tasks.await
import de.sipgate.federmappe.firestore.toObjects
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantComponentSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.junit.Test

class IntegrationTest {

    init {
        val firebaseApp = Firebase.initialize(
            ApplicationProvider.getApplicationContext<Context>(),
            options = FirebaseOptions.Builder().setApplicationId("demo-test")
                .setProjectId("demo-test").build()
        )
        Firebase.firestore(firebaseApp)
            .useEmulator("10.0.2.2", 8080)
    }

    @Serializable
    data class User(
        val id: String,
        @Serializable(with = InstantComponentSerializer::class)
        val createdAt: Instant,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun asdf(): Unit = runTest {
        val a = Firebase.firestore.collection("users").get().await().toObjects<User>()
        a
    }
}
