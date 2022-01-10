package composables

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

@Composable
fun FilenameSelector(fileName: MutableState<String>) {
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
}
