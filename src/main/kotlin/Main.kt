
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
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
	val type = remember { mutableStateOf(TemplateType.RECIPE) }
	
	Row {
		DropDown(
			TemplateType.values().toList(),
			type,
			{ it.name },
			"Type",
		)
		
		Row {
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
	}
	
	if (type.value == TemplateType.RECIPE) {
		Column(
			modifier = Modifier.fillMaxSize().padding(top = 100.dp)
		) {
			val fileName = remember { mutableStateOf("") }
			val recipeType = remember { mutableStateOf(RecipeType.CRAFTING_SHAPED) }
			
			OutlinedTextField(
				value = fileName.value,
				label = { Text("File Name") },
				onValueChange = { fileName.value = it },
			)
			
			DropDown(
				RecipeType.values().toList(),
				recipeType,
				{ it.name },
				"Recipe Type",
			)
		}
	}
}

fun main() = application {
	Window(onCloseRequest = ::exitApplication) {
		App()
		frame = window.rootPane
	}
}
