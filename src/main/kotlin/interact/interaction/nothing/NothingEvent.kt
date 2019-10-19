package interact.interaction.nothing

import core.events.Event
import core.gameState.Target

class NothingEvent(val source: Target) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}