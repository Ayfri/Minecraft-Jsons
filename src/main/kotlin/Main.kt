import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import composables.ButtonCreate
import composables.DropDown
import composables.FilenameSelector
import composables.FolderSelector
import kotlinx.serialization.InternalSerializationApi
import templates.CraftingShapedRecipeTemplate
import templates.RecipeType
import templates.Template
import templates.TemplateType
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
					
					FilenameSelector(fileName)
					
					DropDown(recipeType, "Recipe Type") {
						templateName.value = it
						templateValue.value = it.toTemplate()
					}
				}
			}
			
			ButtonCreate(folder, fileName, templateValue, type)
		}
		
		Column(
			modifier = Modifier.weight(0.7f)
		) {
			FolderSelector(folder)
			
			Box(modifier = Modifier.fillMaxSize().padding(15.dp)) {
				val stateVertical = rememberScrollState(0)
				composables.Template(templateValue.value, Modifier.verticalScroll(stateVertical))
				VerticalScrollbar(modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(), adapter = rememberScrollbarAdapter(stateVertical))
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
