package templates

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import composables.TemplateValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import serializers.MutableStateSerializer
import serializers.TemplateMapSerializable

@Serializable
open class ItemTemplate : VanillaTemplate() {
	@Serializable(with = MutableStateSerializer::class)
	val id = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	val tag = mutableStateOf("")
	
	@Composable
	override fun Content() {
		Row {
			TemplateValue("id", id)
			TemplateValue("tag", tag)
		}
	}
}

@Serializable
class ItemTemplateMap : ItemTemplate(), TemplateMapSerializable {
	@Transient
	override val key = mutableStateOf("")
}
