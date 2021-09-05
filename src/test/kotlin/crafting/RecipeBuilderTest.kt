package crafting

import core.properties.Properties
import core.properties.Tags
import org.junit.Test
import kotlin.test.assertEquals


class RecipeBuilderTest {

    @Test
    fun basicBuild() {
        val expected = Recipe(
            "Sliced Food",
            listOf(RecipeIngredient(tags = Tags(listOf("Food", "Slicable")))),
            mapOf("Cooking" to 1),
            Properties(tags = Tags("Sharp")),
            listOf(RecipeResult(id = 0, tagsAdded = Tags(listOf("Sliced")), tagsRemoved = Tags(listOf("Slicable")))),
            "slice"
        )


        val actual = recipe("Sliced Food") {
            verb("slice")
            skill("Cooking", 1)
            ingredient("Food", "Slicable")
            toolProps {
                tag("Sharp")
            }
            result {
                id(0)
                addTag("Sliced")
                removeTag("Slicable")
            }

        }.build()

        assertEquals(expected, actual)
    }

}