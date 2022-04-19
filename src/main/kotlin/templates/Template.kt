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
	
	inline fun <reified E : Enum<*>> toTemplateValues()  = when (this) {
		RECIPE -> RecipeType.values() as Array<E>
		FABRIC -> FabricType.values() as Array<E>
	}
	
	fun toTemplateSubType() = (toTemplateValues() as Array<ITemplateType<*>>).first().toTemplate()
}

@Serializable
sealed class FabricTemplate : Template()

@Serializable
sealed class ForgeTemplate : Template()

@Serializable
sealed class VanillaTemplate : Template()
