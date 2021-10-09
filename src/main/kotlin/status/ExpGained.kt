package status

import core.events.EventListener
import core.history.display
import core.history.displayYou

class ExpGained : EventListener<ExpGainedEvent>() {
    override fun shouldExecute(event: ExpGainedEvent): Boolean {
        return event.creature.isPlayer()
    }

    override fun execute(event: ExpGainedEvent) {
        val stat = event.creature.soul.getStatOrNull(event.stat)
        if (stat != null && event.amount > 0) {
            if (stat.expExponential > 1) {
                event.creature.displayYou("You gained ${event.amount} exp in ${stat.name}")
            }
            stat.addEXP(event.amount)
        }
    }
}