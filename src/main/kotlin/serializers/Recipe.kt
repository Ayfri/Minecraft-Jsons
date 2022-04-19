package serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import templates.CraftingSpecialRecipeTemplate
import templates.SpecialRecipes

object CraftingSpecialRecipeSerializer : KSerializer<CraftingSpecialRecipeTemplate> {
	override fun deserialize(decoder: Decoder) = decoder.decodeStructure(descriptor) {
		val type = decodeStringElement(descriptor, 0)
		
		CraftingSpecialRecipeTemplate().apply {
			specialType.value = SpecialRecipes.valueOf(type)
		}
	}
	
	override val descriptor = buildClassSerialDescriptor("CraftingSpecialRecipeTemplate") {
		element("type", MutableStateSerializerLowerCase(String.serializer()).descriptor)
	}
	
	override fun serialize(encoder: Encoder, value: CraftingSpecialRecipeTemplate) {
		encoder.encodeStructure(descriptor) {
			encodeStringElement(descriptor, 0, value.specialType.value.name.lowercase())
		}
	}
}
