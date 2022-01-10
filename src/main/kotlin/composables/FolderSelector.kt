package composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

@Composable
fun FolderSelector(folder: MutableState<String>) {
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
}
