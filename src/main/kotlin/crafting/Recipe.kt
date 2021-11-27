package crafting

import core.properties.Properties
import core.thing.Thing
import core.utility.Named
import status.Soul

data class Recipe(
    override val name: String,
    val ingredients: List<RecipeIngredient>,
    val skills: Map<String, Int> = mapOf(),
    val toolProperties: Properties = Properties(),
    val results: List<RecipeResult> = listOf(),
    val craftVerb: String = "craft"
) : Named {

    fun matches(crafter: Thing, ingredients: List<Thing>, tool: Thing?): Boolean {
        return toolMatches(tool) && ingredientsMatch(crafter, ingredients, tool)
    }

    private fun toolMatches(tool: Thing?): Boolean {
        return (tool?.properties ?: Properties()).hasAll(this.toolProperties)
    }

    fun canBeCraftedBy(crafter: Thing, tool: Thing?): Boolean {
        return hasSkillsToCraft(crafter.soul) && matches(crafter, crafter.inventory.getAllItems(), tool)
    }

    fun hasSkillsToCraft(soul: Soul): Boolean {
        skills.forEach {
            val current = soul.getStatOrNull(it.key)
            if (current == null || current.current < it.value) {
                return false
            }
        }
        return true
    }

    fun getUsedIngredients(crafter: Thing, availableItems: List<Thing>, tool: Thing?): List<Thing> {
        val ingredientsLeft = availableItems.toMutableList()
        val usedIngredients = mutableListOf<Thing>()
        this.ingredients.forEach {
            val match = it.findMatchingIngredient(crafter, ingredientsLeft, tool)
            if (match != null) {
                ingredientsLeft.remove(match)
                usedIngredients.add(match)
            }
        }
        return usedIngredients
    }

    fun getResults(usedIngredients: List<Thing>): List<Thing> {
        return results.map { it.getResult(usedIngredients) }
    }

    private fun ingredientsMatch(crafter: Thing, ingredients: List<Thing>, tool: Thing?): Boolean {
        val ingredientsLeft = ingredients.toMutableList()
        this.ingredients.forEach {
            val match = it.findMatchingIngredient(crafter, ingredientsLeft, tool)
            if (match == null) {
                return false
            } else {
                ingredientsLeft.remove(match)
            }
        }
        return true
    }

    fun read(): String {
        return "$name:" + readIngredients() + readSkills() + readTools() + readResults()
    }

    private fun readIngredients(): String {
        return if (ingredients.isEmpty()) {
            ""
        } else {
            "\n\tIngredients: ${ingredients.joinToString(", ") { it.read() }}"
        }
    }

    private fun readResults(): String {
        return if (results.isEmpty()) {
            ""
        } else {
            "\n\tResults: ${results.joinToString(", ") { it.read() }}"
        }
    }

    private fun readTools(): String {
        return if (toolProperties.isEmpty()) {
            ""
        } else {
            "\n\tTool: Something $toolProperties"
        }
    }

    private fun readSkills(): String {
        return if (skills.isEmpty()) {
            ""
        } else {
            "\n\tIngredients: ${skills.entries.joinToString(", ") { "${it.value} ${it.key}" }}"
        }
    }


}