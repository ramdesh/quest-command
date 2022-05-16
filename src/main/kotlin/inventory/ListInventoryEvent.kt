package inventory

import core.Player
import core.events.Event
import core.thing.Thing

data class ListInventoryEvent(val source: Player, val thing: Thing) : Event