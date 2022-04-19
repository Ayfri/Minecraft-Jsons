package templates

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import composables.TemplateValue
import composables.TemplateValueEnum
import composables.TemplateValueList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import serializers.CraftingSpecialRecipeSerializer
import serializers.MutableStateSerializer
import serializers.MutableStateSerializerLowerCase
import serializers.SnapshotListMapSerializer
import serializers.SnapshotListSerializer
import serializers.TemplateMapSerializable

enum class RecipeType : ITemplateType<VanillaTemplate> {
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
	
	override fun toTemplate() = when (this) {
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


@Serializable
enum class RecipeTypeSerializable {
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
	@Transient
	var type = mutableStateOf(RecipeTypeSerializable.CRAFTING_SHAPELESS)
}

@Serializable
open class CraftResultTemplate : VanillaTemplate() {
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
sealed class CraftResultTemplateMap : CraftResultTemplate(), TemplateMapSerializable {
	@Transient
	override val key = mutableStateOf("")
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
		ingredient.value.Content()
		Row {
			TemplateValue("result", result)
			TemplateValue("cooking time", cookingTime)
			TemplateValue("experience", experience)
			TemplateValue("group", group)
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
		TemplateValueList("ingredients", ingredient)
		
		Row {
			TemplateValue("result", result)
			TemplateValue("cooking time", cookingTime)
			TemplateValue("experience", experience)
			TemplateValue("group", group)
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
		type.value = RecipeTypeSerializable.BLASTING
	}
}

@Serializable
class BlastingRecipeMultiTemplate : CookingRecipeMulti() {
	init {
		cookingTime.value = 100
		type.value = RecipeTypeSerializable.BLASTING
	}
}

@Serializable
class CampFireRecipeSingleTemplate : CookingRecipeSingle() {
	init {
		cookingTime.value = 100
		type.value = RecipeTypeSerializable.CAMPFIRE_COOKING
	}
}

@Serializable
class CampFireRecipeMultiTemplate : CookingRecipeMulti() {
	init {
		cookingTime.value = 100
		type.value = RecipeTypeSerializable.CAMPFIRE_COOKING_MULTI
	}
}

@Serializable
class CraftingShapedRecipeTemplate : CraftRecipe() {
	@Serializable(with = SnapshotListSerializer::class)
	val pattern = mutableStateListOf<String>()
	
	@Serializable(with = SnapshotListMapSerializer::class)
	val key = mutableStateListOf<ItemTemplateMap>()
	
	init {
		type.value = RecipeTypeSerializable.CRAFTING_SHAPED
	}
	
	@Composable
	override fun Content() {
		TemplateValueList("pattern", pattern, limit = 3)
		TemplateValueList("keys", key, 9, true)
		result.value.Content()
	}
}

@Serializable
class CraftingShapelessRecipeTemplate : CraftRecipe() {
	@Serializable(with = SnapshotListMapSerializer::class)
	val ingredients = mutableStateListOf<CraftResultTemplateMap>()
	
	init {
		type.value = RecipeTypeSerializable.CRAFTING_SHAPELESS
	}
	
	@Composable
	override fun Content() {
		TemplateValueList("ingredients", ingredients, 9, true)
		result.value.Content()
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

@Serializable
class SmeltingRecipeSingleTemplate : CookingRecipeSingle() {
	init {
		cookingTime.value = 200
		type.value = RecipeTypeSerializable.SMELTING
	}
}

@Serializable
class SmeltingRecipeMultiTemplate : CookingRecipeMulti() {
	init {
		cookingTime.value = 200
		type.value = RecipeTypeSerializable.SMELTING_MULTI
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
		type.value = RecipeTypeSerializable.SMITHING
	}
	
	@Composable
	override fun Content() {
		addition.value.Content()
		base.value.Content()
		
		TemplateValue("result", result)
	}
}

@Serializable
class SmokingRecipeSingleTemplate : CookingRecipeSingle() {
	init {
		cookingTime.value = 100
		type.value = RecipeTypeSerializable.SMOKING
	}
}

@Serializable
class SmokingRecipeMultiTemplate : CookingRecipeMulti() {
	init {
		cookingTime.value = 100
		type.value = RecipeTypeSerializable.SMOKING_MULTI
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
		type.value = RecipeTypeSerializable.STONECUTTING
	}
	
	@Composable
	override fun Content() {
		TemplateValue("count", count)
		ingredient.value.Content()
		TemplateValue("result", result)
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
		type.value = RecipeTypeSerializable.STONECUTTING_MULTI
	}
	
	@Composable
	override fun Content() {
		TemplateValue("count", count)
		TemplateValueList("ingredients", ingredient)
		TemplateValue("result", result)
	}
}
