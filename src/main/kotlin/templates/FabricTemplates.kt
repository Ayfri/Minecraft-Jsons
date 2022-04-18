package templates

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import composables.TemplateValue
import composables.TemplateValueEnum
import composables.TemplateValueList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class FabricType : ITemplateType<FabricTemplate> {
	MOD_JSON;
	
	override fun toTemplate(): FabricTemplate = when (this) {
		MOD_JSON -> FabricModJSONTemplate()
	}
}

@Serializable
sealed class FabricBase : FabricTemplate()

@Serializable
enum class Environment {
	@SerialName("*") BOTH,
	@SerialName("client") CLIENT,
	@SerialName("server") SERVER;
}

@Serializable
class EntrySets : FabricTemplate() {
	@Serializable(with = SnapshotListSerializer::class)
	val main = mutableStateListOf<String>()
	
	@Serializable(with = SnapshotListSerializer::class)
	val client = mutableStateListOf<String>()
	
	@Serializable(with = SnapshotListSerializer::class)
	val server = mutableStateListOf<String>()
	
	@Composable
	override fun Content() {
		TemplateValueList("main", main)
		TemplateValueList("client", client)
		TemplateValueList("server", server)
	}
}

@Serializable
class JARDependant : FabricTemplate() {
	@Serializable(with = MutableStateSerializer::class)
	val file = mutableStateOf("")
	
	@Composable
	override fun Content() {
		TemplateValue("file", file)
	}
}

@Serializable
class LanguageAdapters : FabricTemplate(), TemplateMapSerializable {
	override val key = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	val value = mutableStateOf("")
	
	@Composable
	override fun Content() {
		TemplateValue("value", value)
	}
}

@Serializable
class FabricModJSONTemplate : FabricBase() {
	@Serializable(with = MutableStateSerializer::class)
	val schemaVersion = mutableStateOf(1)
	
	@Serializable(with = MutableStateSerializer::class)
	val id = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	val version = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	val environment = mutableStateOf(Environment.BOTH)
	
	@Serializable(with = MutableStateSerializer::class)
	val entryPoints = mutableStateOf(EntrySets())
	
	@Serializable(with = SnapshotListSerializer::class)
	val jars = mutableStateListOf<JARDependant>()
	
	@Serializable(with = SnapshotListMapSerializer::class)
	val languageAdapters = mutableStateListOf<LanguageAdapters>()
	
	@Composable
	override fun Content() {
		Row {
			TemplateValue("schemaVersion", schemaVersion, constant = true)
			TemplateValue("id", id)
			TemplateValue("version", version)
		}
		TemplateValueEnum("environment", environment)
		entryPoints.value.Content()
		TemplateValueList(jars)
		TemplateValueList(languageAdapters, unique = true)
	}
}
