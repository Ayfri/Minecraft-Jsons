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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
abstract class RecipeTemplate : VanillaTemplate() {
	val type = mutableStateOf(RecipeType.CRAFTING_SHAPELESS)
}

@Serializable
class CraftResultTemplate : VanillaTemplate() {
	val result = mutableStateOf("")
	val count = mutableStateOf(0)
	
	@Composable
	override fun Content() {
		Row {
			TemplateValue("result", result)
			TemplateValue("count", count)
		}
	}
}

abstract class CookingRecipeSingle(
	type: RecipeType,
) : RecipeTemplate() {
	@SerialName("cookingtime")
	val cookingTime = mutableStateOf(0)
	val experience = mutableStateOf(0.0)
	val group = mutableStateOf("")
	val ingredient = mutableStateOf(ItemTemplate())
	val result = mutableStateOf("")
	
	init {
		this.type.value = type
	}
	
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

abstract class CookingRecipeMulti(
	type: RecipeType
) : RecipeTemplate() {
	val cookingTime = mutableStateOf(0)
	val experience = mutableStateOf(0.0)
	val group = mutableStateOf("")
	val ingredient = mutableStateListOf<ItemTemplate>()
	val result = mutableStateOf("")
	
	init {
		this.type.value = type
	}
	
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

abstract class CraftRecipe(type: RecipeType) : RecipeTemplate() {
	val result = mutableStateOf(CraftResultTemplate())
	
	init {
		this.type.value = type
	}
}

class BlastingRecipeSingleTemplate : CookingRecipeSingle(RecipeType.BLASTING) {
	init {
		cookingTime.value = 100
	}
}

class BlastingRecipeMultiTemplate : CookingRecipeMulti(RecipeType.BLASTING_MULTI) {
	init {
		cookingTime.value = 100
	}
}

class CampFireRecipeSingleTemplate : CookingRecipeSingle(RecipeType.CAMPFIRE_COOKING) {
	init {
		cookingTime.value = 100
	}
}

class CampFireRecipeMultiTemplate : CookingRecipeMulti(RecipeType.CAMPFIRE_COOKING_MULTI) {
	init {
		cookingTime.value = 100
	}
}

class CraftingShapedRecipeTemplate : CraftRecipe(RecipeType.CRAFTING_SHAPED) {
	val pattern = mutableStateListOf<String>()
	val key = mutableStateMapOf<String, CraftResultTemplate>()
	
	@Composable
	override fun Content() {
		Column {
			TemplateValueList("pattern", pattern)
			TemplateValueMap(key)
			result.value.Content()
		}
	}
}


class CraftingShapelessRecipeTemplate: CraftRecipe(RecipeType.CRAFTING_SHAPELESS) {
	val ingredients = mutableStateMapOf<String, CraftResultTemplate>()
	
	@Composable
	override fun Content() {
		Column {
			TemplateValueMap(ingredients)
			result.value.Content()
		}
	}
}

class CraftingSpecialRecipeTemplate : VanillaTemplate() {
	val type = mutableStateOf(SpecialRecipes.ARMORDYE)
	
	@Composable
	override fun Content() {
		TemplateValueEnum("type", type) { it.name.lowercase() }
	}
}

class SmeltingRecipeSingleTemplate : CookingRecipeSingle(RecipeType.SMELTING) {
	init {
		cookingTime.value = 200
	}
}

class SmeltingRecipeMultiTemplate : CookingRecipeMulti(RecipeType.SMELTING_MULTI) {
	init {
		cookingTime.value = 200
	}
}

class SmithingRecipeTemplate : RecipeTemplate() {
	val addition = mutableStateOf(ItemTemplate())
	val base = mutableStateOf(ItemTemplate())
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

class SmokingRecipeSingleTemplate : CookingRecipeSingle(RecipeType.SMOKING) {
	init {
		cookingTime.value = 100
	}
}

class SmokingRecipeMultiTemplate : CookingRecipeMulti(RecipeType.SMOKING_MULTI) {
	init {
		cookingTime.value = 100
	}
}

class StoneCuttingRecipeSingleTemplate : RecipeTemplate() {
	val count = mutableStateOf(0)
	val ingredient = mutableStateOf(ItemTemplate())
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

class StoneCuttingRecipeMultiTemplate : RecipeTemplate() {
	val count = mutableStateOf(0)
	val ingredient = mutableStateListOf<ItemTemplate>()
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
