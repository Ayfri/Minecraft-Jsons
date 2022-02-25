package utils

import java.util.*

fun String.title(): String {
	val words = split(" ")
	val result = StringBuilder()
	for (word in words) {
		result.append(word.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
		result.append(" ")
	}
	return result.toString().trim()
}
