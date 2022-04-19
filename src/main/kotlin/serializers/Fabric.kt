package serializers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import templates.Author
import templates.EntryPoint
import templates.LanguageAdapters
import templates.Mixin


object AuthorsSerializer : JsonTransformingSerializer<SnapshotStateList<Author>>(SnapshotListSerializer(Author.serializer())) {
	override fun transformSerialize(element: JsonElement) = buildJsonArray {
		element.jsonArray.forEach {
			val mixin = when {
				it.jsonObject["contact"]?.jsonPrimitive?.content.isNullOrBlank() -> JsonPrimitive(it.jsonObject["name"]!!.jsonPrimitive.content)
				else -> it.jsonObject
			}
			add(mixin)
		}
	}
}

object LanguageAdaptersSerializer : KSerializer<SnapshotStateList<LanguageAdapters>> {
	override val descriptor: SerialDescriptor = ListSerializer(LanguageAdapters.serializer()).descriptor
	
	override fun deserialize(decoder: Decoder): SnapshotStateList<LanguageAdapters> {
		val list = mutableStateListOf<LanguageAdapters>()
		val map = decoder.decodeSerializableValue(MapSerializer(String.serializer(), String.serializer()))
		map.forEach {
			list.add(LanguageAdapters().apply {
				key.value = it.key
				value.value = it.value
			})
		}
		
		return list
	}
	
	override fun serialize(encoder: Encoder, value: SnapshotStateList<LanguageAdapters>) {
		encoder.encodeSerializableValue(MapSerializer(String.serializer(), String.serializer()), value.associate { it.key.value to it.value.value })
	}
}

object EntryPointsSerializer : JsonTransformingSerializer<SnapshotStateList<EntryPoint>>(SnapshotListSerializer(EntryPoint.serializer())) {
	override fun transformSerialize(element: JsonElement) = buildJsonArray {
		element.jsonArray.forEach {
			val mixin = when {
				it.jsonObject["adapter"]?.jsonPrimitive?.content.isNullOrBlank() -> JsonPrimitive(it.jsonObject["value"]!!.jsonPrimitive.content)
				else -> it.jsonObject
			}
			add(mixin)
		}
	}
}

object MixinsSerializer : JsonTransformingSerializer<SnapshotStateList<Mixin>>(SnapshotListSerializer(Mixin.serializer())) {
	override fun transformSerialize(element: JsonElement) = buildJsonArray {
		element.jsonArray.forEach {
			val mixin = when {
				it.jsonObject["environment"]?.jsonPrimitive?.content.isNullOrBlank() -> JsonPrimitive(it.jsonObject["config"]!!.jsonPrimitive.content)
				else -> it.jsonObject
			}
			add(mixin)
		}
	}
}
