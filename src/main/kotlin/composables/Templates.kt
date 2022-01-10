package composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import templates.Template
import templates.TemplateMapSerializable

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
	modifier: Modifier = Modifier,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
	) {
		Column(modifier = Modifier.then(modifier)) {
			template.Content()
		}
	}
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
inline fun <reified T : Comparable<T>> Template(
	name: String,
	value: T,
	modifier: Modifier = Modifier,
	isError: Boolean = false,
	required: Boolean = false,
	errorMessage: String? = null,
	noinline onValueChange: (String) -> Unit = {}
) {
	val isErrorEmpty = required && value.toString().isEmpty()
	
	OutlinedTextField(
		modifier = Modifier.requiredWidthIn(200.dp, 200.dp).then(modifier),
		singleLine = true,
		value = value.toString(),
		label = {
			if (required) Text(
				text = AnnotatedString("$name*", SpanStyle(color = if (isErrorEmpty) MaterialTheme.colors.error else MaterialTheme.colors.onBackground)),
				style = MaterialTheme.typography.caption,
				modifier = Modifier.padding(start = 8.dp, end = 8.dp),
			) else Text(name)
		},
		onValueChange = onValueChange,
		trailingIcon = {
			if (isError || isErrorEmpty) TooltipArea(
				modifier = Modifier.padding(start = 8.dp, end = 8.dp),
				tooltip = {
					Box(
						modifier = Modifier.shadow(7.dp, clip = false).background(color = MaterialTheme.colors.background, shape = RoundedCornerShape(10)).padding(5.dp),
					) {
						Text(if (isErrorEmpty) "Required field." else errorMessage ?: "", style = MaterialTheme.typography.subtitle1)
					}
				})
			{
				Icon(
					imageVector = Icons.Filled.Error,
					contentDescription = "error",
					tint = MaterialTheme.colors.error,
				)
			}
		},
		isError = isError,
	)
}

@Composable
inline fun <reified T : Comparable<T>> TemplateValue(
	name: String,
	value: MutableState<T>,
	modifier: Modifier = Modifier,
	isError: Boolean = false,
	required: Boolean = false,
	errorMessage: String? = null,
) {
	Template(name, value.value, modifier, isError, required, errorMessage) {
		value.value = convertValue(it)
	}
}

@Composable
inline fun <reified T : Enum<T>> TemplateValueEnum(name: String, value: MutableState<T>, modifier: Modifier = Modifier, noinline transformer: (T) -> String) {
	DropDown(value, name, modifier, transformer)
}

@Composable
inline fun <reified T : Comparable<T>> TemplateValueList(name: String, value: SnapshotStateList<T>, modifier: Modifier = Modifier, limit: Int = Int.MAX_VALUE) {
	Row(
		modifier = Modifier.fillMaxWidth()
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.SpaceBetween,
		) {
			value.forEachIndexed { i, item ->
				Row(
					verticalAlignment = Alignment.CenterVertically,
				) {
					ButtonDelete(value, i)
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
		
		if (value.size < limit) ButtonAdd(value)
	}
}

@Composable
inline fun <reified T : Template> TemplateValueList(value: SnapshotStateList<T>, limit: Int = Int.MAX_VALUE) {
	Row(
		modifier = Modifier.fillMaxWidth()
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.SpaceBetween,
		) {
			value.forEachIndexed { index, it ->
				Row(
					verticalAlignment = Alignment.CenterVertically,
				) {
					ButtonDelete(value, index)
					it.Content()
				}
			}
		}
		
		if (value.size < limit) ButtonAdd(value)
	}
}

@Composable
inline fun <reified T> TemplateValueList(value: SnapshotStateList<T>, limit: Int = Int.MAX_VALUE, unique: Boolean = false) where T : Template, T : TemplateMapSerializable {
	Row(
		modifier = Modifier.fillMaxWidth()
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.SpaceBetween,
		) {
			value.forEachIndexed { index, v ->
				Row(
					verticalAlignment = Alignment.CenterVertically,
				) {
					ButtonDelete(value, index)
					val errorMessage = mutableStateOf<String?>(null)
					if (value.count { it == v } > 2 && unique) errorMessage.value = "Keys must be unique"
					TemplateValue("key", v.key, required = true, errorMessage = errorMessage.value, isError = errorMessage.value != null)
					Spacer(Modifier.width(20.dp))
					v.Content()
				}
			}
		}
		
		if (value.size < limit) ButtonAdd(value)
	}
}
