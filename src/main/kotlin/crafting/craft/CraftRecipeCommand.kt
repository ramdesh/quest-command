package crafting.craft

import core.Player
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.displayToMe
import crafting.Recipe

class CraftRecipeCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Craft", "Make", "Build")
    }

    override fun getDescription(): String {
        return "Craft a recipe you know"
    }

    override fun getManual(): String {
        return """
	Craft <Recipe> - Craft a recipe."""
    }

    override fun getCategory(): List<String> {
        return listOf("Crafting")
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        val knownRecipes = source.knownRecipes
        val pickedRecipes = source.knownRecipes.getAll(argString)

        when {
            args.isEmpty() && knownRecipes.isEmpty() -> source.displayToMe("You don't know any recipes.")
            args.isEmpty() -> chooseRecipe(knownRecipes)
            pickedRecipes.isEmpty() -> chooseRecipe(knownRecipes)
            pickedRecipes.size == 1 -> processRecipe(source, source.knownRecipes.get(argString))
            pickedRecipes.size > 1 -> chooseRecipe(pickedRecipes)
            else -> source.displayToMe("Couldn't find recipe ${args.joinToString(" ")}.")
        }
    }

    private fun chooseRecipe(recipes: List<Recipe>) {
        val message = "Craft which recipe?${recipes.joinToString { "\n\t${it.name}" }}"
        val response = ResponseRequest(message, recipes.associate { it.name to "craft ${it.name}" })
         CommandParser.setResponseRequest(response)
    }

    private fun processRecipe(source: Player, recipe: Recipe) {
        val tool = source.target.currentLocation().findActivatorsByProperties(recipe.toolProperties).firstOrNull()
                ?: source.target.inventory.findItemsByProperties(recipe.toolProperties).firstOrNull()
        if (!recipe.toolProperties.isEmpty() && tool == null) {
            source.displayToMe("Couldn't find the necessary tools to create ${recipe.name}")
        } else if (!recipe.matches(source.target.inventory.getAllItems(), tool)) {
            source.displayToMe("Couldn't find all the needed ingredients to create ${recipe.name}.")
        } else {
            EventManager.postEvent(CraftRecipeEvent(source, recipe, tool))
        }
    }

}