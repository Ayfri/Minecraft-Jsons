@file:OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)

package templates

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

interface TemplateMapSerializable {
	@Transient
	val key: MutableState<String>
}

@Serializer(forClass = SnapshotStateList::class)
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

@Serializer(forClass = MutableState::class)
class MutableStateSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<MutableState<T>> {
	override fun deserialize(decoder: Decoder) = mutableStateOf(decoder.decodeSerializableValue(dataSerializer))
	override val descriptor: SerialDescriptor = dataSerializer.descriptor
	override fun serialize(encoder: Encoder, value: MutableState<T>) = encoder.encodeSerializableValue(dataSerializer, value.value)
}

class MutableStateSerializerLowerCase<T>(private val dataSerializer: KSerializer<T>) : KSerializer<MutableState<T>> {
	override fun deserialize(decoder: Decoder) = mutableStateOf(decoder.decodeSerializableValue(dataSerializer))
	override val descriptor: SerialDescriptor = dataSerializer.descriptor
	override fun serialize(encoder: Encoder, value: MutableState<T>) = encoder.encodeString(value.value.toString().lowercase())
}
