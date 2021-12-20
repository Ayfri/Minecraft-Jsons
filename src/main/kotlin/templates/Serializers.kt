@file:OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)

package templates

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

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

@Serializer(forClass = SnapshotStateMap::class)
class SnapshotMapSerializer<K, V>(private val keySerializer: KSerializer<K>, private val valueSerializer: KSerializer<V>) : KSerializer<SnapshotStateMap<K, V>> {
	override val descriptor: SerialDescriptor = MapSerializer(keySerializer, valueSerializer).descriptor
	
	override fun serialize(encoder: Encoder, value: SnapshotStateMap<K, V>) {
		encoder.encodeSerializableValue(MapSerializer(keySerializer, valueSerializer), value as Map<K, V>)
	}
	
	override fun deserialize(decoder: Decoder): SnapshotStateMap<K, V> {
		val map = mutableStateMapOf<K, V>()
		val items = decoder.decodeSerializableValue(MapSerializer(keySerializer, valueSerializer))
		map.putAll(items)
		return map
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
