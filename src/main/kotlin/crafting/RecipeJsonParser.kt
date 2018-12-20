package crafting

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

class RecipeJsonParser : RecipeParser {
    private val recipes by lazy{ NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/recipes", ::parseFile))}
    private fun parseFile(path: String): List<Recipe> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadRecipes(): NameSearchableList<Recipe> {
        return recipes
    }

}