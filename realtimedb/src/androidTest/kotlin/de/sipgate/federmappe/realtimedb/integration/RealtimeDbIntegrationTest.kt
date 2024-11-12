package de.sipgate.federmappe.realtimedb.integration

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.Firebase
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.initialize
import de.sipgate.federmappe.common.serializers.UnixSecondInstantSerializer
import de.sipgate.federmappe.realtimedb.toObjects
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import java.util.UUID
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalSerializationApi::class)
class RealtimeDbIntegrationTest {

    companion object {
        private val tempNamespace = UUID.randomUUID().toString()

        private val firebaseApp by lazy {
            Firebase.initialize(
                ApplicationProvider.getApplicationContext<Context>(),
                options = FirebaseOptions.Builder()
                    .setApplicationId("demo-test")
                    .setProjectId("demo-test")
                    .build()
            )
        }

        private val database by lazy {
            FirebaseDatabase.getInstance(firebaseApp).apply {
                useEmulator("10.0.2.2", 9000)
            }
        }

        @BeforeClass
        @JvmStatic
        fun initializeTestData() {
            val users = database.getReference("$tempNamespace-users")
            val root = users.push()
            root.setValue(
                mapOf<String, Any?>(
                    "id" to root.key,
                    "name" to "root",
                    "createdAt" to 1720794610L,
                )
            )
        }

        @AfterClass
        @JvmStatic
        fun tearDownTestData(): Unit = runTest {
            database.getReference("$tempNamespace-users").removeValue().await()
        }
    }

    @Test
    fun simpleUserParsing(): Unit = runTest {
        @Serializable
        data class User(
            val id: String,
            @Serializable(with = UnixSecondInstantSerializer::class)
            val createdAt: Instant,
        )

        val a = database.getReference("$tempNamespace-users")
            .toObjects<User>(ignoreUnknownProperties = true) { throwable ->
                fail(null, throwable)
            }
        assertTrue(a.isNotEmpty())
    }
}
