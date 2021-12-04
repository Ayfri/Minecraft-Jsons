package templates

import kotlinx.serialization.Serializable

@Serializable
class ItemTemplate(val id: String, val tag: String? = null) : VanillaTemplate()
