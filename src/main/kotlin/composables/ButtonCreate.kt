package composables

import CLASS_TYPE_TO_REMOVE
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import templates.Template
import templates.TemplateType
import java.io.File

enum class ErrorType(val message: String) {
	REQUIRED_FIELD("Required field."),
	DUPLICATE_ENTRY("Keys must be unique."),
}

data class ErrorCreate<T>(val error: ErrorType?, val name: String, val value: T)

val canCreate = mutableStateListOf<ErrorCreate<*>>()

@Composable
fun ButtonCreate(
	folder: MutableState<String>,
	fileName: MutableState<String>,
	templateValue: MutableState<Template>,
	type: MutableState<TemplateType>
) {
	Button(
		modifier = Modifier.paddingFromBaseline(bottom = 10.dp),
		enabled = canCreate.isEmpty(),
		onClick = {
			val file = File(folder.value, "${fileName.value}.json")
			
			if (file.exists()) file.delete()
			file.createNewFile()
			
			val writer = file.writer()
			val serializer = Json {
				classDiscriminator = CLASS_TYPE_TO_REMOVE
				isLenient = true
				coerceInputValues = true
				prettyPrint = true
			}
			
			val json = serializer.encodeToJsonElement(templateValue.value castWith type.value)
			val jsonFixed = json.jsonObject.filterNot {
				it.key == CLASS_TYPE_TO_REMOVE || runCatching { it.value.jsonPrimitive.contentOrNull?.isEmpty() == true }.isSuccess
			}
			
			writer.write(serializer.encodeToString(jsonFixed))
			writer.close()
		}
	) {
		Text("Create")
	}
}
