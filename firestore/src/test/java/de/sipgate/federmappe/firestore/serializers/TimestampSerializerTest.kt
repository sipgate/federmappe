package de.sipgate.federmappe.firestore.serializers

import com.google.firebase.Timestamp
import de.sipgate.federmappe.firestore.FirebaseTimestampDecoder
import de.sipgate.federmappe.common.StringMapToObjectDecoder
import de.sipgate.federmappe.common.serializers.TimestampSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.Date


class TimestampSerializerTest {
    private val epochSeconds = 1707833611L * 1000
    private val nanos = 801 / 1000000

    /* Nanos will be ignored, because java.util.Date isn't precise enough. */
    private val date = Date(epochSeconds + nanos)

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun testDeserialization() {
        @Serializable
        data class TestClass(val a: @Serializable(with = TimestampSerializer::class) Date)

        val serializer = serializer<TestClass>()
        val data = mapOf<String, Any?>("a" to Timestamp(1707833611, 801))

        val result = serializer.deserialize(
            StringMapToObjectDecoder(data,
                subtypeDecoder = { (it as? Timestamp)?.let(::FirebaseTimestampDecoder) })
        )

        // Assert
        Assertions.assertEquals(date, result.a)
        Assertions.assertInstanceOf(TestClass::class.java, result)
    }
}
