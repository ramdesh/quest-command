package traveling.jump

import core.GameState
import core.commands.Command
import core.events.EventManager
import core.history.display
import core.properties.IS_CLIMBING
import core.target.Target
import traveling.direction.Direction

//TODO - eventually jump to specific part while climbing (in any direction)
class JumpCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Jump", "j")
    }

    override fun getDescription(): String {
        return "Jump over obstacles or down to a lower area."

    }

    override fun getManual(): String {
        return """
	Jump <obstacle> - Jump over an obstacle. X
	Jump - Jump down to the location below, possibly taking damage."""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        if (source.properties.values.getBoolean(IS_CLIMBING)) {
            val playerLocation = source.location
            val targetLocation= source.climbTarget!!.location
            EventManager.postEvent(JumpEvent(source = playerLocation, destination = targetLocation, fallDistance = playerLocation.getDistanceToLowestNodeInNetwork()))
        } else {
            val found = source.location.getNeighbors(Direction.BELOW).firstOrNull()
            if (found != null) {
                EventManager.postEvent(JumpEvent(source = source.location, destination = found))
            } else {
                display("Couldn't find anything below to jump down to.")
            }
        }
    }

}