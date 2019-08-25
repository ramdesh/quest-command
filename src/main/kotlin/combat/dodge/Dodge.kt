package combat.dodge

import core.events.EventListener
import core.gameState.GameState

class Dodge : EventListener<DodgeEvent>() {

    override fun execute(event: DodgeEvent) {
        if (GameState.battle != null) {
            val combatant = GameState.battle!!.getCombatant(event.source)
            if (combatant != null) {
                combatant.creature.position = combatant.creature.position.add(event.direction)
//                display("${event.source} dodged to the ${event.direction}.")
            }
        }
    }

}