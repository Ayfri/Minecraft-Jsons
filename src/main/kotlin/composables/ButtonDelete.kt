package composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <T> ButtonDelete(list: SnapshotStateList<T>, index: Int) {
	TextButton(
		onClick = {
			list.removeAt(index)
		},
		colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.LightGray),
		modifier = Modifier.size(32.dp).padding(end = 5.dp),
		contentPadding = PaddingValues(0.dp)
	) {
		Icon(Icons.Outlined.Delete, "delete")
	}
}
