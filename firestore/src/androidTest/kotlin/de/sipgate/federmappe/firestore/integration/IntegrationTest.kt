package de.sipgate.federmappe.firestore.integration

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.Firebase
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.initialize
import de.sipgate.federmappe.common.toObjectWithSerializer
import de.sipgate.federmappe.firestore.normalizeStringMap
import de.sipgate.federmappe.firestore.toObjects
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantComponentSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.junit.Test

@OptIn(ExperimentalSerializationApi::class)
class IntegrationTest {

    companion object {
        private val firebaseApp by lazy {
            Firebase.initialize(
                ApplicationProvider.getApplicationContext<Context>(),
                options = FirebaseOptions.Builder()
                    .setApplicationId("demo-test")
                    .setProjectId("demo-test")
                    .build()
            )
        }

        private val firestore by lazy {
            FirebaseFirestore.getInstance(firebaseApp).apply {
                useEmulator("10.0.2.2", 8080)
            }
        }
    }

    @Test
    fun simpleUserParsing(): Unit = runTest {
        @Serializable
        data class User(
            val id: String,
            @Serializable(with = InstantComponentSerializer::class)
            val createdAt: Instant,
        )

        val a = firestore.collection("users").get().await().toObjects<User>()
        a
    }

    @Serializable
    sealed interface Entity {
        val id: String

        @Serializable
        @SerialName("USER")
        data class FullUser(
            override val id: String,
            @Serializable(with = InstantComponentSerializer::class)
            val createdAt: Instant,
            val name: String,
        ) : Entity
    }

    @Test
    fun fullUserParsing(): Unit = runTest {
        val a = firestore.collection("users").get().await()
        val b = a.map {
            try {
                val c = it.data
                val d = c.normalizeStringMap()
                val e = d.toObjectWithSerializer<Entity>()
                e
            } catch (ex: Throwable) {
                throw ex
            }
        }
        b
    }
}
