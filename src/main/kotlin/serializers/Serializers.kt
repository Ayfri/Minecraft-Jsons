package serializers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import templates.Template

interface TemplateMapSerializable {
	@Transient
	val key: MutableState<String>
}

class SnapshotListSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<SnapshotStateList<T>> {
	override val descriptor: SerialDescriptor = ListSerializer(dataSerializer).descriptor
	
	override fun serialize(encoder: Encoder, value: SnapshotStateList<T>) {
		encoder.encodeSerializableValue(ListSerializer(dataSerializer), value as List<T>)
	}
	
	override fun deserialize(decoder: Decoder): SnapshotStateList<T> {
		val list = mutableStateListOf<T>()
		val items = decoder.decodeSerializableValue(ListSerializer(dataSerializer))
		list.addAll(items)
		return list
	}
}

class SnapshotListMapSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<SnapshotStateList<T>> where T : Template, T : TemplateMapSerializable {
	override val descriptor: SerialDescriptor = ListSerializer(dataSerializer).descriptor
	
	override fun serialize(encoder: Encoder, value: SnapshotStateList<T>) {
		encoder.encodeSerializableValue(MapSerializer(String.serializer(), dataSerializer), value.associateBy { it.key.value })
	}
	
	override fun deserialize(decoder: Decoder): SnapshotStateList<T> {
		val list = mutableStateListOf<T>()
		val items = decoder.decodeSerializableValue(MapSerializer(String.serializer(), dataSerializer))
		items.forEach { (key, value) ->
			value.key.value = key
			list.add(value)
		}
		return list
	}
}

class MutableStateSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<MutableState<T>> {
	override val descriptor: SerialDescriptor = dataSerializer.descriptor
	
	override fun serialize(encoder: Encoder, value: MutableState<T>) {
		encoder.encodeSerializableValue(dataSerializer, value.value)
	}
	
	override fun deserialize(decoder: Decoder): MutableState<T> {
		val value = decoder.decodeSerializableValue(dataSerializer)
		return mutableStateOf(value)
	}
}

class MutableStateSerializerLowerCase<T>(private val dataSerializer: KSerializer<T>) : KSerializer<MutableState<T>> {
	override val descriptor: SerialDescriptor = dataSerializer.descriptor
	
	override fun serialize(encoder: Encoder, value: MutableState<T>) {
		encoder.encodeString(value.value.toString().lowercase())
	}
	
	override fun deserialize(decoder: Decoder): MutableState<T> {
		val value = decoder.decodeSerializableValue(dataSerializer)
		return mutableStateOf(value)
	}
}

object StringArrayOrStringSerializer : JsonTransformingSerializer<SnapshotStateList<String>>(SnapshotListSerializer(String.serializer())) {
	override fun transformSerialize(element: JsonElement): JsonElement {
		require(element is JsonArray)
		return element.singleOrNull()?.jsonPrimitive?.contentOrNull?.let { JsonPrimitive(it) } ?: element
	}
}

interface TemplateListAsMap {
	@Serializable(with = MutableStateSerializer::class)
	val key: MutableState<String>
	
	@Serializable(with = MutableStateSerializer::class)
	val value: MutableState<String>
}

class ListAsMapSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<SnapshotStateList<T>> where T : TemplateListAsMap, T : Template {
	override val descriptor = MapSerializer(String.serializer(), String.serializer()).descriptor
	
	/**
	 * Maybe not working properly.
	 */
	override fun deserialize(decoder: Decoder): SnapshotStateList<T> {
		val list = mutableStateListOf<T>()
		val items = decoder.decodeSerializableValue(MapSerializer(String.serializer(), String.serializer()))
		
		items.forEach { (key, value) ->
			val item = dataSerializer.deserialize(decoder)
			item.key.value = key
			item.value.value = value
			list.add(item)
		}
		
		return list
	}
	
	override fun serialize(encoder: Encoder, value: SnapshotStateList<T>) {
		encoder.encodeSerializableValue(MapSerializer(String.serializer(), String.serializer()), value.associate { it.key.value to it.value.value })
	}
}
