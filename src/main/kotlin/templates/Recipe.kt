package templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import composables.TemplateValue
import composables.TemplateValueEnum
import composables.TemplateValueList
import composables.TemplateValueMap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.serializer

@Serializable
enum class RecipeType {
	BLASTING,
	BLASTING_MULTI,
	CAMPFIRE_COOKING,
	CAMPFIRE_COOKING_MULTI,
	CRAFTING_SHAPED,
	CRAFTING_SHAPELESS,
	CRAFTING_SPECIAL,
	SMELTING,
	SMELTING_MULTI,
	SMITHING,
	SMOKING,
	SMOKING_MULTI,
	STONECUTTING,
	STONECUTTING_MULTI;
	
	fun toTemplate(): VanillaTemplate {
		return when (this) {
			BLASTING -> BlastingRecipeSingleTemplate()
			BLASTING_MULTI -> BlastingRecipeMultiTemplate()
			CAMPFIRE_COOKING -> CampFireRecipeSingleTemplate()
			CAMPFIRE_COOKING_MULTI -> CampFireRecipeMultiTemplate()
			CRAFTING_SHAPED -> CraftingShapedRecipeTemplate()
			CRAFTING_SHAPELESS -> CraftingShapelessRecipeTemplate()
			CRAFTING_SPECIAL -> CraftingSpecialRecipeTemplate()
			SMELTING -> SmeltingRecipeSingleTemplate()
			SMELTING_MULTI -> SmeltingRecipeMultiTemplate()
			SMITHING -> SmithingRecipeTemplate()
			SMOKING -> SmokingRecipeSingleTemplate()
			SMOKING_MULTI -> SmokingRecipeMultiTemplate()
			STONECUTTING -> StoneCuttingRecipeSingleTemplate()
			STONECUTTING_MULTI -> StoneCuttingRecipeMultiTemplate()
		}
	}
}

@Serializable
enum class SpecialRecipes {
	ARMORDYE,
	BANNERDUPLICATE,
	BOOKCLONING,
	FIREWORK_ROCKET,
	FIREWORK_STAR,
	FIREWORK_STAR_FADE,
	MAPCLONING,
	MAPEXTENDING,
	REPAIRITEM,
	SHIELDDECORATION,
	SHULKERBOXCOLORING,
	TIPPEDARROW,
	SUSPICIOUSSTEW,
}

@Serializable
sealed class RecipeTemplate : VanillaTemplate() {
	@Serializable(with = MutableStateSerializerLowerCase::class)
	var type = mutableStateOf(RecipeType.CRAFTING_SHAPELESS)
}

@Serializable
class CraftResultTemplate : VanillaTemplate() {
	@Serializable(with = MutableStateSerializerLowerCase::class)
	val result = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializerLowerCase::class)
	val count = mutableStateOf(0)
	
	@Composable
	override fun Content() {
		Row {
			TemplateValue("result", result)
			TemplateValue("count", count)
		}
	}
}

@Serializable
sealed class CookingRecipeSingle : RecipeTemplate() {
	@SerialName("cookingtime")
	@Serializable(with = MutableStateSerializer::class)
	val cookingTime = mutableStateOf(0)
	
	@Serializable(with = MutableStateSerializer::class)
	val experience = mutableStateOf(0.0)
	
	@Serializable(with = MutableStateSerializer::class)
	val group = mutableStateOf("")
	
	@Serializable(with = MutableStateSerializer::class)
	val ingredient = mutableStateOf(ItemTemplate())
	
	@Serializable(with = MutableStateSerializer::class)
	val result = mutableStateOf("")
	
	@Composable
	override fun Content() {
		Column {
			ingredient.value.Content()
			Row {
				TemplateValue("result", result)
				TemplateValue("cooking time", cookingTime)
				TemplateValue("experience", experience)
				TemplateValue("group", group)
			}
		}
	}
}

@Serializable
sealed class CookingRecipeMulti : RecipeTemplate() {
	@Serializable(with = MutableStateSerializer::class)
	val cookingTime = mutableStateOf(0)
	
	@Serializable(with = MutableStateSerializer::class)
	val experience = mutableStateOf(0.0)
	
	@Serializable(with = MutableStateSerializer::class)
	val group = mutableStateOf("")
	
	@Serializable(with = SnapshotListSerializer::class)
	val ingredient = mutableStateListOf<ItemTemplate>()
	
	@Serializable(with = MutableStateSerializer::class)
	val result = mutableStateOf("")
	
	@Composable
	override fun Content() {
		Column {
			TemplateValueList(ingredient)
			
			Row {
				TemplateValue("result", result)
				TemplateValue("cooking time", cookingTime)
				TemplateValue("experience", experience)
				TemplateValue("group", group)
			}
		}
	}
}

@Serializable
sealed class CraftRecipe : RecipeTemplate() {
	@Serializable(with = MutableStateSerializer::class)
	val result = mutableStateOf(CraftResultTemplate())
}

@Serializable
class BlastingRecipeSingleTemplate : CookingRecipeSingle() {
	init {
		cookingTime.value = 100
		type.value = RecipeType.BLASTING
	}
}

@Serializable
class BlastingRecipeMultiTemplate : CookingRecipeMulti() {
	init {
		cookingTime.value = 100
		type.value = RecipeType.BLASTING
	}
}

@Serializable
class CampFireRecipeSingleTemplate : CookingRecipeSingle() {
	init {
		cookingTime.value = 100
		type.value = RecipeType.CAMPFIRE_COOKING
	}
}

@Serializable
class CampFireRecipeMultiTemplate : CookingRecipeMulti() {
	init {
		cookingTime.value = 100
		type.value = RecipeType.CAMPFIRE_COOKING_MULTI
	}
}

@Serializable
class CraftingShapedRecipeTemplate : CraftRecipe() {
	@Serializable(with = SnapshotListSerializer::class)
	val pattern = mutableStateListOf<String>()
	
	@Serializable(with = SnapshotMapSerializer::class)
	val key = mutableStateMapOf<String, CraftResultTemplate>()
	
	init {
		type.value = RecipeType.CRAFTING_SHAPED
	}
	
	@Composable
	override fun Content() {
		Column {
			TemplateValueList("pattern", pattern)
			TemplateValueMap(key)
			result.value.Content()
		}
	}
}

@Serializable
class CraftingShapelessRecipeTemplate : CraftRecipe() {
	@Serializable(with = SnapshotMapSerializer::class)
	val ingredients = mutableStateMapOf<String, CraftResultTemplate>()
	
	init {
		type.value = RecipeType.CRAFTING_SHAPELESS
	}
	
	@Composable
	override fun Content() {
		Column {
			TemplateValueMap(ingredients)
			result.value.Content()
		}
	}
}

@Serializable(with = CraftingSpecialRecipeSerializer::class)
class CraftingSpecialRecipeTemplate : RecipeTemplate() {
	@Serializable(with = MutableStateSerializerLowerCase::class)
	val specialType = mutableStateOf(SpecialRecipes.ARMORDYE)
	
	@Composable
	override fun Content() {
		TemplateValueEnum("type", specialType) { it.name.lowercase() }
	}
	
}

object CraftingSpecialRecipeSerializer : KSerializer<CraftingSpecialRecipeTemplate> {
	override fun deserialize(decoder: Decoder): CraftingSpecialRecipeTemplate {
		return decoder.decodeStructure(descriptor) {
			val type = decodeStringElement(descriptor, 0)
			
			CraftingSpecialRecipeTemplate().apply {
				specialType.value = SpecialRecipes.valueOf(type)
			}
		}
	}
	
	override val descriptor = buildClassSerialDescriptor("CraftingSpecialRecipeTemplate") {
		element("type", MutableStateSerializerLowerCase(serializer<String>()).descriptor)
	}
	
	
	override fun serialize(encoder: Encoder, value: CraftingSpecialRecipeTemplate) {
		encoder.encodeStructure(descriptor) {
			encodeStringElement(descriptor, 0, value.specialType.value.name.lowercase())
		}
	}
}

@Serializable
class SmeltingRecipeSingleTemplate : CookingRecipeSingle() {
	init {
		cookingTime.value = 200
		type.value = RecipeType.SMELTING
	}
}

@Serializable
class SmeltingRecipeMultiTemplate : CookingRecipeMulti() {
	init {
		cookingTime.value = 200
		type.value = RecipeType.SMELTING_MULTI
	}
}

@Serializable
class SmithingRecipeTemplate : RecipeTemplate() {
	@Serializable(with = MutableStateSerializer::class)
	val addition = mutableStateOf(ItemTemplate())
	
	@Serializable(with = MutableStateSerializer::class)
	val base = mutableStateOf(ItemTemplate())
	
	@Serializable(with = MutableStateSerializer::class)
	val result = mutableStateOf("")
	
	init {
		type.value = RecipeType.SMITHING
	}
	
	@Composable
	override fun Content() {
		Column {
			addition.value.Content()
			base.value.Content()
			
			TemplateValue("result", result)
		}
	}
}

@Serializable
class SmokingRecipeSingleTemplate : CookingRecipeSingle() {
	init {
		cookingTime.value = 100
		type.value = RecipeType.SMOKING
	}
}

@Serializable
class SmokingRecipeMultiTemplate : CookingRecipeMulti() {
	init {
		cookingTime.value = 100
		type.value = RecipeType.SMOKING_MULTI
	}
}

@Serializable
class StoneCuttingRecipeSingleTemplate : RecipeTemplate() {
	@Serializable(with = MutableStateSerializer::class)
	val count = mutableStateOf(0)
	
	@Serializable(with = MutableStateSerializer::class)
	val ingredient = mutableStateOf(ItemTemplate())
	
	@Serializable(with = MutableStateSerializer::class)
	val result = mutableStateOf("")
	
	init {
		type.value = RecipeType.STONECUTTING
	}
	
	@Composable
	override fun Content() {
		Column {
			TemplateValue("count", count)
			ingredient.value.Content()
			TemplateValue("result", result)
		}
	}
}

@Serializable
class StoneCuttingRecipeMultiTemplate : RecipeTemplate() {
	@Serializable(with = MutableStateSerializer::class)
	val count = mutableStateOf(0)
	
	@Serializable(with = SnapshotListSerializer::class)
	val ingredient = mutableStateListOf<ItemTemplate>()
	
	@Serializable(with = MutableStateSerializer::class)
	val result = mutableStateOf("")
	
	init {
		type.value = RecipeType.STONECUTTING_MULTI
	}
	
	@Composable
	override fun Content() {
		Column {
			TemplateValue("count", count)
			TemplateValueList(ingredient)
			TemplateValue("result", result)
		}
	}
}
