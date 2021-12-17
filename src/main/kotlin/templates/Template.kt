package templates

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

sealed interface Template {
	@Composable
	fun Content()
	
	infix fun castWith(type: TemplateType) = when (type) {
		TemplateType.RECIPE -> this as RecipeTemplate
	}
}

enum class TemplateType {
	RECIPE;
}

@Serializable
sealed class FabricTemplate : Template

@Serializable
sealed class ForgeTemplate : Template

@Serializable
sealed class VanillaTemplate : Template
