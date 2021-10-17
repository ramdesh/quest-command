package core.commands

import core.GameManager
import core.GameState
import core.events.EventManager
import core.history.GameLogger
import core.properties.Properties
import core.properties.Tags
import core.thing.Thing
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals

class ResponseRequestIntegrationTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            EventManager.registerListeners()
        }
    }

    @Before
    fun reset() {
        GameManager.newGame(testing = true)
        EventManager.executeEvents()
    }

    @Test
    fun takeSecondObject() {
        val props = Properties(tags = Tags("Item"))
        GameState.player.thing.currentLocation().addThing(Thing("Wheat Bundle", properties = props))
        GameState.player.thing.currentLocation().addThing(Thing("Wheat Flour", properties = props))

        val input = "pickup wheat && 2"
        CommandParser.parseCommand(input)
        assertEquals("Player picked up Wheat Flour.", GameLogger.main.getLastOutput())
    }

}