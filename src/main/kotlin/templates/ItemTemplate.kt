package templates

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import composables.TemplateValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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
	
	@Composable
	override fun Content() {
		Row {
			TemplateValue("key", key)
			Spacer(Modifier.width(20.dp))
			TemplateValue("id", id)
			TemplateValue("tag", tag)
		}
	}
}
