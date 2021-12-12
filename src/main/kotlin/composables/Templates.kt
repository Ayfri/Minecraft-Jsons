package composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import templates.Template

inline fun <reified T : Comparable<T>> convertValue(value: String): T {
	return when (T::class) {
		Int::class -> (try {
			value.toInt()
		} catch (_: Exception) {
			0
		}) as T
		Float::class -> (try {
			value.toFloat()
		} catch (_: Exception) {
			0f
		}) as T
		Double::class -> (try {
			value.toDouble()
		} catch (_: Exception) {
			0.0
		}) as T
		Boolean::class -> (try {
			value.toBoolean()
		} catch (_: Exception) {
			false
		}) as T
		else -> value as T
	}
}

@Composable
inline fun <reified T : Template> Template(
	template: T,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
	) {
		template.Content()
	}
}

@Composable
inline fun <reified T : Comparable<T>> Template(name: String, value: T, modifier: Modifier = Modifier, noinline onValueChange: (String) -> Unit) {
	OutlinedTextField(
		modifier = Modifier.requiredWidthIn(200.dp, 200.dp).then(modifier),
		singleLine = true,
		value = value.toString(),
		label = { Text(name) },
		onValueChange = onValueChange,
	)
}

@Composable
inline fun <reified T : Comparable<T>> TemplateValue(name: String, value: MutableState<T>, modifier: Modifier = Modifier) {
	Template(name, value.value, modifier) {
		value.value = convertValue(it)
	}
}

@Composable
inline fun <reified T : Enum<T>> TemplateValueEnum(name: String, value: MutableState<T>, modifier: Modifier = Modifier, noinline transformer: (T) -> String) {
	DropDown(value, name, modifier, transformer)
}

@Composable
inline fun <reified T : Comparable<T>> TemplateValueList(name: String, value: SnapshotStateList<T>, modifier: Modifier = Modifier) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.SpaceBetween,
	) {
		value.forEachIndexed { i, item ->
			Template(
				name = "$name #$i",
				value = item.toString(),
				modifier = Modifier.then(modifier),
			) {
				value[i] = convertValue(it)
			}
		}
	}
}
@Composable
inline fun <reified T : Template> TemplateValueList(value: SnapshotStateList<T>) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.SpaceBetween,
	) {
		value.forEach { it.Content() }
	}
}

@Composable
inline fun <reified K : Comparable<K>, reified V : Comparable<V>>TemplateValueMap(name: String, value: SnapshotStateMap<K, V>, modifier: Modifier = Modifier) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.SpaceBetween,
	) {
		value.forEach { (key, v) ->
			Template(
				name = "$name $key",
				value = v.toString(),
				modifier = Modifier.then(modifier),
			) {
				value[key] = convertValue(it)
			}
		}
	}
}

@Composable
inline fun <reified K : Comparable<K>, reified V : Template>TemplateValueMap(value: SnapshotStateMap<K, V>) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.SpaceBetween,
	) {
		value.values.forEach { it.Content() }
	}
}
