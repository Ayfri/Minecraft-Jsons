
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import composables.ButtonFolderSelector
import composables.DropDown
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import templates.CraftingShapedRecipeTemplate
import templates.RecipeType
import templates.Template
import templates.TemplateType
import java.io.File
import java.nio.file.Paths
import javax.swing.JComponent

lateinit var frame: JComponent

const val CLASS_TYPE_TO_REMOVE = "class_type_to_remove"

@OptIn(InternalSerializationApi::class)
@Composable
@Preview
fun App() {
	val fileName = remember { mutableStateOf("file") }
	val folder = remember { mutableStateOf(Paths.get("").toAbsolutePath().normalize().toString()) }
	val templateValue = remember { mutableStateOf<Template>(CraftingShapedRecipeTemplate()) }
	val templateName = remember { mutableStateOf<Any?>(RecipeType.values()[0]) }
	val type = remember { mutableStateOf(TemplateType.RECIPE) }
	
	Row {
		Column(
			modifier = Modifier.drawBehind {
				drawLine(
					start = Offset(size.width, 0f),
					end = Offset(size.width, size.height),
					brush = SolidColor(Color.LightGray)
				)
			}.weight(0.3f).padding(end = 10.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			DropDown(type, "Type") { it.name }
			
			if (type.value == TemplateType.RECIPE) {
				Column(
					modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f).padding(top = 100.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					val recipeType = remember { mutableStateOf(RecipeType.CRAFTING_SHAPED) }
					
					OutlinedTextField(
						value = fileName.value,
						label = { Text("File Name") },
						onValueChange = { fileName.value = it },
						visualTransformation = {
							TransformedText(
								text = it + AnnotatedString(".json", SpanStyle(color = Color.Gray)),
								offsetMapping = OffsetMapping.Identity
							)
						}
					)
					
					DropDown(recipeType, "Recipe Type") {
						templateName.value = it
						templateValue.value = it.toTemplate()
					}
				}
			}
			
			Button(
				modifier = Modifier.paddingFromBaseline(bottom = 10.dp),
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
						it.key == CLASS_TYPE_TO_REMOVE
								|| try {
							it.value.jsonPrimitive.contentOrNull?.isEmpty() == true
						} catch (_: Exception) {
							false
						}
					}
					
					writer.write(serializer.encodeToString(jsonFixed))
					writer.close()
				}
			) {
				Text("Create")
			}
		}
		
		Column(
			modifier = Modifier.weight(0.7f)
		) {
			Row(
				modifier = Modifier.padding(vertical = 10.dp).drawBehind {
					drawLine(
						start = Offset(0f, size.height + 10),
						end = Offset(size.width, size.height + 10),
						brush = SolidColor(Color.LightGray)
					)
				}.padding(horizontal = 10.dp)
			) {
				TextField(
					value = folder.value,
					label = { Text("Folder") },
					onValueChange = {},
					readOnly = true,
					modifier = Modifier.fillMaxWidth(0.8f),
				)
				
				Box(
					modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically),
				) {
					ButtonFolderSelector(folder, Modifier.align(Alignment.Center))
				}
			}
			
			Column(
				modifier = Modifier.fillMaxSize().padding(15.dp),
			) {
				composables.Template(templateValue.value)
			}
		}
	}
}

fun main() = application {
	val state = rememberWindowState(size = DpSize(1200.dp, 700.dp))
	Window(onCloseRequest = ::exitApplication, title = "Minecraft JSON Generator", state = state, resizable = false) {
		App()
		frame = window.rootPane
	}
}
