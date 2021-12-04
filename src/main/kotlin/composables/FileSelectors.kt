package composables

import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import frame
import javax.swing.JFileChooser
import javax.swing.UIManager

@Composable
fun ButtonFolderSelector(value: MutableState<String>, modifier: Modifier = Modifier) {
	val openFileSelector = remember { mutableStateOf(false) }
	
	Button(
		onClick = {
			openFileSelector.value = true
		},
		modifier = modifier
	) {
		Icon(Icons.Filled.Folder, "Folder")
	}
	
	if (openFileSelector.value) {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
		val chooser = JFileChooser()
		chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
		val returnVal = chooser.showOpenDialog(frame)
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			value.value = chooser.selectedFile.absolutePath
			openFileSelector.value = false
		}
	}
}
