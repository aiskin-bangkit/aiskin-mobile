package com.capstone.aiskin.core.data.network.disease.response

import com.google.gson.*
import java.lang.reflect.Type

class FlexibleListDeserializer : JsonDeserializer<List<String>> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): List<String> {
        return when {
            json.isJsonArray -> json.asJsonArray.mapNotNull { it.asString }
            json.isJsonPrimitive -> listOf(json.asString)
            else -> emptyList()
        }
    }
}
