package templates

import androidx.compose.runtime.Composable

interface Template {
	@Composable
	fun Content()
}

enum class TemplateType {
	RECIPE
}

abstract class FabricTemplate : Template
abstract class ForgeTemplate : Template
abstract class VanillaTemplate : Template
