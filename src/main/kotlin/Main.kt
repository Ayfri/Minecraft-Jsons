import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import templates.TemplateType

@Composable
@Preview
fun App() {
	val folder = remember { mutableStateOf("") }
	val type = remember { mutableStateOf(TemplateType.RECIPE) }
	
	Column(
		modifier = Modifier
	) {
		var expanded by remember { mutableStateOf(false) }
		var textFieldSize by remember { mutableStateOf(Size.Zero) }
		
		OutlinedTextField(
			label = { Text("Type") },
			value = type.value.name,
			onValueChange = {},
			readOnly = true,
			trailingIcon = {
				Icon(if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown, "contentDescription", Modifier.clickable { expanded = !expanded })
			},
			modifier = Modifier.padding(10.dp).onGloballyPositioned {
				textFieldSize = it.size.toSize()
			},
		)
		DropdownMenu(
			expanded = expanded,
			onDismissRequest = { expanded = false },
			modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
		) {
			for (t in TemplateType.values()) {
				DropdownMenuItem(
					onClick = {
						type.value = t
						expanded = false
					},
				) {
					Text(t.name)
				}
			}
		}
	}
}

fun main() = application {
	Window(onCloseRequest = ::exitApplication) {
		App()
	}
}
