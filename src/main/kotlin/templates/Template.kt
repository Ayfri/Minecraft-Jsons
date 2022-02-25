package templates

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
sealed class Template {
	@Composable
	abstract fun Content()
	
	infix fun castWith(type: TemplateType) = when (type) {
		TemplateType.RECIPE -> this as RecipeTemplate
		TemplateType.FABRIC -> this as FabricTemplate
	}
}

sealed interface ITemplateType<T : Template> {
	fun toTemplate(): T
}

enum class TemplateType {
	RECIPE,
	FABRIC;
}

@Serializable
sealed class FabricTemplate : Template()

@Serializable
sealed class ForgeTemplate : Template()

@Serializable
sealed class VanillaTemplate : Template()
