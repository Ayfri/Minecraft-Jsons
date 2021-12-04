package composables

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
import androidx.compose.runtime.MutableState
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

@Composable
fun DropDown(
	items: List<String>,
	selection: MutableState<String>,
	label: String,
) {
	Column {
		var expanded by remember { mutableStateOf(false) }
		var textFieldSize by remember { mutableStateOf(Size.Zero) }
		
		OutlinedTextField(
			label = { Text(label) },
			value = selection.value,
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
			for (t in items) {
				DropdownMenuItem(
					onClick = {
						selection.value = t
						expanded = false
					},
				) {
					Text(t)
				}
			}
		}
	}
}

@Composable
fun <T> DropDown(
	items: List<T>,
	selection: MutableState<T>,
	transform: (T) -> String,
	label: String,
) {
	Column {
		var expanded by remember { mutableStateOf(false) }
		var textFieldSize by remember { mutableStateOf(Size.Zero) }
		
		OutlinedTextField(
			label = { Text(label) },
			value = transform(selection.value),
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
			for (t in items) {
				DropdownMenuItem(
					onClick = {
						selection.value = t
						expanded = false
					},
				) {
					Text(transform(t))
				}
			}
		}
	}
}
