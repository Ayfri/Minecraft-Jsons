
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import composables.ButtonFolderSelector
import composables.DropDown
import templates.RecipeType
import templates.TemplateType
import javax.swing.JComponent

lateinit var frame: JComponent

@Composable
@Preview
fun App() {
	val folder = remember { mutableStateOf("") }
	val templateName = remember { mutableStateOf("") }
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
					modifier = Modifier.fillMaxSize().padding(top = 100.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					val fileName = remember { mutableStateOf("") }
					val recipeType = remember { mutableStateOf(RecipeType.CRAFTING_SHAPED) }
					
					OutlinedTextField(
						value = fileName.value,
						label = { Text("File Name") },
						onValueChange = { fileName.value = it },
					)
					
					DropDown(recipeType, "Recipe Type") {
						templateName.value = it.name
						it.name
					}
				}
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
				Text("Values")
				val template = RecipeType.values().find { it.name == templateName.value }?.toRecipe() ?: return@Row
				composables.Template(template)
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
