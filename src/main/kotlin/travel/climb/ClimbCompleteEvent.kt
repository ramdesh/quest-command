package travel.climb

import core.events.Event
import core.gameState.GameState
import core.gameState.Target
import core.gameState.location.LocationPoint

class ClimbCompleteEvent(val creature: Target = GameState.player, val climbTarget: Target, val origin: LocationPoint, val destination: LocationPoint) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}