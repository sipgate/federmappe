package de.sipgate.federmappe.firestore.integration

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.Firebase
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.initialize
import de.sipgate.federmappe.firestore.toObjects
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantComponentSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.junit.Test

@OptIn(ExperimentalSerializationApi::class)
class IntegrationTest {

    private val firebaseApp = Firebase.initialize(
        ApplicationProvider.getApplicationContext<Context>(),
        options = FirebaseOptions.Builder()
            .setApplicationId("demo-test")
            .setProjectId("demo-test")
            .build()
    )
    private val firestore = FirebaseFirestore.getInstance(firebaseApp).apply {
        useEmulator("10.0.2.2", 8080)
    }

    @Test
    fun asdf(): Unit = runTest {
        @Serializable
        data class User(
            val id: String,
            @Serializable(with = InstantComponentSerializer::class)
            val createdAt: Instant,
        )

        val a = firestore.collection("users").get().await().toObjects<User>()
        a
    }
}
