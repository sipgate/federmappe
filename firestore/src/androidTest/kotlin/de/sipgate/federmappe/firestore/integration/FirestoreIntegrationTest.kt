@file:OptIn(ExperimentalTime::class)

package de.sipgate.federmappe.firestore.integration

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.Firebase
import com.google.firebase.FirebaseOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.initialize
import de.sipgate.federmappe.common.serializers.InstantComponentSerializer
import de.sipgate.federmappe.common.toObjectWithSerializer
import de.sipgate.federmappe.firestore.integration.FirestoreIntegrationTest.Entity.FullUser
import de.sipgate.federmappe.firestore.normalizeStringMap
import de.sipgate.federmappe.firestore.toObject
import de.sipgate.federmappe.firestore.toObjects
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.junit.BeforeClass
import org.junit.Test
import java.util.UUID
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalSerializationApi::class)
class FirestoreIntegrationTest {

    companion object {
        private val tempNamespace = UUID.randomUUID().toString()

        private lateinit var rootUserId: String

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

        @BeforeClass
        @JvmStatic
        fun initializeTestData(): Unit = runTest {
            val users = firestore.collection("$tempNamespace-users")
            val root = users.add(
                mapOf<String, Any?>(
                    "name" to "root",
                    "createdAt" to Timestamp(1720794610L, 0),
                    "nested" to mapOf<String, Any?>("updatedAt" to Timestamp(1720794610L, 0)),
                    "type" to "USER",
                )
            ).await()
            root.update("id", root.id).await()
            rootUserId = root.id
        }
    }

    @Serializable
    data class SimpleUser(
        val id: String,
        @Serializable(with = InstantComponentSerializer::class)
        val createdAt: Instant,
    )

    @Test
    fun simpleUserParsingManual(): Unit = runTest {
        val user = firestore.document("$tempNamespace-users/$rootUserId")
            .get().await().data?.normalizeStringMap()?.toObjectWithSerializer<SimpleUser>()

        assertIs<SimpleUser>(user)
    }

    @Test
    fun simpleUserParsingWithConvenienceWrapper(): Unit = runTest {
        val user = firestore.document("$tempNamespace-users/$rootUserId")
            .get().await().toObject<SimpleUser>()

        assertIs<SimpleUser>(user)
    }

    @Test
    fun simpleUserListParsingWithConvenienceWrapper(): Unit = runTest {
        val simpleUser = firestore.collection("$tempNamespace-users")
            .get().await().toObjects<SimpleUser>()

        assertTrue(simpleUser.isNotEmpty())
    }

    @Test
    fun simpleUserListParsingManual(): Unit = runTest {
        val user = firestore.collection("$tempNamespace-users")
            .get().await()
            .map { it.data.normalizeStringMap().toObjectWithSerializer<SimpleUser>() }

        assertTrue(user.isNotEmpty())
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
        val user = firestore.collection("${tempNamespace}-users")
            .get().await().toObjects<Entity>()

        assertTrue(user.isNotEmpty())
        assertIs<FullUser>(user.first())
    }
}
