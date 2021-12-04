package templates

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class RecipeType {
	BLASTING,
	CAMPFIRE_COOKING,
	CRAFTING_SHAPED,
	CRAFTING_SHAPELESS,
	CRAFTING_SPECIAL,
	SMELTING,
	SMITHING,
	SMOKING,
	STONECUTTING
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
abstract class RecipeTemplate(val type: RecipeType) : VanillaTemplate()

@Serializable
abstract class CraftResultTemplate(val result: String, val count: Int) : VanillaTemplate()

abstract class CookingRecipeSingle(
	type: RecipeType, val ingredient: ItemTemplate, val result: String, val experience: Double, @SerialName("cookingtime") val cookingTime: Int = 0,
	val group: String? = null
) : RecipeTemplate(type)

abstract class CookingRecipeMulti(
	type: RecipeType, val ingredient: List<ItemTemplate>, val result: String, val experience: Double, @SerialName("cookingtime") val cookingTime: Int = 0,
	val group: String? = null
) : RecipeTemplate(type)

abstract class CraftRecipe(type: RecipeType, val result: CraftResultTemplate) : RecipeTemplate(type)

class BlastingRecipeSingleTemplate(ingredient: ItemTemplate, result: String, experience: Double, cookingTime: Int = 100) :
	CookingRecipeSingle(RecipeType.BLASTING, ingredient, result, experience, cookingTime)

class BlastingRecipeMultiTemplate(ingredient: List<ItemTemplate>, result: String, experience: Double, cookingTime: Int = 100) :
	CookingRecipeMulti(RecipeType.BLASTING, ingredient, result, experience, cookingTime)

class CampFireRecipeSingleTemplate(ingredient: ItemTemplate, result: String, experience: Double, cookingTime: Int = 100) :
	CookingRecipeSingle(RecipeType.CAMPFIRE_COOKING, ingredient, result, experience, cookingTime)

class CampFireRecipeMultiTemplate(ingredient: List<ItemTemplate>, result: String, experience: Double, cookingTime: Int = 100) :
	CookingRecipeMulti(RecipeType.CAMPFIRE_COOKING, ingredient, result, experience, cookingTime)

class CraftingShapedRecipeTemplate(
	val pattern: List<String>, val key: Map<String, CraftResultTemplate>, result: CraftResultTemplate
) : CraftRecipe(RecipeType.CRAFTING_SHAPED, result)


class CraftingShapelessRecipeTemplate(
	val ingredients: Map<String, CraftResultTemplate>, result: CraftResultTemplate
) : CraftRecipe(RecipeType.CRAFTING_SHAPELESS, result)

class CraftingSpecialRecipeTemplate(val type: SpecialRecipes) : VanillaTemplate()

class SmeltingRecipeSingleTemplate(ingredient: ItemTemplate, result: String, experience: Double, cookingTime: Int = 200) :
	CookingRecipeSingle(RecipeType.SMELTING, ingredient, result, experience, cookingTime)

class SmeltingRecipeMultiTemplate(ingredient: List<ItemTemplate>, result: String, experience: Double, cookingTime: Int = 200) :
	CookingRecipeMulti(RecipeType.SMELTING, ingredient, result, experience, cookingTime)

class SmithingRecipeTemplate(val base: ItemTemplate, val addition: ItemTemplate, val result: String) : RecipeTemplate(RecipeType.SMITHING)

class SmokingRecipeSingleTemplate(ingredient: ItemTemplate, result: String, experience: Double, cookingTime: Int = 100) :
	CookingRecipeSingle(RecipeType.SMOKING, ingredient, result, experience, cookingTime)

class SmokingRecipeMultiTemplate(ingredient: List<ItemTemplate>, result: String, experience: Double, cookingTime: Int = 100) :
	CookingRecipeMulti(RecipeType.SMOKING, ingredient, result, experience, cookingTime)

class StoneCuttingRecipeSingleTemplate(result: String, count: Int, ingredient: ItemTemplate) : RecipeTemplate(RecipeType.STONECUTTING)
class StoneCuttingRecipeMultiTemplate(result: String, count: Int, ingredient: List<ItemTemplate>) : RecipeTemplate(RecipeType.STONECUTTING)
