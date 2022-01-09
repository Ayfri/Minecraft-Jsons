@file:OptIn(ExperimentalComposeUiApi::class)

package composables

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import templates.Template
import kotlin.reflect.full.createInstance

@Composable
inline fun <reified T : Template> ButtonAdd(
	list: SnapshotStateList<T>,
	modifier: Modifier = Modifier,
) {
	Button(
		modifier = Modifier.then(modifier),
		onClick = {
			list.add(T::class.createInstance())
		},
	) {
		Text("+")
	}
}

@JvmName("ButtonAdd1")
@Composable
inline fun <reified T : Comparable<T>> ButtonAdd(
	list: SnapshotStateList<T>,
	modifier: Modifier = Modifier,
) {
	Button(
		modifier = Modifier.then(modifier),
		onClick = {
			list.add(T::class.createInstance())
		},
	) {
		Text("+")
	}
}

@Composable
inline fun <reified K : Comparable<K>, reified V : Comparable<V>> ButtonAdd(
	map: SnapshotStateMap<K, V>,
	modifier: Modifier = Modifier,
) {
	Button(
		modifier = Modifier.then(modifier),
		onClick = {
			map[K::class.createInstance()] = V::class.createInstance()
		},
	) {
		Text("+")
	}
}

@JvmName("ButtonAdd1")
@Composable
inline fun <reified K : Comparable<K>, reified V : Template> ButtonAdd(
	map: SnapshotStateMap<K, V>,
	modifier: Modifier = Modifier,
) {
	Button(
		modifier = Modifier.then(modifier),
		onClick = {
			map[K::class.createInstance()] = V::class.createInstance()
		},
	) {
		Text("+")
	}
}
