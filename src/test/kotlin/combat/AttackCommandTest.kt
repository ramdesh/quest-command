package combat

import combat.attack.AttackCommand
import combat.attack.StartAttackEvent
import core.DependencyInjector
import core.GameManager
import core.GameState
import core.ai.AIManager
import core.ai.action.dsl.AIActionsCollection
import core.ai.action.dsl.AIActionsMock
import core.ai.dsl.AIsCollection
import core.ai.dsl.AIsMock
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsMock
import core.body.BodyManager
import core.events.EventListenersCollection
import core.events.EventListenersMock
import core.events.EventManager
import core.target.Target
import injectAllDefaultMocks
import org.junit.Before
import org.junit.Test
import system.BodyFakeParser
import system.location.LocationFakeParser
import traveling.location.location.LocationManager
import traveling.location.location.LocationParser
import traveling.location.weather.WeatherFakeParser
import traveling.location.weather.WeatherManager
import traveling.location.weather.WeatherParser
import kotlin.test.assertEquals

class AttackCommandTest {
    private val command = AttackCommand()

    @Before
    fun setup() {
        injectAllDefaultMocks()
        EventManager.clear()

        GameState.player = GameManager.newPlayer()
    }

    @Test
    fun attackCreatureWithoutDirection() {
        val rat = Target("Rat", bodyName = "human")
        GameState.currentLocation().addTarget(rat)

        command.execute("sl", "rat".split(" "))
        val event = EventManager.getUnexecutedEvents()[0] as StartAttackEvent
        assertEquals(rat, event.target.target)
    }
}
