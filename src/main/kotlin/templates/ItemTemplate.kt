package templates

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import composables.TemplateValue
import kotlinx.serialization.Serializable

@Serializable
class ItemTemplate : VanillaTemplate() {
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
