package use.actions

import core.events.Event
import core.events.EventManager
import core.history.display
import core.history.displayToMe
import core.utility.asSubject
import core.utility.isAre
import use.UseEvent
import use.UseListener

class NoUseFound : UseListener() {
    private var checkedEvent: Event? = null

    override suspend fun shouldExecute(event: UseEvent): Boolean {
        if (event != checkedEvent) {
            checkedEvent = event
            return EventManager.getNumberOfMatchingListeners(event) == 0
        }
        return false
    }

    override suspend fun executeUseEvent(event: UseEvent) {
        if (event.creature.canInteract()) {
            if (!event.usedOn.isWithinRangeOf(event.creature)) {
                event.creature.display{event.creature.asSubject(it) + " " + event.creature.isAre(it) + " too far away to interact with ${event.usedOn}."}
            } else if (event.usedOn.canConsume(event)) {
                event.usedOn.consume(event)
            } else {
                event.creature.display{"${event.creature.asSubject(it)} use ${event.used.name} on ${event.usedOn.name} but nothing happens."}
            }
        } else {
            event.creature.displayToMe("You can't interact with that right now.")
        }
    }
}