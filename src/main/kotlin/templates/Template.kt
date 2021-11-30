package templates

import kotlinx.serialization.Serializable

@Serializable
open class Template

enum class TemplateType {
	RECIPE
}

open class FabricTemplate : Template()
open class ForgeTemplate : Template()
open class VanillaTemplate : Template()
