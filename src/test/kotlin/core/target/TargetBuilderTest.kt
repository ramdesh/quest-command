package core.target

import core.DependencyInjector
import core.ai.behavior.*
import core.body.BodyManager
import core.conditional.ConditionalStringPointer
import core.conditional.ConditionalStringType
import core.properties.Properties
import core.properties.Tags
import core.properties.Values
import explore.look.LookEvent
import org.junit.Before
import org.junit.Test
import quests.ConditionalEvents
import system.BodyFakeParser
import traveling.location.location.LocationParser
import kotlin.math.exp
import kotlin.test.assertEquals

//TODO - asserts on basics
//TODO - test extends

class TargetBuilderTest {

    @Before
    fun setup() {
        val bodyParser = BodyFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, bodyParser)
        BodyManager.reset()

        val behaviorParser = BehaviorsMock(listOf(Behavior("Burnable", ConditionalEvents(LookEvent::class.java))))
        DependencyInjector.setImplementation(BehaviorsCollection::class.java, behaviorParser)
        BehaviorManager.reset()
    }

    @Test
    fun basicBuild() {
        val expected = Target(
            "Bob",
            params = mapOf("this" to "that"),
            dynamicDescription = ConditionalStringPointer("A normal dude"),
            behaviorRecipes = listOf(BehaviorRecipe("Burnable", mapOf("fireHealth" to "1"))),
            properties = Properties(Tags(listOf("Person")))
        )


        val actual = target("Bob"){
            description("A normal dude")
            params("this" to "that")
            props {
                tag("Person")
            }
            behavior("Burnable", "fireHealth" to 1)
        }.build()

        assertEquals(expected, actual)
    }

    @Test
    fun buildAnother() {
        val expected = Target(
            "Jim",
            params = mapOf("another" to "thing"),
            dynamicDescription = ConditionalStringPointer("A fine fellow"),
            properties = Properties(Tags(listOf("Warrior")))
        )

        val actual = target("Jim"){
            description("A fine fellow", ConditionalStringType.LOCATION_DESCRIPTION)
            params("another" to "thing")
            props {
                tag("Warrior")
            }
        }.build()

        assertEquals(expected, actual)
    }
}