package travel.climb

import core.events.Event

class ClimbJourneyEvent(val desiredStep: Int, val force: Boolean = false) : Event