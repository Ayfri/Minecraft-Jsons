package templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import composables.TemplateValue
import composables.TemplateValueEnum
import composables.TemplateValueList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import serializers.*

enum class FabricType : ITemplateType<FabricTemplate> {
	MOD_JSON,
	BLOCKSTATE;
	
	override fun toTemplate(): FabricTemplate = when (this) {
		MOD_JSON -> FabricModJSONTemplate()
		BLOCKSTATE -> FabricBlockstateTemplate()
	}
}

@Serializable
sealed class FabricBase : FabricTemplate()

@Serializable
enum class Facing {
	NORTH,
	SOUTH,
	EAST,
	WEST;
}

@Serializable
class FabricBlockstateTemplate : FabricBase() {
	@Serializable(with = MutableStateSerializerLowerCase::class)
	val facing = mutableStateOf(Facing.NORTH)
	
	@Composable
	override fun Content() {
		TemplateValueEnum("facing", facing)
	}
}

@Serializable
enum class Environment {
	@SerialName("*") BOTH,
	@SerialName("client") CLIENT,
	@SerialName("server") SERVER;
}

@Serializable
class Author : FabricTemplate() {
	@Serializable(with = MutableStateSerializer::class)
	val name = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	val contact = mutableStateOf("")
	
	@Composable
	override fun Content() {
		TemplateValue("name", name, required = true)
		TemplateValue("contact", contact)
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
	@Transient
	override val key = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	val value = mutableStateOf("")
	
	@Composable
	override fun Content() {
		TemplateValue("value", value)
	}
}

@Serializable
class Mixin : FabricTemplate() {
	@Serializable(with = MutableStateSerializer::class)
	val config = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	val environment = mutableStateOf("")
	
	@Composable
	override fun Content() {
		TemplateValue("config", config, required = true)
		TemplateValue("environment", environment)
	}
}

@Serializable
class Contact : FabricTemplate() {
	@Serializable(with = MutableStateSerializer::class)
	val email = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	val irc = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	val homepage = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	val issues = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	val sources = mutableStateOf("")
	
	@Composable
	override fun Content() {
		TemplateValue("email", email)
		TemplateValue("irc", irc)
		TemplateValue("homepage", homepage)
		TemplateValue("issues", issues)
		TemplateValue("sources", sources)
	}
}

@Serializable
class EntryPoint : FabricTemplate() {
	@Serializable(with = MutableStateSerializer::class)
	val value = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	val adapter = mutableStateOf("")
	
	@Composable
	override fun Content() {
		TemplateValue("value", value, required = true)
		TemplateValue("adapter", adapter)
	}
}

@Serializable
class EntrySets : FabricTemplate() {
	@Serializable(with = EntryPointsSerializer::class)
	val main = mutableStateListOf(EntryPoint())
	
	@Serializable(with = EntryPointsSerializer::class)
	val client = mutableStateListOf<EntryPoint>()
	
	@Serializable(with = EntryPointsSerializer::class)
	val server = mutableStateListOf<EntryPoint>()
	
	@Composable
	override fun Content() {
		Text("Entry Sets :")
		Column(
			modifier = Modifier.padding(start = 16.dp, top = 8.dp)
		) {
			TemplateValueList("main", main)
			TemplateValueList("client", client)
			TemplateValueList("server", server)
		}
	}
}

@Serializable
class Dependency : FabricTemplate(), TemplateListAsMap {
	@Serializable(with = MutableStateSerializer::class)
	override val key = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	override val value = mutableStateOf("")
	
	@Composable
	override fun Content() {
		TemplateValue("name", key, required = true)
		TemplateValue("version", value, required = true)
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
	val name = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	val description = mutableStateOf("")
	
	@Serializable(with = AuthorsSerializer::class)
	val authors = mutableStateListOf<Author>()
	
	@Serializable(with = MutableStateSerializer::class)
	val contact = mutableStateOf(Contact())
	
	@Serializable(with = SnapshotListSerializer::class)
	val contributors = mutableStateListOf<String>()
	
	@Serializable(with = MutableStateSerializer::class)
	val licence = mutableStateOf("")
	
	@Serializable(with = StringArrayOrStringSerializer::class)
	val icon = mutableStateListOf<String>()
	
	@Serializable(with = MutableStateSerializer::class)
	val environment = mutableStateOf(Environment.BOTH)
	
	@Serializable(with = MutableStateSerializer::class)
	val entryPoints = mutableStateOf(EntrySets())
	
	@Serializable(with = SnapshotListSerializer::class)
	val jars = mutableStateListOf<JARDependant>()
	
	@Serializable(with = LanguageAdaptersSerializer::class)
	val languageAdapters = mutableStateListOf<LanguageAdapters>()
	
	@Serializable(with = MixinsSerializer::class)
	val mixins = mutableStateListOf<Mixin>()
	
	@Serializable(with = ListAsMapSerializer::class)
	val depends = mutableStateListOf<Dependency>()
	
	@Serializable(with = ListAsMapSerializer::class)
	val recommends = mutableStateListOf<Dependency>()
	
	@Serializable(with = ListAsMapSerializer::class)
	val suggests = mutableStateListOf<Dependency>()
	
	@Serializable(with = ListAsMapSerializer::class)
	val breaks = mutableStateListOf<Dependency>()
	
	@Serializable(with = ListAsMapSerializer::class)
	val conflicts = mutableStateListOf<Dependency>()
	
	@Composable
	override fun Content() {
		Row {
			TemplateValue("schemaVersion", schemaVersion, constant = true)
			TemplateValue("id", id)
			TemplateValue("version", version)
		}
		Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
		
		TemplateValue("name", name)
		TemplateValue("description", description)
		TemplateValueList("authors", authors)
		
		Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
		
		contact.value.Content()
		TemplateValueList("contributors", contributors)
		TemplateValue("licence", licence)
		TemplateValueList("icon", icon)
		
		Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
		
		TemplateValueEnum("environment", environment)
		
		Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
		
		entryPoints.value.Content()
		
		Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
		
		TemplateValueList("mixins", mixins)
		TemplateValueList("jars", jars)
		TemplateValueList("languageAdapters", languageAdapters, unique = true)
		
		Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
		
		TemplateValueList("depends", depends)
		TemplateValueList("recommends", recommends)
		TemplateValueList("suggests", suggests)
		TemplateValueList("breaks", breaks)
		TemplateValueList("conflicts", conflicts)
	}
}
