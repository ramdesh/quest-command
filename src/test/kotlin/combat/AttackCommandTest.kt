package combat

import combat.attack.AttackCommand
import combat.attack.StartAttackEvent
import core.GameManager
import core.GameState
import core.events.EventManager
import core.target.Target
import core.target.activator.ActivatorManager
import core.target.target
import createMockedGame
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AttackCommandTest {
    private val command = AttackCommand()

    @Before
    fun setup() {
        createMockedGame()
        EventManager.clear()
    }

    @Test
    fun attackCreatureWithoutDirection() {
        val player = GameManager.newPlayer()
        val rat = target("Rat"){
            body("human")
        }.build()
        player.currentLocation().addTarget(rat)

        command.execute(player, "sl", "rat".split(" "))
        val event = EventManager.getUnexecutedEvents()[0] as StartAttackEvent
        assertEquals(rat, event.target.target)
    }
}
